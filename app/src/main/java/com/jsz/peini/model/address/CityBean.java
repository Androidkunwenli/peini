package com.jsz.peini.model.address;

import java.util.List;

/**
 * 城市的业务类
 */

public class CityBean {

    /**
     * provinceObject : [{"cityId":110100,"cityName":"北京市","id":35}]
     * provinceName : 北京
     * id : 1
     * provinceId : 110000
     */

    private String provinceName;
    private int id;
    private int provinceId;
    private List<ProvinceObjectBean> provinceObject;

    @Override
    public String toString() {
        return "CityBean{" +
                "provinceName='" + provinceName + '\'' +
                ", id=" + id +
                ", provinceId=" + provinceId +
                ", provinceObject=" + provinceObject +
                '}';
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public List<ProvinceObjectBean> getProvinceObject() {
        return provinceObject;
    }

    public void setProvinceObject(List<ProvinceObjectBean> provinceObject) {
        this.provinceObject = provinceObject;
    }

    public static class ProvinceObjectBean {
        /**
         * cityId : 110100
         * cityName : 北京市
         * id : 35
         */

        private int cityId;
        private String cityName;
        private int id;

        @Override
        public String toString() {
            return "ProvinceObjectBean{" +
                    "cityId=" + cityId +
                    ", cityName='" + cityName + '\'' +
                    ", id=" + id +
                    '}';
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
