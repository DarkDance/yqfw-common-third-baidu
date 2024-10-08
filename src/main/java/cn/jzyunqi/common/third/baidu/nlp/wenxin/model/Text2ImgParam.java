package cn.jzyunqi.common.third.baidu.nlp.wenxin.model;

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
public class Text2ImgParam {

    /**
     * 输入内容，长度不超过190个字,「公式」= 图片主体，细节词，修饰词
     */
    private String text;

    /**
     * 图片分辨率，可支持512*512、640*360、360*640、1024*1024、720*1280、1280*720
     */
    private String resolution;

    /**
     * 目前支持风格有：二次元、写实风格、古风、赛博朋克、水彩画、油画、卡通画，更多风格可自行探索
     */
    private String style;

    /**
     * 图片生成数量，支持1-6张
     */
    private Integer num;

    /**
     * 水印内容
     */
    @JsonProperty("text_content")
    private String textContent;

    /**
     * 模型侧的提示词检测开关，仅支持输入0和1。0：关闭，1：开启（默认值）。开启时，如果提示词未通过模型侧提示词检测则子任务的sub_task_error_code会返回501，无法生成图片。
     */
    private Integer textCheck;
}
