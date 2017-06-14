package com.jsz.peini.model;

import java.util.List;

/**
 * Created by th on 2016/12/17.
 */

public class CrtyBean {


    private int resultCode;
    private String resultDesc;
    private List<AreaCityBean> areaCity;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public List<AreaCityBean> getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(List<AreaCityBean> areaCity) {
        this.areaCity = areaCity;
    }

    @Override
    public String toString() {
        return "CrtyBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", areaCity=" + areaCity +
                '}';
    }

    public static class AreaCityBean {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
            private String id;
            private int cityId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

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
