package cn.jzyunqi.common.third.baidu.image;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.LockType;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.common.constant.BaiduCache;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenData;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenRedisDto;
import cn.jzyunqi.common.third.baidu.image.ai.BaiduAiImgApiProxy;
import cn.jzyunqi.common.third.baidu.image.ai.enums.TaskStatus;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgData;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgDataV2;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgParam;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgParamV2;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgRsp;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgRspV2;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@Slf4j
public class BaiduAiImgClient {

    @Resource
    private BaiduTokenApiProxy baiduTokenApiProxy;

    @Resource
    private BaiduAiImgApiProxy baiduNLPWenxinApiProxy;

    @Resource
    private RedisHelper redisHelper;

    @Resource
    private BaiduAiImgClientConfig baiduNLPClientConfig;

    public final Img img = new Img();

    public class Img {

        //AI作画 - 基础版
        public List<String> generateImage(String prompt, Integer width, Integer height, String style) throws BusinessException {
            Text2ImgParam request = new Text2ImgParam();
            request.setText(prompt);
            request.setResolution(String.format("%d*%d", width, height));
            request.setStyle(style);
            request.setNum(1);
            //request.setTextContent("");
            request.setTextCheck(1);
            Long taskId = baiduNLPWenxinApiProxy.text2Image(getClientToken(), request).getData().getTaskId();

            Text2ImgData queryRequest = new Text2ImgData();
            queryRequest.setTaskId(taskId);
            Text2ImgRsp response;
            Duration duration = Duration.ZERO;
            do {
                if (duration.toSeconds() > 0) {
                    log.info("任务{}等待中，等待时间：{}秒", taskId, duration.toSeconds());
                    try {
                        TimeUnit.SECONDS.sleep(duration.toSeconds());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                response = baiduNLPWenxinApiProxy.getImg(getClientToken(), queryRequest);
                duration = Duration.parse("PT" + response.getData().getWaiting());
            } while (duration.toSeconds() > 0);
            return response.getData().getImgUrls().stream().map(Text2ImgData.ImgData::getImage).toList();
        }

        //AI作画 - 高级版/急速版 - 请求绘画
        public List<String> generateImageV2(String prompt, Integer width, Integer height, org.springframework.core.io.Resource image, Integer changeDegree, boolean speed) throws BusinessException {
            Text2ImgParamV2 request = new Text2ImgParamV2();
            request.setPrompt(prompt);
            request.setWidth(width);
            request.setHeight(height);
            request.setImageNum(1);
            request.setImage(getResourceBase64(image));
            request.setChangeDegree(changeDegree);
            //request.setTextContent("");
            //request.setTaskTimeOut();
            request.setTextCheck(1);
            String taskId = speed ?
                    baiduNLPWenxinApiProxy.text2ImageEx(getClientToken(), request).getData().getTaskId() :
                    baiduNLPWenxinApiProxy.text2ImageV2(getClientToken(), request).getData().getTaskId();

            Text2ImgDataV2 queryRequest = new Text2ImgDataV2();
            queryRequest.setTaskId(taskId);
            Text2ImgRspV2 response;

            int attempt = 0;
            do {
                response = speed ?
                        baiduNLPWenxinApiProxy.getImgEx(getClientToken(), queryRequest) :
                        baiduNLPWenxinApiProxy.getImgV2(getClientToken(), queryRequest);
                try {
                    int jitterDelayMillis = jitterDelayMillis(attempt);
                    attempt++;
                    log.info("任务{}等待中，等待时间：{}毫秒", taskId, jitterDelayMillis);
                    TimeUnit.MILLISECONDS.sleep(jitterDelayMillis);
                } catch (InterruptedException e) {
                    throw new BusinessException(e, "任务超时");
                }
                if (attempt > 3) {
                    throw new BusinessException("任务超时");
                }
            } while (!response.getData().getTaskProgress());

            List<Text2ImgDataV2.ImgData> imgData = response.getData().getSubTaskResultList().stream()
                    .filter(subTaskResult -> subTaskResult.getSubTaskStatus() == TaskStatus.SUCCESS)
                    .flatMap(subTaskResult -> subTaskResult.getFinalImageList().stream())
                    .toList();
            return imgData.stream().map(Text2ImgDataV2.ImgData::getImgUrl).toList();
        }
    }

    private String getClientToken() throws BusinessException {
        ClientTokenRedisDto clientToken = (ClientTokenRedisDto) redisHelper.vGet(BaiduCache.BAIDU_NLP_V, getClientTokenKey());
        if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
            return clientToken.getToken();
        }
        Lock lock = redisHelper.getLock(BaiduCache.BAIDU_NLP_V, getClientTokenKey().concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                clientToken = (ClientTokenRedisDto) redisHelper.vGet(BaiduCache.BAIDU_NLP_V, getClientTokenKey());
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                }

                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!locked && System.currentTimeMillis() > timeOutMillis) {
                    throw new InterruptedException("获取accessToken超时：获取时间超时");
                }
            } while (!locked);

            //获取到锁的服务可以去获取accessToken
            ClientTokenData clientTokenData = baiduTokenApiProxy.getClientToken(baiduNLPClientConfig.getAppId(), baiduNLPClientConfig.getAppSecret());
            clientToken = new ClientTokenRedisDto();
            clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
            clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

            redisHelper.vPut(BaiduCache.BAIDU_NLP_V, getClientTokenKey(), clientToken);
            return clientTokenData.getAccessToken();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String getClientTokenKey() {
        return "client_token:" + baiduNLPClientConfig.getAppId();
    }

    private static String getResourceBase64(org.springframework.core.io.Resource resource) throws BusinessException {
        if (resource == null) {
            return null;
        }
        String base64Content;
        try {
            base64Content = DigestUtilPlus.Base64.encodeBase64String(resource.getContentAsByteArray());
        } catch (IOException e) {
            throw new BusinessException(e, "资源读取失败");
        }
        return base64Content;
    }

    private int jitterDelayMillis(int attempt) {
        double delay = (double) 5000 * Math.pow(1.5, attempt);
        double jitter = delay * 0.2;
        return (int) (delay + (double) RandomUtilPlus.Number.nextInt(0, (int) jitter));
    }
}
