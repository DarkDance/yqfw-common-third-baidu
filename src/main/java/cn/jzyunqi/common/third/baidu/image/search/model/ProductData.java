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

    /**
     * 上传图片的签名信息，请务必保存至本地，以便后续用作批量删除、查询某张图是否已经入过库等用途
     */
    @JsonProperty("cont_sign")
    private String contSign;

    /**
     * 检索时原样带回,最长256B。样例{"name":"周杰伦", "id":"666"}
     */
    private String brief;

    /**
     * 图片相关性，取值范围0-1，越接近1代表越相似
     */
    private Double score;
}
