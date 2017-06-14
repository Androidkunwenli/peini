package com.jsz.peini.model.address;

import java.util.List;

/**
 * 个人信息相关的基类(星座,购车,等等等...)
 */

public class PersonalDataBean {

    private List<NationListBean> nationList;
    private List<ConstellationListBean> constellationList;
    private List<EmotionListBean> emotionList;
    private List<DegreeListBean> degreeList;
    private List<IndustryListBean> industryList;
    private List<HouseListBean> houseList;
    private List<CarListBean> carList;

    public List<NationListBean> getNationList() {
        return nationList;
    }

    public void setNationList(List<NationListBean> nationList) {
        this.nationList = nationList;
    }

    public List<ConstellationListBean> getConstellationList() {
        return constellationList;
    }

    public void setConstellationList(List<ConstellationListBean> constellationList) {
        this.constellationList = constellationList;
    }

    public List<EmotionListBean> getEmotionList() {
        return emotionList;
    }

    public void setEmotionList(List<EmotionListBean> emotionList) {
        this.emotionList = emotionList;
    }

    public List<DegreeListBean> getDegreeList() {
        return degreeList;
    }

    public void setDegreeList(List<DegreeListBean> degreeList) {
        this.degreeList = degreeList;
    }

    public List<IndustryListBean> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<IndustryListBean> industryList) {
        this.industryList = industryList;
    }

    public List<HouseListBean> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<HouseListBean> houseList) {
        this.houseList = houseList;
    }

    public List<CarListBean> getCarList() {
        return carList;
    }

    public void setCarList(List<CarListBean> carList) {
        this.carList = carList;
    }

    public static class NationListBean {
        /**
         * codes : 101
         * codesNum : 1
         * codesName : 汉族
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }

    public static class ConstellationListBean {
        /**
         * codes : 103
         * codesNum : 1
         * codesName : 白羊座
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }

    public static class EmotionListBean {
        /**
         * codes : 104
         * codesNum : 1
         * codesName : 单身
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }

    public static class DegreeListBean {
        /**
         * codes : 105
         * codesNum : 1
         * codesName : 小学
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }

    public static class IndustryListBean {
        /**
         * codes : 106
         * codesNum : 1
         * codesName : 计算机/互联网/通信
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }

    public static class HouseListBean {
        /**
         * codes : 107
         * codesNum : 1
         * codesName : 已购房
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }

    public static class CarListBean {
        /**
         * codes : 108
         * codesNum : 1
         * codesName : 已购车
         */

        private int codes;
        private int codesNum;
        private String codesName;

        public int getCodes() {
            return codes;
        }

        public void setCodes(int codes) {
            this.codes = codes;
        }

        public int getCodesNum() {
            return codesNum;
        }

        public void setCodesNum(int codesNum) {
            this.codesNum = codesNum;
        }

        public String getCodesName() {
            return codesName;
        }

        public void setCodesName(String codesName) {
            this.codesName = codesName;
        }
    }
}
