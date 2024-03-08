package cn.jzyunqi.common.third.baidu.utils;

import cn.jzyunqi.common.third.baidu.model.Location;

import java.math.BigDecimal;

/**
 * @author wiiyaya
 * @date 2018/12/11.
 */
public class GPSConverterUtils {

    private static final double PI = 3.1415926535897932384626;
    private static final double X_PI = PI * 3000.0 / 180.0;
    private static final double A = 6378245.0;// A: 卫星椭球坐标投影到平面地图坐标系的投影因子。
    private static final double EE = 0.00669342162296594323;// EE: 椭球的偏心率。

    /**
     * gps84_To_Gcj02
     */
    public static Location gps84ToGcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new Location(BigDecimal.valueOf(lat), BigDecimal.valueOf(lon));
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Location(BigDecimal.valueOf(mgLat), BigDecimal.valueOf(mgLon));
    }

    /**
     * gcj02_To_Bd09
     */
    public static Location gcj02ToBd09(double gg_lat, double gg_lon) {
        double x = gg_lon;
        double y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new Location(BigDecimal.valueOf(bd_lat), BigDecimal.valueOf(bd_lon));
    }

    /**
     * bd09_To_Gcj02
     */
    public static Location bd09ToGcj02(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new Location(BigDecimal.valueOf(gg_lat), BigDecimal.valueOf(gg_lon));
    }

    private static Boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        return lat < 0.8293 || lat > 55.8271;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
}
