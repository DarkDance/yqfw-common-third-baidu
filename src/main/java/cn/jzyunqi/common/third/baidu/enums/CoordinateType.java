package cn.jzyunqi.common.third.baidu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2018/12/10.
 */
@Getter
@AllArgsConstructor
public enum CoordinateType {

	/**
	 * GPS经纬度坐标
	 */
	wgs84ll(1, "wgs84ll"),

	/**
	 * GPS米制坐标
	 */
	wgs84mc(2, "wgs84mc"),

	/**
	 * 国测局经纬度坐标，仅限中国
	 */
	gcj02ll(3, "gcj02ll"),

	/**
	 * 国测局米制坐标，仅限中国
	 */
	gcj02mc(4, "gcj02mc"),

	/**
	 * 百度经纬度坐标
	 */
	bd09ll(5, "bd09ll"),

	/**
	 * 百度米制坐标
	 */
	bd09mc(6, "bd09mc"),

	/**
	 * mapbar地图坐标
	 */
	mapbar(7, "mapbar"),

	/**
	 * 51地图坐标
	 */
	_51map(8, "51map"),
	;

	private final Integer index;

	private final String type;
}
