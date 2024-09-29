package cn.jzyunqi.common.third.baidu.map;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV3;
import cn.jzyunqi.common.third.baidu.map.address.BaiduMapAddressApiProxy;
import cn.jzyunqi.common.third.baidu.map.address.enums.Coordinate;
import cn.jzyunqi.common.third.baidu.map.address.enums.MapSupplier;
import cn.jzyunqi.common.third.baidu.map.address.model.LngLatAddress;
import cn.jzyunqi.common.third.baidu.map.address.model.Location;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Slf4j
public class BaiduMapClient {

    @Resource
    private BaiduMapAddressApiProxy baiduMapAddressApiProxy;

    @Resource
    private BaiduMapClientConfig baiduMapClientConfig;

    public final Address address = new Address();

    public class Address {
        //坐标转换
        BaiduRspV3<Location> lngLatChange(String lon, String lat, MapSupplier from, MapSupplier to) throws BusinessException {
            String output = "json";
            String coords = StringUtilPlus.join(lon, ",", lat);
            String model = MapSupplier.parseModel(from, to);

            Map<String, String> paramMap = new LinkedHashMap<>();
            paramMap.put("ak", baiduMapClientConfig.getAk());
            paramMap.put("coords", coords);
            paramMap.put("model", model);
            paramMap.put("output", output);

            String sn = generateSn("/geocoder/v2/?", paramMap);
            return baiduMapAddressApiProxy.lngLatChange(baiduMapClientConfig.getAk(), coords, model, sn, output);
        }

        //全球逆地理编码
        BaiduRspV3<LngLatAddress> lngLatToAddress(String lon, String lat, Coordinate coordinate, Coordinate retCoordinate) throws BusinessException {
            String output = "json";
            String location = StringUtilPlus.join(lon, ",", lat);

            Map<String, String> paramMap = new LinkedHashMap<>();
            paramMap.put("ak", baiduMapClientConfig.getAk());
            paramMap.put("location", location);
            paramMap.put("coordtype", coordinate.name());
            paramMap.put("ret_coordtype", retCoordinate.name());
            paramMap.put("output", output);

            String sn = generateSn("/reverse_geocoding/v3/?", paramMap);
            return baiduMapAddressApiProxy.lngLatToAddress(baiduMapClientConfig.getAk(), location, coordinate.name(), retCoordinate.name(), null, null, null, null, sn, output, null, null, null, null);
        }

        private String generateSn(String url, Map<String, String> paramMap) {
            String paramsStr = CollectionUtilPlus.Map.getUrlParam(paramMap, true, true, true);
            String wholeStr = url + paramsStr + baiduMapClientConfig.getSk();
            String tempStr = URLEncoder.encode(wholeStr, StringUtilPlus.UTF_8);
            return DigestUtilPlus.MD5.sign(tempStr, Boolean.FALSE);
        }
    }
}
