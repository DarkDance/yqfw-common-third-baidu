package cn.jzyunqi.common.third.baidu.map.address;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV3;
import cn.jzyunqi.common.third.baidu.map.address.model.Location;
import cn.jzyunqi.common.third.baidu.map.address.model.LngLatAddress;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@BaiduHttpExchange
@HttpExchange(url = "https://api.map.baidu.com", accept = {"application/json"})
public interface BaiduMapAddressApiProxy {

    //坐标转换
    @PostExchange(url = "/geoconv/v2/")
    BaiduRspV3<Location> lngLatChange(@RequestParam String ak, @RequestParam String coords, @RequestParam(required = false) String model, @RequestParam(required = false) String sn, @RequestParam(required = false) String output) throws BusinessException;

    //全球逆地理编码
    @PostExchange(url = "/reverse_geocoding/v3/")
    BaiduRspV3<LngLatAddress> lngLatToAddress(@RequestParam String ak, @RequestParam String location,
                                              @RequestParam(required = false) String coordtype,
                                              @RequestParam(required = false) String ret_coordtype,
                                              @RequestParam(required = false) String extensions_poi,
                                              @RequestParam(required = false) Boolean extensions_road,
                                              @RequestParam(required = false) Boolean extensions_town,
                                              @RequestParam(required = false) Integer radius,
                                              @RequestParam(required = false) String sn,
                                              @RequestParam(required = false) String output,
                                              @RequestParam(required = false) String callback,
                                              @RequestParam(required = false) String poi_types,
                                              @RequestParam(required = false) String language,
                                              @RequestParam(required = false) String language_auto
    ) throws BusinessException;

}
