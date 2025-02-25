package cn.jzyunqi.common.third.baidu.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2020/9/21.
 */
@Getter
@Setter
@ToString
public class BaiduRspV1 {

    /**
     * 返回结果状态值， 成功返回0
     */
    @JsonProperty("error_code")
    private Integer errorCode;

    /**
     * 返回结果说明
     */
    @JsonProperty("error_msg")
    private String errorMsg;

    /**
     * 唯一请求 ID，用于问题排查
     */
    @JsonProperty("log_id")
    private String logId;
}
