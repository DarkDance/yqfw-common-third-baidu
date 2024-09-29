package cn.jzyunqi.common.third.baidu.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Getter
@Setter
@ToString
public class ClientTokenData extends BaiduRspV2{

    /**
     * 授权token
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 过期时间
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * 授权token
     */
    private String scope;

    /**
     * 授权token
     */
    @JsonProperty("session_key")
    private String sessionKey;

    /**
     * 授权token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 授权token
     */
    @JsonProperty("session_secret")
    private String sessionSecret;
}
