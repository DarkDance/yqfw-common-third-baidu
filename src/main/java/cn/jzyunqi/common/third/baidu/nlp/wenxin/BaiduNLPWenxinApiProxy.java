package cn.jzyunqi.common.third.baidu.nlp.wenxin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.model.Text2ImgData;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.model.Text2ImgParam;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.model.Text2ImgRsp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@BaiduHttpExchange
@HttpExchange(url = "https://aip.baidubce.com", accept = {"application/json"}, contentType = "application/json")
public interface BaiduNLPWenxinApiProxy {

    //AI作画 - 基础版 - 请求绘画
    @PostExchange(url = "/rpc/2.0/wenxin/v1/basic/textToImage")
    Text2ImgRsp text2Image(@RequestParam String access_token, @RequestBody Text2ImgParam request) throws BusinessException;


    //AI作画 - 基础版 - 查询结果
    @PostExchange(url = "/rpc/2.0/wenxin/v1/basic/getImg")
    Text2ImgRsp getImg(@RequestParam String access_token, @RequestBody Text2ImgData request) throws BusinessException;
}
