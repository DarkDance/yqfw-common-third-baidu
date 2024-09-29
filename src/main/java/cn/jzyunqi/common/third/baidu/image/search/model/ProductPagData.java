package cn.jzyunqi.common.third.baidu.image.search.model;

import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/29
 */
@Getter
@Setter
@ToString
public class ProductPagData extends BaiduRspV1 {

    @JsonProperty("result_num")
    private Integer resultNum;

    @JsonProperty("has_more")
    private Boolean hasMore;
    private List<ProductData> result;
}
