package cn.jzyunqi.common.third.baidu.map;

import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchangeWrapper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.image.BaiduImgClient;
import cn.jzyunqi.common.third.baidu.map.address.BaiduMapAddressApiProxy;
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
public class BaiduMapConfig {

    @Bean
    @ConditionalOnMissingBean
    public BaiduHttpExchangeWrapper baiduHttpExchangeWrapper() {
        return new BaiduHttpExchangeWrapper();
    }

    @Bean
    public BaiduMapClient baiduMapClient() {
        return new BaiduMapClient();
    }

    @Bean
    public BaiduMapAddressApiProxy baiduMapAddressApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(BaiduMapAddressApiProxy.class);
    }
}
