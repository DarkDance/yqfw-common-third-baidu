package cn.jzyunqi.common.third.baidu.common.constant;

import cn.jzyunqi.common.feature.redis.Cache;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Getter
@AllArgsConstructor
public enum BaiduCache implements Cache {

    THIRD_BAIDU_IMG_V(Duration.ZERO, Boolean.FALSE),
    THIRD_BAIDU_NLP_V(Duration.ZERO, Boolean.FALSE),
    ;

    private final Duration expiration;

    private final Boolean autoRenew;
}
