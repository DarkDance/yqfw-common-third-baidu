package cn.jzyunqi.common.third.baidu.image.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@Getter
@AllArgsConstructor
public enum ResolutionType {
    _512x512("512*512"),
    _640x360("640*360"),
    _360x640("360*640"),
    _1024x1024("1024*1024"),
    _720x1280("720*1280"),
    _1280x720("1280*720"),
    ;
    private final String value;
}
