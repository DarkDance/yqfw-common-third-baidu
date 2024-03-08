package cn.jzyunqi.common.third.baidu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/12/11.
 */
@Getter
@Setter
public class LonLatAddress implements Serializable {
	@Serial
	private static final long serialVersionUID = 9140958388974362525L;

	/**
	 * 经纬度坐标
	 */
	private Location location;

	/**
	 * 结构化地址信息
	 */
	@JsonProperty("formatted_address")
	private String formattedAddress;

	/**
	 * 坐标所在商圈信息，如 "人民大学,中关村,苏州街"。最多返回3个。
	 */
	private String business;

	/**
	 * 地址
	 */
	private AddressComponent addressComponent;

	/**
	 * 周边poi
	 */
	private List<Poi> pois;

	/**
	 * 周边路口
	 */
	private List<Object> roads;

	/**
	 * 当前Poi归属
	 */
	private List<PoiRegion> poiRegions;

	/**
	 * 当前位置结合POI的语义化结果描述。
	 */
	@JsonProperty("sematic_description")
	private String sematicDescription;

	@Getter
	@Setter
	public static class Poi implements Serializable {
		private static final long serialVersionUID = 6852903704148807877L;

		/**
		 * 地址信息
		 */
		private String addr;

		/**
		 * 和当前坐标点的方向
		 */
		private String direction;

		/**
		 * 离坐标点距离
		 */
		private Integer distance;

		/**
		 * poi名称
		 */
		private String name;

		/**
		 * poi类型，如’ 办公大厦,商务大厦’
		 */
		private String poiType;

		/**
		 * poi坐标{x,y}
		 */
		private String point;

		/**
		 * 电话
		 */
		private Integer tel;

		/**
		 * poi唯一标识
		 */
		private String uid;

		/**
		 * 邮编
		 */
		private Integer zip;

		/**
		 * poi对应的主点poi（如，海底捞的主点为上地华联，该字段则为上地华联的poi信息。如无，该字段为空），包含子字段和pois基础召回字段相同。
		 */
		@JsonProperty("parent_poi")
		private Poi parentPoi;

	}

	@Getter
	@Setter
	public static class PoiRegion implements Serializable {
		private static final long serialVersionUID = -4366159185112421959L;

		/**
		 * 请求中的坐标与所归属区域面的相对位置关系
		 */
		@JsonProperty("direction_desc")
		private String directionDesc;

		/**
		 * 归属区域面名称
		 */
		private String name;

		/**
		 * 归属区域面类型
		 */
		private String tag;
	}

	@Getter
	@Setter
	public static class AddressComponent implements Serializable {
		private static final long serialVersionUID = -2242080385202148866L;

		/**
		 * 国家
		 */
		private String country;

		/**
		 * 省名
		 */
		private String province;

		/**
		 * 城市名
		 */
		private String city;

		/**
		 * 区县名
		 */
		private String district;

		/**
		 * 乡镇名
		 */
		private String town;

		/**
		 * 街道名（行政区划中的街道层级）
		 */
		private String street;

		/**
		 * 街道门牌号
		 */
		@JsonProperty("street_number")
		private String streetNumber;

		/**
		 * 行政区划代码
		 */
		@JsonProperty("adcode")
		private Integer adCode;

		/**
		 * 国家代码
		 */
		@JsonProperty("country_code")
		private Integer countryCode;

		/**
		 * 相对当前坐标点的方向，当有门牌号的时候返回数据
		 */
		private String direction;

		/**
		 * 相对当前坐标点的距离，当有门牌号的时候返回数据
		 */
		private String distance;

	}
}
