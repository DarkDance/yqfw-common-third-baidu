package cn.jzyunqi.common.third.baidu.image;

import cn.jzyunqi.common.third.baidu.common.BaiduHttpExchangeWrapper;
import cn.jzyunqi.common.third.baidu.common.BaiduTokenApiProxy;
import cn.jzyunqi.common.third.baidu.image.search.BaiduImgClassifyApiProxy;
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
public class BaiduImgConfig {

    @Bean
    @ConditionalOnMissingBean
    public BaiduHttpExchangeWrapper baiduHttpExchangeWrapper() {
        return new BaiduHttpExchangeWrapper();
    }

    @Bean
    public BaiduImgClient baiduImgClient() {
        return new BaiduImgClient();
    }

    @Bean
    public BaiduTokenApiProxy baiduTokenApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(BaiduTokenApiProxy.class);
    }

    @Bean
    public BaiduImgClassifyApiProxy baiduImgClassifyApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(BaiduImgClassifyApiProxy.class);
    }
}
