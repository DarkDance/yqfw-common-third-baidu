package cn.jzyunqi.common.third.baidu.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.enums.Granularity;
import cn.jzyunqi.common.third.baidu.enums.Language;
import cn.jzyunqi.common.third.baidu.response.AccurateBasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * @author wiiyaya
 * @date 2020/9/21.
 */
@Slf4j
public class BaiduOcrClient {
    /**
     * 通用文字识别（高精度版）
     */
    private static final String BD_OCR_ACCURATE_BASIC_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=%s";

    /**
     * 通用文字识别（高精度含位置版）
     */
    private static final String BD_OCR_ACCURATE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate?access_token=%s";

    private final RestTemplate restTemplate;

    public BaiduOcrClient() {
        this.restTemplate = new RestTemplate();
    }

    public AccurateBasicResponse ttsAccurate(String base64File, String token)throws BusinessException {
        return ttsAccurate(base64File, Language.CHN_ENG, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Granularity.big, Boolean.FALSE, token);
    }

    public AccurateBasicResponse ttsAccurate(String base64File, Language language, Boolean direction, Boolean paragraph, Boolean probability,
                                             Boolean withLocation, Granularity granularity, Boolean vLocation, String token)throws BusinessException {
        AccurateBasicResponse accurateBasicResponse;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            URI findScheduleUri = new URIBuilder(withLocation ? String.format(BD_OCR_ACCURATE_URL, token) : String.format(BD_OCR_ACCURATE_BASIC_URL, token)).build();

            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("image", base64File);
            params.add("language_type", language);//识别语言类型
            params.add("detect_direction", direction);//是否检测图像朝向
            params.add("paragraph", paragraph);//是否输出段落信息
            params.add("probability", probability);//是否返回识别结果中每一行的置信度

            if(withLocation){
                params.add("recognize_granularity", granularity);//是否定位单字符位置
                params.add("vertexes_location", vLocation);//是否返回文字外接多边形顶点位置
            }

            RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(params, httpHeaders, HttpMethod.POST, findScheduleUri);
            ResponseEntity<AccurateBasicResponse> responseEntity  = restTemplate.exchange(requestEntity, AccurateBasicResponse.class);
            accurateBasicResponse = Optional.ofNullable(responseEntity.getBody()).orElseGet(AccurateBasicResponse::new);
        }catch (Exception e) {
            log.error("======BaiduOcrHelper ttsAccurate error:", e);
            throw new BusinessException("common_error_bd_ttsAccurate_error");
        }

        if(accurateBasicResponse.getErrorCode() == null){
            return accurateBasicResponse;
        }else{
            log.error("======BaiduOcrHelper ttsAccurate 200 error[{}][{}]", accurateBasicResponse.getErrorCode(), accurateBasicResponse.getErrorMsg());
            throw new BusinessException("common_error_bd_ttsAccurate_failed");
        }
    }

}
