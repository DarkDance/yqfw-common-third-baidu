package cn.jzyunqi.common.third.baidu.image;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.LockType;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.common.constant.BaiduCache;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenData;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenRedisDto;
import cn.jzyunqi.common.third.baidu.image.search.BaiduImgClassifyApiProxy;
import cn.jzyunqi.common.third.baidu.image.search.model.ProductPagData;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
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
    private BaiduImgClassifyApiProxy baiduImgClassifyApiProxy;

    @Resource
    private BaiduImgClientConfig baiduImgClientConfig;

    @Resource
    private RedisHelper redisHelper;

    public final Search search = new Search();

    public class Search {
        //图像搜索 - 商品图片搜索 - 入库
        public String productAdd(org.springframework.core.io.Resource image, String brief) throws BusinessException {
            return baiduImgClassifyApiProxy.productAdd(getClientToken(), getImageBase64(image), null, brief, null, null).getContSign();
        }

        //图像搜索 - 商品图片搜索 - 检索
        public ProductPagData productSearch(org.springframework.core.io.Resource image, Integer start, Integer limit) throws BusinessException {
            return baiduImgClassifyApiProxy.productSearch(getClientToken(), getImageBase64(image), null, null, null, null, start, limit);
        }

        //图像搜索 - 商品图片搜索 - 删除
        public void productDelete(String contSign) throws BusinessException {
            baiduImgClassifyApiProxy.productDelete(getClientToken(), null, null, contSign);
        }

        //图像搜索 - 商品图片搜索 - 更新
        public void productUpdate(String contSign, String brief, String classId1, String classId2) throws BusinessException {
            baiduImgClassifyApiProxy.productUpdate(getClientToken(), null, null, contSign, brief, classId1, classId2);
        }

        private static String getImageBase64(org.springframework.core.io.Resource image) throws BusinessException {
            String base64Image;
            try {
                base64Image = DigestUtilPlus.Base64.encodeBase64String(image.getContentAsByteArray());
            } catch (IOException e) {
                throw new BusinessException(e, "图片读取失败");
            }
            return base64Image;
        }
    }

    private String getClientToken() throws BusinessException {
        ClientTokenRedisDto clientToken = (ClientTokenRedisDto) redisHelper.vGet(BaiduCache.BAIDU_IMG_V, getClientTokenKey());
        if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
            return clientToken.getToken();
        }
        Lock lock = redisHelper.getLock(BaiduCache.BAIDU_IMG_V, getClientTokenKey().concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                clientToken = (ClientTokenRedisDto) redisHelper.vGet(BaiduCache.BAIDU_IMG_V, getClientTokenKey());
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

            redisHelper.vPut(BaiduCache.BAIDU_IMG_V, getClientTokenKey(), clientToken);
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
}
