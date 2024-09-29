package cn.jzyunqi.common.third.baidu.image.search;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import cn.jzyunqi.common.third.baidu.image.search.model.ProductData;
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
@HttpExchange(url = "https://aip.baidubce.com", accept = {"application/json"}, contentType = "application/x-www-form-urlencoded")
public interface BaiduImgClassifyApiProxy {

    //图像搜索 - 商品图片搜索 - 入库
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/add")
    ProductData productAdd(@RequestParam String access_token, @RequestParam(required = false) String image, @RequestParam(required = false) String url, @RequestParam String brief, @RequestParam(required = false) String class_id1, @RequestParam(required = false) String class_id2) throws BusinessException;

    //图像搜索 - 商品图片搜索 - 检索
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/search")
    ProductPagData productSearch(@RequestParam String access_token, @RequestParam(required = false) String image, @RequestParam(required = false) String url, @RequestParam(required = false) String class_id1, @RequestParam(required = false) String class_id2, @RequestParam(required = false) String tag_logic, @RequestParam String pn, @RequestParam String rn) throws BusinessException;

    //图像搜索 - 商品图片搜索 - 删除
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/delete")
    BaiduRspV1 productDelete(@RequestParam String access_token, @RequestParam(required = false) String image, @RequestParam(required = false) String url, @RequestParam(required = false) String cont_sign) throws BusinessException;

    //图像搜索 - 商品图片搜索 - 更新
    @PostExchange(url = "/rest/2.0/image-classify/v1/realtime_search/product/update")
    BaiduRspV1 productUpdate(@RequestParam String access_token, @RequestParam(required = false) String image, @RequestParam(required = false) String url, @RequestParam(required = false) String cont_sign, @RequestParam(required = false) String brief, @RequestParam(required = false) String class_id1, @RequestParam(required = false) String class_id2) throws BusinessException;

}
