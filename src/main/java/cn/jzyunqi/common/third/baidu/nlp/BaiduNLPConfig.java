package cn.jzyunqi.common.third.baidu.nlp;

import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchangeWrapper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.image.BaiduImgClient;
import cn.jzyunqi.common.third.baidu.image.ocr.BaiduImgOcrApiProxy;
import cn.jzyunqi.common.third.baidu.image.search.BaiduImgSearchApiProxy;
import cn.jzyunqi.common.third.baidu.nlp.wenxin.BaiduNLPWenxinApiProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Configuration
public class BaiduNLPConfig {

    @Bean
    @ConditionalOnMissingBean
    public BaiduHttpExchangeWrapper baiduHttpExchangeWrapper() {
        return new BaiduHttpExchangeWrapper();
    }

    @Bean
    public BaiduNLPClient baiduNLPClient() {
        return new BaiduNLPClient();
    }

    @Bean
    public BaiduNLPWenxinApiProxy baiduNLPWenxinApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(BaiduNLPWenxinApiProxy.class);
    }
}
