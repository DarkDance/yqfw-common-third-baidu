package cn.jzyunqi.common.third.baidu.nlp.wenxin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@Getter
@Setter
@ToString
public class Text2ImgData {

    /**
     * 图片生成任务id，作为查询接口的入参
     */
    private Long taskId;

    /**
     * 生成图片任务string类型 id，与“taskId”参数输出相同，该 id 可用于查询任务状态
     */
    private String primaryTaskId;

    /**
     * 请求内容中的图片风格
     */
    private String style;

    /**
     * 请求内容中的文本
     */
    private String text;

    /**
     * 0或1。"1"表示已生成完成，"0"表示任务排队中或正在处理。
     */
    private String status;

    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;

    /**
     * 生成结果地址
     */
    private String img;

    /**
     * 生成结果数组（目前默认生成1张图）
     */
    private List<ImgData> imgUrls;

    /**
     * 预计等待时间（仅供参考）
     */
    private String waiting;

    @Getter
    @Setter
    @ToString
    public static class ImgData {

        /**
         * 生成结果地址（有效期30天）
         */
        private String image;

        @JsonProperty("image_approve_conclusion")
        private String imgApproveConclusion;
    }
}
