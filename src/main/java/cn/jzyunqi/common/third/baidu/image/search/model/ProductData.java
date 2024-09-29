package cn.jzyunqi.common.third.baidu.image.search.model;

import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Getter
@Setter
@ToString
public class ProductData extends BaiduRspV1 {

    @JsonProperty("cont_sign")
    private String contSign;

    private String brief;

    private Double score;
}
