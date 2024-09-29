package cn.jzyunqi.common.third.baidu.image.ocr.model;

import cn.jzyunqi.common.third.baidu.common.model.BaiduRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @date 2020/9/21.
 */
@Getter
@Setter
public class WordData extends BaiduRspV1 {

    /**
     * 图像方向，当detect_direction=true时存在
     */
    private Integer direction;

    /**
     * 段落检测数
     */
    @JsonProperty("paragraphs_result_num")
    private Integer paragraphCount;

    /**
     * 识别结果数
     */
    @JsonProperty("words_result_num")
    private Integer wordCount;

    /**
     * 识别结果数组
     */
    @JsonProperty("words_result")
    private List<WordsResult> wordsResult;

    /**
     * 段落检测结果，当 paragraph=true 时返回该字段
     */
    @JsonProperty("paragraphs_result")
    private List<ParagraphsResult> paragraphsResult;

    @Getter
    @Setter
    public static class WordsResult {

        /**
         * 识别结果字符串
         */
        private String words;

        /**
         * 识别结果字符串
         */
        private List<Chars> chars;

        /**
         * 每一行的置信度值
         */
        private Probability probability;

        /**
         * 位置
         */
        private DivBox location;

        /**
         * 顶点位置
         */
        @JsonProperty("vertexes_location")
        private List<Point> vPoint;

        /**
         * 友好的顶点位置
         */
        @JsonProperty("finegrained_vertexes_location")
        private List<Point> fvPoint;

        /**
         * 最小顶点位置
         */
        @JsonProperty("min_finegrained_vertexes_location")
        private List<Point> minFvPoint;

    }

    @Getter
    @Setter
    public static class Chars {

        @JsonProperty("char")
        private String character;
        private DivBox location;
    }

    @Getter
    @Setter
    public static class DivBox {

        private Integer width;
        private Integer height;
        private Integer top;
        private Integer left;
    }

    @Getter
    @Setter
    public static class Point {

        private Integer x;
        private Integer y;
    }

    @Getter
    @Setter
    public static class Probability {

        /**
         * 行置信度方差
         */
        private Double variance;

        /**
         * 行置信度平均值
         */
        private Double average;

        /**
         * 行置信度最小值
         */
        private Double min;
    }

    @Getter
    @Setter
    public static class ParagraphsResult {

        /**
         * 一个段落包含的行序号，当 paragraph=true 时返回该字段
         */
        @JsonProperty("words_result_idx")
        private List<Integer> wordsResultIdx;

        /**
         * 友好的顶点位置
         */
        @JsonProperty("finegrained_vertexes_location")
        private List<Point> fvPoint;

        /**
         * 最小顶点位置
         */
        @JsonProperty("min_finegrained_vertexes_location")
        private List<Point> minFvPoint;
    }
}
