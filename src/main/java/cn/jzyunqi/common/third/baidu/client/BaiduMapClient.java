package cn.jzyunqi.common.third.baidu.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.enums.CoordinateType;
import cn.jzyunqi.common.third.baidu.model.Location;
import cn.jzyunqi.common.third.baidu.model.LonLatAddress;
import cn.jzyunqi.common.third.baidu.response.BaiduType3Response;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wiiyaya
 * @date 2018/12/10.
 */
@Slf4j
public class BaiduMapClient {

    private static final String MAP_GATEWAY_V1_URL = "http://api.map.baidu.com/geoconv/v1/";
    private static final String MAP_GATEWAY_V2_URL = "http://api.map.baidu.com/geocoder/v2/";

    private static final String GATEWAY_V1_SING = "/geoconv/v1/?%s%s";
    private static final String GATEWAY_V2_SING = "/geocoder/v2/?%s%s";

    /**
     * 应用AK
     */
    private final String ak;

    /**
     * 应用SK
     */
    private final String sk;

    private final RestTemplate restTemplate;

    public BaiduMapClient(String ak, String sk) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(new ObjectMapper());
        jsonConverter.setSupportedMediaTypes(CollectionUtilPlus.Array.asList(MediaType.APPLICATION_JSON, MediaType.valueOf("text/javascript;charset=UTF-8")));
        restTemplate.setMessageConverters(CollectionUtilPlus.Array.asList(jsonConverter));

        this.ak = ak;
        this.sk = sk;
        this.restTemplate = restTemplate;
    }

    /**
     * 将坐标转换为地址
     *
     * @param lon            经度
     * @param lat            纬度
     * @param coordinateType 坐标类型
     * @return 地址
     * @throws BusinessException 业务异常
     */
    public LonLatAddress lonLatToAddress(String lon, String lat, CoordinateType coordinateType) throws BusinessException {
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("ak", ak); // 用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key” 申请ak
        paramMap.put("location", StringUtilPlus.join(lat, ",", lon)); // 根据经纬度坐标获取地址
        paramMap.put("coordtype", coordinateType.getType()); // 坐标的类型
        paramMap.put("output", "json"); // 输出格式为json或者xml
        paramMap.put("language", "zh-CN"); // 指定召回的新政区划语言类型
        paramMap.put("latest_admin", "1"); // 是否访问最新版行政区划数据（仅对中国数据生效），1（访问），0（不访问）

        List<NameValuePair> nvpList = CollectionUtilPlus.Map.getUriParam(paramMap, true, true, false);
        String sign = this.getSign(GATEWAY_V2_SING, paramMap);
        nvpList.add(new BasicNameValuePair("sn", sign));

        BaiduType3Response<LonLatAddress> llTOAddressRsp;
        try {
            URI uri = new URIBuilder(MAP_GATEWAY_V2_URL)
                    .addParameters(nvpList)
                    .build();
            RequestEntity<Map<String, String>> requestEntity = new RequestEntity<>(paramMap, HttpMethod.GET, uri);
            ParameterizedTypeReference<BaiduType3Response<LonLatAddress>> responseType = new ParameterizedTypeReference<BaiduType3Response<LonLatAddress>>() {
            };
            ResponseEntity<BaiduType3Response<LonLatAddress>> sendRsp = restTemplate.exchange(requestEntity, responseType);
            llTOAddressRsp = Optional.ofNullable(sendRsp.getBody()).orElseGet(BaiduType3Response::new);
        } catch (URISyntaxException e) {
            log.error("======BaiduMapHelper lonLatToAddress other error:", e);
            throw new BusinessException("common_error_baidu_lon_lat_to_address_error");
        }

        if (llTOAddressRsp.getStatus() == 0) {
            return llTOAddressRsp.getResult();
        } else {
            log.error("======BaiduMapHelper lonLatToAddress 200 error[{}][{}]", llTOAddressRsp.getStatus(), llTOAddressRsp.getMessage());
            throw new BusinessException("common_error_baidu_lon_lat_to_address_failed");
        }
    }

    /**
     * 经纬度转换
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 转换后的值
     * @throws BusinessException 业务异常
     */
    public Location lonLatChange(String lon, String lat, CoordinateType fromType, CoordinateType toType) throws BusinessException {
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("coords", StringUtilPlus.join(lon, ",", lat)); // 根据经纬度坐标获取地址
        paramMap.put("ak", ak); // 用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key” 申请ak
        paramMap.put("from", fromType.getIndex().toString());
        paramMap.put("to", toType.getIndex().toString());
        paramMap.put("output", "json"); // 输出格式为json或者xml

        List<NameValuePair> nvpList = CollectionUtilPlus.Map.getUriParam(paramMap, true, true, false);
        String sign = this.getSign(GATEWAY_V1_SING, paramMap);
        nvpList.add(new BasicNameValuePair("sn", sign));

        BaiduType3Response<List<Map<String, String>>> lonLatChangeRsp;
        try {
            URI uri = new URIBuilder(MAP_GATEWAY_V1_URL)
                    .addParameters(nvpList)
                    .build();
            RequestEntity<Map<String, String>> requestEntity = new RequestEntity<>(paramMap, HttpMethod.GET, uri);
            ParameterizedTypeReference<BaiduType3Response<List<Map<String, String>>>> responseType = new ParameterizedTypeReference<BaiduType3Response<List<Map<String, String>>>>() {
            };
            ResponseEntity<BaiduType3Response<List<Map<String, String>>>> sendRsp = restTemplate.exchange(requestEntity, responseType);
            lonLatChangeRsp = Optional.ofNullable(sendRsp.getBody()).orElseGet(BaiduType3Response::new);
        } catch (URISyntaxException e) {
            log.error("======BaiduMapHelper lonLatChange other error:", e);
            throw new BusinessException("common_error_baidu_lon_lat_change_error");
        }

        if (lonLatChangeRsp.getStatus() == 0) {
            List<Map<String, String>> change = lonLatChangeRsp.getResult();
            Location location = new Location();
            location.setLan(new BigDecimal(change.get(0).get("x")));
            location.setLat(new BigDecimal(change.get(0).get("y")));
            return location;
        } else {
            log.error("======BaiduMapHelper lonLatChange 200 error[{}][{}]", lonLatChangeRsp.getStatus(), lonLatChangeRsp.getMessage());
            throw new BusinessException("common_error_baidu_lon_lat_change_failed");
        }
    }

    private String getSign(String gatewaySingStr, Map<String, String> paramMap) {
        String paramStr = String.format(gatewaySingStr, CollectionUtilPlus.Map.getUrlParam(paramMap, true, true, true), sk);
        try {
            return DigestUtilPlus.MD5.sign(URLEncoder.encode(paramStr, StringUtilPlus.UTF_8_S), Boolean.FALSE);
        } catch (UnsupportedEncodingException e) {
            log.error("======URLEncoder.getSign[{}] error, use original value!", paramStr, e);
            return DigestUtilPlus.MD5.sign(paramStr, Boolean.FALSE);
        }
    }
}
