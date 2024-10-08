package cn.jzyunqi.common.third.baidu.nlp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.LockType;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.common.constant.BaiduCache;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenData;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenRedisDto;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.BaiduNLPWenxinApiProxy;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.enums.ResolutionType;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.model.Text2ImgData;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.model.Text2ImgParam;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.model.Text2ImgRsp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@Slf4j
public class BaiduNLPClient {

    @Resource
    private BaiduTokenApiProxy baiduTokenApiProxy;

    @Resource
    private BaiduNLPWenxinApiProxy baiduNLPWenxinApiProxy;

    @Resource
    private RedisHelper redisHelper;

    @Resource
    private BaiduNLPClientConfig baiduNLPClientConfig;

    public final Img img = new Img();

    public class Img {

        //AI作画 - 基础版 - 请求绘画
        public Long text2Image(String text, ResolutionType resolution, String style) throws BusinessException {
            Text2ImgParam request = new Text2ImgParam();
            request.setText(text);
            request.setResolution(resolution.getValue());
            request.setStyle(style);
            request.setNum(1);
            //request.setTextContent("");
            request.setTextCheck(1);
            return baiduNLPWenxinApiProxy.text2Image(getClientToken(), request).getData().getTaskId();
        }

        //AI作画 - 基础版 - 查询结果
        public List<String> getImg(Long taskId) throws BusinessException {
            Text2ImgData request = new Text2ImgData();
            request.setTaskId(taskId);
            Text2ImgRsp response = baiduNLPWenxinApiProxy.getImg(getClientToken(), request);
            return response.getData().getImgUrls().stream().map(Text2ImgData.ImgData::getImage).toList();
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
}
