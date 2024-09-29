package cn.jzyunqi.common.third.baidu.common;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.Cache;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.baidu.response.ApiTokenResponse;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author wiiyaya
 * @date 2020/9/21.
 */
@Slf4j
public class BaiduApiTokenClient {

    /**
     * 获取access_token
     */
    private static final String BD_INTERFACE_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s";

    /**
     * INTERFACE_TOKEN缓存key
     */
    private static final String BD_INTERFACE_TOKEN_KEY = "INTERFACE_TOKEN";

    /**
     * 第三方用户唯一接口凭证
     */
    private final String apiKey;

    /**
     * 第三方用户唯一接口凭证密钥
     */
    private final String secretKey;

    private final RestTemplate restTemplate;

    private final RedisHelper redisHelper;

    private final ObjectMapper objectMapper;

    public BaiduApiTokenClient(String apiKey, String secretKey, RedisHelper redisHelper) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.redisHelper = redisHelper;
    }

    /**
     * 获取access_token
     *
     * @param cache 缓存
     * @return 获取access_token
     */
    public String getApiToken(Cache cache) throws BusinessException {
        AiTokenRedisDto tokenResult = (AiTokenRedisDto) redisHelper.vGet(cache, BD_INTERFACE_TOKEN_KEY);
        if (tokenResult != null && LocalDateTime.now().isBefore(tokenResult.getExpireTime())) {
            return tokenResult.getToken();
        }

        ApiTokenResponse apiTokenResponse;
        try {
            URI accessTokenUri = URI.create(String.format(BD_INTERFACE_TOKEN_URL, apiKey, secretKey));

            RequestEntity<Map<String, String>> requestEntity = new RequestEntity<>(HttpMethod.GET, accessTokenUri);
            ResponseEntity<ApiTokenResponse> aiTokenRspEntity = restTemplate.exchange(requestEntity, ApiTokenResponse.class);
            apiTokenResponse = Optional.ofNullable(aiTokenRspEntity.getBody()).orElseGet(ApiTokenResponse::new);
        } catch (HttpClientErrorException e) {
            log.error("======BaiduApiTokenHelper getApiToken client error:", e);
            try {
                BaiduRspV2 errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), BaiduRspV2.class);
                throw new BusinessException("common_error_bd_get_interface_token_error", errorRsp.getError(), errorRsp.getErrorDescription());
            } catch (IOException e1) {
                throw new BusinessException("common_error_bd_get_interface_token_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======BaiduApiTokenHelper getApiToken other error:", e);
            throw new BusinessException("common_error_bd_get_interface_token_error");
        }

        if (apiTokenResponse.getError() == null) {
            //把token放入缓存
            tokenResult = new AiTokenRedisDto();
            tokenResult.setToken(apiTokenResponse.getAccessToken()); //获取到的凭证
            tokenResult.setExpireTime(LocalDateTime.now().plusSeconds(apiTokenResponse.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒
            redisHelper.vPut(cache, BD_INTERFACE_TOKEN_KEY, tokenResult);

            return apiTokenResponse.getAccessToken();
        } else {
            log.error("======BaiduApiTokenHelper getApiToken 200 error[{}][{}]", apiTokenResponse.getError(), apiTokenResponse.getErrorDescription());
            throw new BusinessException("common_error_bd_get_interface_token_failed");
        }
    }
}
