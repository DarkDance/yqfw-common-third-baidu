package cn.jzyunqi.common.third.baidu.common;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import cn.jzyunqi.common.third.baidu.common.model.ClientTokenData;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@BaiduHttpExchange
@HttpExchange(url = "https://aip.baidubce.com", accept = {"application/json"})
public interface BaiduTokenApiProxy {

    //获取 Access_token
    @PostExchange(url = "/oauth/2.0/token?grant_type=client_credentials")
    ClientTokenData getClientToken(@RequestParam String client_id, @RequestParam String client_secret) throws BusinessException;

}
