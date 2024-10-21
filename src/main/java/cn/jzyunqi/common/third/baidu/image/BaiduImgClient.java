package cn.jzyunqi.common.third.baidu.image;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.LockType;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.common.constant.BaiduCache;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenData;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenRedisDto;
import cn.jzyunqi.common.third.baidu.image.ocr.BaiduImgOcrApiProxy;
import cn.jzyunqi.common.third.baidu.image.ocr.enums.EngGranularity;
import cn.jzyunqi.common.third.baidu.image.ocr.enums.Language;
import cn.jzyunqi.common.third.baidu.image.ocr.enums.RecognizeGranularity;
import cn.jzyunqi.common.third.baidu.image.ocr.model.WordData;
import cn.jzyunqi.common.third.baidu.image.search.BaiduImgSearchApiProxy;
import cn.jzyunqi.common.third.baidu.image.search.model.ProductPagData;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Slf4j
public class BaiduImgClient {

    @Resource
    private BaiduTokenApiProxy baiduTokenApiProxy;

    @Resource
    private BaiduImgOcrApiProxy baiduImgOcrApiProxy;

    @Resource
    private BaiduImgSearchApiProxy baiduImgSearchApiProxy;

    @Resource
    private BaiduImgClientConfig baiduImgClientConfig;

    @Resource
    private RedisHelper redisHelper;

    public final Ocr ocr = new Ocr();
    public final Search search = new Search();

    public class Ocr {
        //文字识别 - 通用文字识别（高精度版）
        public WordData wordsList(org.springframework.core.io.Resource image,
                                  Language languageType,
                                  Boolean detectDirection,
                                  Boolean paragraph,
                                  Boolean probability,
                                  Boolean multiDirectionalRecognize
        ) throws BusinessException {
            return baiduImgOcrApiProxy.wordsList(getClientToken(), getResourceBase64(image), null, null, null, null, null, languageType, detectDirection, paragraph, probability, multiDirectionalRecognize);
        }

        //文字识别 - 通用文字识别（高精度版）
        public WordData wordsWithPositionList(org.springframework.core.io.Resource image,
                                              Language languageType,
                                              EngGranularity engGranularity,
                                              RecognizeGranularity recognizeGranularity,
                                              Boolean detectDirection,
                                              Boolean vertexesLocation,
                                              Boolean paragraph,
                                              Boolean probability,
                                              Boolean charProbability,
                                              Boolean multiDirectionalRecognize
        ) throws BusinessException {
            return baiduImgOcrApiProxy.wordsWithPositionList(getClientToken(), getResourceBase64(image), null, null, null, null, null, languageType, engGranularity, recognizeGranularity, detectDirection, vertexesLocation, paragraph, probability, charProbability, multiDirectionalRecognize);
        }
    }

    public class Search {
        //图像搜索 - 商品图片搜索 - 入库
        public String productAdd(org.springframework.core.io.Resource image, String brief) throws BusinessException {
            return baiduImgSearchApiProxy.productAdd(getClientToken(), getResourceBase64(image), null, brief, null, null).getContSign();
        }

        //图像搜索 - 商品图片搜索 - 检索
        public ProductPagData productSearch(org.springframework.core.io.Resource image, Integer start, Integer limit) throws BusinessException {
            return baiduImgSearchApiProxy.productSearch(getClientToken(), getResourceBase64(image), null, null, null, null, start, limit);
        }

        //图像搜索 - 商品图片搜索 - 删除
        public void productDelete(String contSign) throws BusinessException {
            baiduImgSearchApiProxy.productDelete(getClientToken(), null, null, contSign);
        }

        //图像搜索 - 商品图片搜索 - 更新
        public void productUpdate(String contSign, String brief, String classId1, String classId2) throws BusinessException {
            baiduImgSearchApiProxy.productUpdate(getClientToken(), null, null, contSign, brief, classId1, classId2);
        }
    }

    private String getClientToken() throws BusinessException {
        ClientTokenRedisDto clientToken = (ClientTokenRedisDto) redisHelper.vGet(BaiduCache.THIRD_BAIDU_IMG_V, getClientTokenKey());
        if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
            return clientToken.getToken();
        }
        Lock lock = redisHelper.getLock(BaiduCache.THIRD_BAIDU_IMG_V, getClientTokenKey().concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                clientToken = (ClientTokenRedisDto) redisHelper.vGet(BaiduCache.THIRD_BAIDU_IMG_V, getClientTokenKey());
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                }

                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!locked && System.currentTimeMillis() > timeOutMillis) {
                    throw new InterruptedException("获取accessToken超时：获取时间超时");
                }
            } while (!locked);

            //获取到锁的服务可以去获取accessToken
            ClientTokenData clientTokenData = baiduTokenApiProxy.getClientToken(baiduImgClientConfig.getAppId(), baiduImgClientConfig.getAppSecret());
            clientToken = new ClientTokenRedisDto();
            clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
            clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

            redisHelper.vPut(BaiduCache.THIRD_BAIDU_IMG_V, getClientTokenKey(), clientToken);
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
        return "client_token:" + baiduImgClientConfig.getAppId();
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
}
