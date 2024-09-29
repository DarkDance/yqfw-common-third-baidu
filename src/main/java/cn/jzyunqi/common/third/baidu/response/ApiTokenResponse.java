package cn.jzyunqi.common.third.baidu.response;

import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @date 2020/9/21.
 */
@Getter
@Setter
public class ApiTokenResponse extends BaiduRspV2 {
    private static final long serialVersionUID = -8802987797086759958L;

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
