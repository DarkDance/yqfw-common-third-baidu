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

    /**
     * 检索结果数
     */
    @JsonProperty("result_num")
    private Integer resultNum;

    /**
     * 是否还有下一页，返回值：true、false；如果不分页，不用关注该字段
     */
    @JsonProperty("has_more")
    private Boolean hasMore;

    /**
     * 结果数组
     */
    private List<ProductData> result;
}
