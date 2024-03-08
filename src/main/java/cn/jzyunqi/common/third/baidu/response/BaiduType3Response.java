package cn.jzyunqi.common.third.baidu.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @date 2018/12/10.
 */
@Getter
@Setter
public class BaiduType3Response<T> {

	/**
	 * 返回结果状态值， 成功返回0
	 */
	private Integer status;

	/**
	 * 返回结果说明
	 */
	private String message;

	/**
	 * 返回结果
	 */
	private T result;
}
