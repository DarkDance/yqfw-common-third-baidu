package cn.jzyunqi.common.third.baidu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2020/9/21.
 */
@Getter
@Setter
public class BaiduType2Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -2025511973895963563L;

    /**
     * 错误代码
     */
    private String error;

    /**
     * 错误信息
     */
    @JsonProperty("error_description")
    private String errorDescription;
}
