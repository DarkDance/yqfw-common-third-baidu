package cn.jzyunqi.common.third.baidu.map.address.model;

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
 * @since 2018/12/11.
 */
@Getter
@Setter
@NoArgsConstructor
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 2279401105657056035L;

    /**
     * 经度值
     */
    private String lng;

    /**
     * 纬度值
     */
    private String lat;

    /**
     * 经度值
     */
    private String x;

    /**
     * 纬度值
     */
    private String y;

    public Location(String lng, String lat) {
        this.lng = lng;
        this.lat = lat;
        this.x = lng;
        this.y = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
        this.x = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
        this.y = lat;
    }

    public void setX(String x) {
        setLng(x);
    }

    public void setY(String y) {
        setLat(y);
    }
}
