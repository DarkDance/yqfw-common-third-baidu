package cn.jzyunqi.common.third.baidu.common;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV2;
import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV3;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Slf4j
@Aspect
@Order
public class BaiduHttpExchangeWrapper {

    /**
     * 所有标记了@BaiduHttpExchange的类下所有的方法
     */
    @Pointcut("within(@cn.jzyunqi.common.third.baidu.common.BaiduHttpExchange *)")
    public void baiduHttpExchange() {
    }

    @Around(value = "baiduHttpExchange() ", argNames = "proceedingJoinPoint")
    public Object Around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("======baiduHttpExchange start=======");
        Object resultObj;
        try {
            resultObj = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.debug("======baiduHttpExchange proceed throw exception=======");
            throw new BusinessException(e, "common_error_baidu_http_exchange_error");
        }
        log.debug("======baiduHttpExchange proceed success=======");
        if (resultObj instanceof BaiduRspV1 baiduRspV1) {
            if (baiduRspV1.getErrorCode() != null && baiduRspV1.getErrorCode() != 0) {
                throw new BusinessException("common_error_baidu_http_exchange_failed", baiduRspV1.getErrorCode(), baiduRspV1.getErrorMsg());
            }
        } else if (resultObj instanceof BaiduRspV2 baiduRspV2) {
            if (StringUtilPlus.isNotBlank(baiduRspV2.getError()) && !"0".equals(baiduRspV2.getError())) {
                throw new BusinessException("common_error_baidu_http_exchange_failed", baiduRspV2.getError(), baiduRspV2.getErrorDescription());
            }
        } else if (resultObj instanceof BaiduRspV3<?> baiduRspV3) {
            if (baiduRspV3.getStatus() != null && baiduRspV3.getStatus() != 0) {
                throw new BusinessException("common_error_baidu_http_exchange_failed", baiduRspV3.getStatus(), baiduRspV3.getMessage());
            }
        }
        log.debug("======baiduHttpExchange end=======");
        return resultObj;
    }
}
