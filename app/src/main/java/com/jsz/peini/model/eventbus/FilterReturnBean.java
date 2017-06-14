package com.jsz.peini.model.eventbus;

/**
 * Created by th on 2017/1/3.
 */

public class FilterReturnBean {
    /**
     * sort	Int		排序方式
     * （1时间（默认1）2距离）
     * otherSex	Int		性别（不限不传1男2女）
     * otherLowAge	Int		年龄最低限（没有不传）
     * otherHignAge	Int		年龄最高限（没有不传）
     * otherLowheight	Int		身高最低限（没有不传）
     * otherHignheight	Int		身高最高限（没有不传）
     * isVideo	Int		是否视频验证（1或不传）
     * isIdcard	Int		是否身份认证（1或不传）
     * sellerType	Int		商家类别（全部为不传（101 美食102 唱歌103 电影104 运动健身105 休闲娱乐106 酒店107 丽人108 生活服务109 其他））
     * taskCity	Int		城市石家庄（130100）
     */
    private String sort;
    private String otherSex;
    private String otherLowAge;
    private String otherHignAge;
    private String otherLowheight;
    private String otherHignheight;
    private String isVideo;
    private String isIdcard;
    private String sellerType;
    private String taskCity;

    public FilterReturnBean(String sort, String otherSex, String otherLowAge, String otherHignAge, String otherLowheight, String otherHignheight, String isVideo, String isIdcard, String sellerType, String taskCity) {
        this.sort = sort;
        this.otherSex = otherSex;
        this.otherLowAge = otherLowAge;
        this.otherHignAge = otherHignAge;
        this.otherLowheight = otherLowheight;
        this.otherHignheight = otherHignheight;
        this.isVideo = isVideo;
        this.isIdcard = isIdcard;
        this.sellerType = sellerType;
        this.taskCity = taskCity;
    }

    public String getTaskCity() {
        return taskCity;
    }

    public void setTaskCity(String taskCity) {
        this.taskCity = taskCity;
    }

    public String getOtherSex() {
        return otherSex;
    }

    public void setOtherSex(String otherSex) {
        this.otherSex = otherSex;
    }

    public String getOtherLowAge() {
        return otherLowAge;
    }

    public void setOtherLowAge(String otherLowAge) {
        this.otherLowAge = otherLowAge;
    }

    public String getOtherHignAge() {
        return otherHignAge;
    }

    public void setOtherHignAge(String otherHignAge) {
        this.otherHignAge = otherHignAge;
    }

    public String getOtherLowheight() {
        return otherLowheight;
    }

    public void setOtherLowheight(String otherLowheight) {
        this.otherLowheight = otherLowheight;
    }

    public String getOtherHignheight() {
        return otherHignheight;
    }

    public void setOtherHignheight(String otherHignheight) {
        this.otherHignheight = otherHignheight;
    }

    public String getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(String isVideo) {
        this.isVideo = isVideo;
    }

    public String getIsIdcard() {
        return isIdcard;
    }

    public void setIsIdcard(String isIdcard) {
        this.isIdcard = isIdcard;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "FilterReturnBean{" +
                "sort='" + sort + '\'' +
                ", otherSex='" + otherSex + '\'' +
                ", otherLowAge='" + otherLowAge + '\'' +
                ", otherHignAge='" + otherHignAge + '\'' +
                ", otherLowheight='" + otherLowheight + '\'' +
                ", otherHignheight='" + otherHignheight + '\'' +
                ", isVideo='" + isVideo + '\'' +
                ", isIdcard='" + isIdcard + '\'' +
                ", sellerType='" + sellerType + '\'' +
                ", taskCity='" + taskCity + '\'' +
                '}';
    }
}
