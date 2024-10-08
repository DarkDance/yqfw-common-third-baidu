package cn.jzyunqi.common.third.baidu.image.ai;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgData;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgDataV2;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgParam;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgParamV2;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgRsp;
import cn.jzyunqi.common.third.baidu.image.ai.model.Text2ImgRspV2;
import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange;
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
public interface BaiduAiImgApiProxy {

    //AI作画 - 基础版 - 请求绘画
    @PostExchange(url = "/rpc/2.0/wenxin/v1/basic/textToImage")
    Text2ImgRsp text2Image(@RequestParam String access_token, @RequestBody Text2ImgParam request) throws BusinessException;

    //AI作画 - 基础版 - 查询结果
    @PostExchange(url = "/rpc/2.0/wenxin/v1/basic/getImg")
    Text2ImgRsp getImg(@RequestParam String access_token, @RequestBody Text2ImgData request) throws BusinessException;

    //AI作画 - 极速版 - 请求绘画
    @PostExchange(url = "/rpc/2.0/wenxin/v1/extreme/textToImage")
    Text2ImgRspV2 text2ImageEx(@RequestParam String access_token, @RequestBody Text2ImgParamV2 request) throws BusinessException;

    //AI作画 - 极速版 - 查询结果
    @PostExchange(url = "/rpc/2.0/wenxin/v1/extreme/getImg")
    Text2ImgRspV2 getImgV2Ex(@RequestParam String access_token, @RequestBody Text2ImgDataV2 request) throws BusinessException;

    //AI作画 - 高级版 - 请求绘画
    @PostExchange(url = "/rpc/2.0/ernievilg/v1/txt2imgv2")
    Text2ImgRspV2 text2ImageV2(@RequestParam String access_token, @RequestBody Text2ImgParamV2 request) throws BusinessException;

    //AI作画 - 高级版 - 查询结果
    @PostExchange(url = "/rpc/2.0/ernievilg/v1/getImgv2")
    Text2ImgRspV2 getImgV2(@RequestParam String access_token, @RequestBody Text2ImgDataV2 request) throws BusinessException;
}
