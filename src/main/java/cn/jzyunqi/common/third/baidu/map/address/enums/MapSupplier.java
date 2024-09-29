package cn.jzyunqi.common.third.baidu.map.address.enums;

import cn.jzyunqi.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Getter
@AllArgsConstructor
public enum MapSupplier {
    /**
     * GPS地图
     */
    gps(Coordinate.wgs84ll),

    /**
     * 高德地图
     */
    amap(Coordinate.gcj02ll),

    /**
     * 腾讯地图
     */
    tencent(Coordinate.gcj02ll),

    /**
     * 百度地图
     */
    baidu_ll(Coordinate.bd09ll),

    /**
     * 百度地图
     */
    baidu_mc(Coordinate.bd09mc),
    ;

    private final Coordinate coordinate;

    public static String parseModel(MapSupplier from, MapSupplier to) throws BusinessException {
        if (from.coordinate == Coordinate.gcj02ll && to.coordinate == Coordinate.bd09ll) {
            return "1";
        }
        if (from.coordinate == Coordinate.wgs84ll && to.coordinate == Coordinate.bd09ll) {
            return "2";
        }
        if (from.coordinate == Coordinate.bd09ll && to.coordinate == Coordinate.bd09mc) {
            return "3";
        }
        if (from.coordinate == Coordinate.bd09mc && to.coordinate == Coordinate.bd09ll) {
            return "4";
        }
        if (from.coordinate == Coordinate.bd09ll && to.coordinate == Coordinate.gcj02ll) {
            return "5";
        }
        if (from.coordinate == Coordinate.bd09mc && to.coordinate == Coordinate.gcj02ll) {
            return "6";
        }
        throw new BusinessException("Unsupported coordinate transform!!");
    }
}
