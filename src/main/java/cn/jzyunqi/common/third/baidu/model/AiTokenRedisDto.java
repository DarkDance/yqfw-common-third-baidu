package cn.jzyunqi.common.third.baidu.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @date 2020/9/21.
 */
@Getter
@Setter
public class AiTokenRedisDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -6450423948955549727L;

    /**
     * 授权token
     */
    private String token;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
