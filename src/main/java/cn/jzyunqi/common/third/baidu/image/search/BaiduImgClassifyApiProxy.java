package cn.jzyunqi.common.third.baidu.image.search;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import cn.jzyunqi.common.third.baidu.image.search.model.ProductPagData;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@BaiduHttpExchange
@HttpExchange(url = "https://aip.baidubce.com", accept = {"application/json"})
public interface BaiduImgClassifyApiProxy {

    //图像搜索 - 商品图片搜索 - 入库
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/add")
    BaiduRspV1 productAdd(@RequestHeader String access_token, @RequestParam String image, @RequestParam String url, @RequestParam String brief, @RequestParam String class_id1, @RequestParam String class_id2) throws BusinessException;

    //图像搜索 - 商品图片搜索 - 检索
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/search")
    ProductPagData productSearch(@RequestHeader String access_token, @RequestParam String image, @RequestParam String url, @RequestParam String class_id1, @RequestParam String class_id2, @RequestParam String tag_logic, @RequestParam String pn, @RequestParam String rn) throws BusinessException;

    //图像搜索 - 商品图片搜索 - 删除
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/delete")
    BaiduRspV1 productDelete(@RequestHeader String access_token, @RequestParam String image, @RequestParam String url, @RequestParam String cont_sign) throws BusinessException;

    //图像搜索 - 商品图片搜索 - 更新
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/update")
    BaiduRspV1 productUpdate(@RequestHeader String access_token, @RequestParam String image, @RequestParam String url, @RequestParam String cont_sign, @RequestParam String brief, @RequestParam String class_id1, @RequestParam String class_id2) throws BusinessException;

}
