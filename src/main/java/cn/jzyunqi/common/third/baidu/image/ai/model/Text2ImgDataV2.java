package cn.jzyunqi.common.third.baidu.image.ai.model;

import cn.jzyunqi.common.third.baidu.image.ai.enums.TaskStatus;
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

    @JsonProperty("task_status")
    private TaskStatus taskStatus;

    @JsonProperty("task_progress_detail")
    private Float taskProgressDetail;

    @JsonProperty("task_progress")
    private Boolean taskProgress;

    @JsonProperty("sub_task_result_list")
    private List<SubTaskResult> subTaskResultList;

    @Getter
    @Setter
    @ToString
    public static class SubTaskResult {
        @JsonProperty("sub_task_status")
        private TaskStatus subTaskStatus;

        @JsonProperty("sub_task_progress_detail")
        private Float subTaskProgressDetail;

        @JsonProperty("sub_task_progress")
        private Boolean subTaskProgress;

        @JsonProperty("sub_task_error_code")
        private String subTaskErrorCode;

        @JsonProperty("final_image_list")
        private List<ImgData> finalImageList;
    }

    @Getter
    @Setter
    @ToString
    public static class ImgData {
        @JsonProperty("img_url")
        private String imgUrl;

        private String height;

        private String width;

        @JsonProperty("img_approve_conclusion")
        private String imgApproveConclusion;
    }
}
