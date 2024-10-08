package cn.jzyunqi.common.third.baidu.image.ai.model;

import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@Getter
@Setter
@ToString
public class Text2ImgRspV2 extends BaiduRspV1 {

    private Text2ImgDataV2 data;
}
