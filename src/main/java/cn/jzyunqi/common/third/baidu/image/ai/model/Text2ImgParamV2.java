package cn.jzyunqi.common.third.baidu.image.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Text2ImgParamV2 {

    /**
     * 生图的文本描述。仅支持中文、日常标点符号（~！@#$%&*()-+[];:'',./）。不支持英文，特殊符号，限制 200 字
     */
    private String prompt;

    /**
     * 支持：512x512、640x360、360x640、1024x1024、1280x720、720x1280、2048x2048、2560x1440、1440x2560、3840x2160、2160x3840
     */
    private Integer width;
    private Integer height;

    /**
     * 生成图片数量，默认一张，支持生成 1-8 张
     */
    @JsonProperty("image_num")
    private Integer imageNum;

    /**
     * 参考图，需 base64 编码，大小不超过 10M，最短边至少 15px，最长边最大 8192px，支持jpg/jpeg/png/bmp 格式。
     */
    private String image;

    /**
     * 参考图完整 url，url 长度不超过 1024 字节，url 对应的图片需 base64 编码，大小不超过 10M，最短边至少 15px，最长边最大8192px，支持 jpg/jpeg/png/bmp 格式。
     */
    private String url;

    /**
     * 参考图 PDF 文件，base64 编码，大小不超过10M，最短边至少 15px，最长边最大 8192px 。
     */
    @JsonProperty("pdf_file")
    private String pdfFile;

    /**
     * 需要识别的 PDF 文件的对应页码，默认识别第 1 页
     */
    @JsonProperty("pdf_file_num")
    private String pdfFileNum;

    /**
     * 参考图影响因子，支持 1-10 内；数值越大参考图影响越大
     */
    @JsonProperty("change_degree")
    private Integer changeDegree;

    /**
     * 水印文字
     */
    @JsonProperty("text_content")
    private String textContent;

    /**
     * 超时时间（单位：s）
     */
    @JsonProperty("task_time_out")
    private Integer taskTimeOut;

    /**
     * 模型侧的提示词检测开关，默认值1为开启状态
     */
    @JsonProperty("text_check")
    private Integer textCheck;

}
