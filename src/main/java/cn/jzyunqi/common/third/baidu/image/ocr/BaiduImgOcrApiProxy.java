package cn.jzyunqi.common.third.baidu.image.ocr;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange;
import cn.jzyunqi.common.third.baidu.image.ocr.enums.EngGranularity;
import cn.jzyunqi.common.third.baidu.image.ocr.enums.Language;
import cn.jzyunqi.common.third.baidu.image.ocr.enums.RecognizeGranularity;
import cn.jzyunqi.common.third.baidu.image.ocr.model.WordData;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@BaiduHttpExchange
@HttpExchange(url = "https://aip.baidubce.com", accept = {"application/json"}, contentType = "application/x-www-form-urlencoded")
public interface BaiduImgOcrApiProxy {

    //文字识别 - 通用文字识别（高精度版）
    @PostExchange(url = "/rest/2.0/ocr/v1/accurate_basic")
    WordData wordsList(@RequestParam String access_token, @RequestParam(required = false) String image, @RequestParam(required = false) String url, @RequestParam(required = false) String pdf_file, @RequestParam(required = false) String pdf_file_num, @RequestParam(required = false) String ofd_file, @RequestParam(required = false) String ofd_file_num,
                       @RequestParam(required = false) Language language_type,
                       @RequestParam(required = false) Boolean detect_direction,
                       @RequestParam(required = false) Boolean paragraph,
                       @RequestParam(required = false) Boolean probability,
                       @RequestParam(required = false) Boolean multidirectional_recognize
    ) throws BusinessException;


    //文字识别 - 通用文字识别（高精度版）
    @PostExchange(url = "/rest/2.0/ocr/v1/accurate")
    WordData wordsWithPositionList(@RequestParam String access_token, @RequestParam(required = false) String image, @RequestParam(required = false) String url, @RequestParam(required = false) String pdf_file, @RequestParam(required = false) String pdf_file_num, @RequestParam(required = false) String ofd_file, @RequestParam(required = false) String ofd_file_num,
                                   @RequestParam(required = false) Language language_type,
                                   @RequestParam(required = false) EngGranularity eng_granularity,
                                   @RequestParam(required = false) RecognizeGranularity recognize_granularity,
                                   @RequestParam(required = false) Boolean detect_direction,
                                   @RequestParam(required = false) Boolean vertexes_location,
                                   @RequestParam(required = false) Boolean paragraph,
                                   @RequestParam(required = false) Boolean probability,
                                   @RequestParam(required = false) Boolean char_probability,
                                   @RequestParam(required = false) Boolean multidirectional_recognize
    ) throws BusinessException;

}
