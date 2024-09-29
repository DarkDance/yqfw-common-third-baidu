package cn.jzyunqi.common.third.baidu.map.address.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2018/12/10.
 */
@Getter
@AllArgsConstructor
public enum Coordinate {

    /**
     * GPS经纬度坐标
     */
    wgs84ll,

    /**
     * GPS米制坐标
     */
    wgs84mc,

    /**
     * 国测局经纬度坐标，仅限中国
     */
    gcj02ll,

    /**
     * 国测局米制坐标，仅限中国
     */
    gcj02mc,

    /**
     * 百度经纬度坐标
     */
    bd09ll,

    /**
     * 百度米制坐标
     */
    bd09mc,
    ;
}
