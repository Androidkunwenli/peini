package com.jsz.peini.model.address;

import java.util.List;

/**
 * Created by th on 2016/12/9.
 */

public class AddressBean {


    private List<AreaCityBean> areaCity;

    public List<AreaCityBean> getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(List<AreaCityBean> areaCity) {
        this.areaCity = areaCity;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "areaCity=" + areaCity +
                '}';
    }

    public static class AreaCityBean {
        /**
         * provinceId : 110000
         * provinceName : 北京
         * provinceObject : [{"cityId":110100,"cityName":"北京市","cityObject":[{"countyId":110101,"countyName":"东城区"},{"countyId":110102,"countyName":"西城区"},{"countyId":110103,"countyName":"崇文区"},{"countyId":110104,"countyName":"宣武区"},{"countyId":110105,"countyName":"朝阳区"},{"countyId":110106,"countyName":"丰台区"},{"countyId":110107,"countyName":"石景山区"},{"countyId":110108,"countyName":"海淀区"},{"countyId":110109,"countyName":"门头沟区"},{"countyId":110111,"countyName":"房山区"},{"countyId":110112,"countyName":"通州区"},{"countyId":110113,"countyName":"顺义区"},{"countyId":110114,"countyName":"昌平区"},{"countyId":110115,"countyName":"大兴区"},{"countyId":110116,"countyName":"怀柔区"},{"countyId":110117,"countyName":"平谷区"},{"countyId":110118,"countyName":"密云区"},{"countyId":110119,"countyName":"延庆区"}]}]
         */

        private int provinceId;
        private String provinceName;
        private List<ProvinceObjectBean> provinceObject;

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public List<ProvinceObjectBean> getProvinceObject() {
            return provinceObject;
        }

        public void setProvinceObject(List<ProvinceObjectBean> provinceObject) {
            this.provinceObject = provinceObject;
        }

        @Override
        public String toString() {
            return "AreaCityBean{" +
                    "provinceId=" + provinceId +
                    ", provinceName='" + provinceName + '\'' +
                    ", provinceObject=" + provinceObject +
                    '}';
        }

        public static class ProvinceObjectBean {
            /**
             * cityId : 110100
             * cityName : 北京市
             * cityObject : [{"countyId":110101,"countyName":"东城区"},{"countyId":110102,"countyName":"西城区"},{"countyId":110103,"countyName":"崇文区"},{"countyId":110104,"countyName":"宣武区"},{"countyId":110105,"countyName":"朝阳区"},{"countyId":110106,"countyName":"丰台区"},{"countyId":110107,"countyName":"石景山区"},{"countyId":110108,"countyName":"海淀区"},{"countyId":110109,"countyName":"门头沟区"},{"countyId":110111,"countyName":"房山区"},{"countyId":110112,"countyName":"通州区"},{"countyId":110113,"countyName":"顺义区"},{"countyId":110114,"countyName":"昌平区"},{"countyId":110115,"countyName":"大兴区"},{"countyId":110116,"countyName":"怀柔区"},{"countyId":110117,"countyName":"平谷区"},{"countyId":110118,"countyName":"密云区"},{"countyId":110119,"countyName":"延庆区"}]
             */

            private int cityId;
            private String cityName;
            private List<CityObjectBean> cityObject;

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

            public List<CityObjectBean> getCityObject() {
                return cityObject;
            }

            public void setCityObject(List<CityObjectBean> cityObject) {
                this.cityObject = cityObject;
            }

            @Override
            public String toString() {
                return "ProvinceObjectBean{" +
                        "cityId=" + cityId +
                        ", cityName='" + cityName + '\'' +
                        ", cityObject=" + cityObject +
                        '}';
            }

            public static class CityObjectBean {
                /**
                 * countyId : 110101
                 * countyName : 东城区
                 */

                private int countyId;
                private String countyName;

                public int getCountyId() {
                    return countyId;
                }

                public void setCountyId(int countyId) {
                    this.countyId = countyId;
                }

                public String getCountyName() {
                    return countyName;
                }

                public void setCountyName(String countyName) {
                    this.countyName = countyName;
                }

                @Override
                public String toString() {
                    return "CityObjectBean{" +
                            "countyId=" + countyId +
                            ", countyName='" + countyName + '\'' +
                            '}';
                }
            }
        }
    }
}
