package cn.jzyunqi.common.third.baidu.image.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/10/8
 */
@Getter
@Setter
@ToString
public class Text2ImgDataV2 {

    @JsonProperty("primary_task_id")
    private Long primaryTaskId;

    @JsonProperty("task_id")
    private String taskId;

    private String task_status;
    private Float task_progress_detail;
    private Boolean task_progress;

    private List<SubTaskResult> sub_task_result_list;

    @Getter
    @Setter
    @ToString
    public static class SubTaskResult {
        private String sub_task_status;
        private Float sub_task_progress_detail;
        private Boolean sub_task_progress;
        private String sub_task_error_code;
        private List<ImgData> final_image_list;
    }

    @Getter
    @Setter
    @ToString
    public static class ImgData {
        private String img_url;
        private String height;
        private String width;
        private String img_approve_conclusion;
    }
}
