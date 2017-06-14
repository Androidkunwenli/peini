package com.jsz.peini.model.seller;

/**
 * Created by lenovo on 2017/3/28.
 */
public class SellerInfoBean {
    /**
     * id : 137
     * sellerName : 好声音
     * sellerScore : 0
     * districtCode : 5
     * labels_id : 24
     * price : 20
     * sellerLogo : /upload/business/sellerInfo/好声音/2017/2/28/6e181d7f2bfe4914b7b006a99239f5c7.jpg
     * imageSrc : /upload/business/sellerInfo/好声音/2017/2/28/e72250bc52b641b0b221beed14ef5c0b.jpg
     * districtName : {"id":5,"districtCode":130102,"districtName":"长安区","placeCode":13010205,"plcaeName":"省博物馆","searchCount":6}
     * distance : 206
     * labels_name : Ktv
     * countJB :
     */

    private int id;
    private String sellerName;
    private int sellerScore;
    private String districtCode;
    private String labels_id;
    private String price;
    private String sellerLogo;
    private String imageSrc;
    private String districtName;
    private int distance;
    private String labels_name;
    private String countJB;
    private String countMJ;
    private String weatherOrder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getSellerScore() {
        return sellerScore;
    }

    public void setSellerScore(int sellerScore) {
        this.sellerScore = sellerScore;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getLabels_id() {
        return labels_id;
    }

    public void setLabels_id(String labels_id) {
        this.labels_id = labels_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellerLogo() {
        return sellerLogo;
    }

    public void setSellerLogo(String sellerLogo) {
        this.sellerLogo = sellerLogo;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getLabels_name() {
        return labels_name;
    }

    public void setLabels_name(String labels_name) {
        this.labels_name = labels_name;
    }

    public String getCountJB() {
        return countJB;
    }

    public void setCountJB(String countJB) {
        this.countJB = countJB;
    }

    public String getCountMJ() {
        return countMJ;
    }

    public void setCountMJ(String countMJ) {
        this.countMJ = countMJ;
    }

    public String getWeatherOrder() {
        return weatherOrder;
    }

    public void setWeatherOrder(String weatherOrder) {
        this.weatherOrder = weatherOrder;
    }

    @Override
    public String toString() {
        return "SellerInfoBean{" +
                "id=" + id +
                ", sellerName='" + sellerName + '\'' +
                ", sellerScore=" + sellerScore +
                ", districtCode='" + districtCode + '\'' +
                ", labels_id='" + labels_id + '\'' +
                ", price='" + price + '\'' +
                ", sellerLogo='" + sellerLogo + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", districtName='" + districtName + '\'' +
                ", distance=" + distance +
                ", labels_name='" + labels_name + '\'' +
                ", countJB='" + countJB + '\'' +
                ", countMJ='" + countMJ + '\'' +
                '}';
    }
}
