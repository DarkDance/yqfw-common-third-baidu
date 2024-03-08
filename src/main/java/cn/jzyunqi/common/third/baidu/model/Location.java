package cn.jzyunqi.common.third.baidu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wiiyaya
 * @date 2018/12/11.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {
	@Serial
	private static final long serialVersionUID = 2279401105657056035L;

	/**
	 * 纬度值
	 */
	private BigDecimal lat;

	/**
	 * 经度值
	 */
	@JsonProperty("lng")
	private BigDecimal lan;
}
