package cn.jzyunqi.common.third.baidu.common.constant;

import cn.jzyunqi.common.feature.redis.Cache;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Getter
@AllArgsConstructor
public enum BaiduCache implements Cache {

    BAIDU_IMG_V(0L, Boolean.FALSE),
    BAIDU_NLP_V(0L, Boolean.FALSE),
    ;

    private final Long expiration;

    private final Boolean autoRenew;
}
