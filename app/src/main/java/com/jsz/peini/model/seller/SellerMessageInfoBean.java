package com.jsz.peini.model.seller;

import java.util.List;

/**
 * Created by th on 2017/2/9.
 */
public class SellerMessageInfoBean {

    /**
     * sellerInfo : {"couponJb":"","couponMj":"","distance":1946,"districtName":"","id":1,"imageList":[],"imageSrc":"/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/4daeff5586ff4ef8a4725ea83063d621.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/ca6b087dbf344da0b5a0499b63cb7463.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/69b5509a78a148d7a1d361569491e5be.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/1c4b9322d9e04b22841e550145deccb6.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/2b67a6bcd47a4b5abb755df4897d8fac.jpg","information":"随着都市生活的节奏越来越快，繁忙的工作压得城市人有点喘不过气来，到了绿茶餐厅没有富丽堂皇的建筑，绿茶餐厅有的只是清新自然，纯朴的原木搭建，少数民族及异域风情的摆设，无不让人感到放松。享受绿茶的美食，放松紧蹦的神经，感受绿茶餐厅自然，无约束的氛围，释放美好心情，这正是绿绿茶餐厅受到越来越多的人喜欢并甘愿为此等候60分钟的因素之一!  注：酒水饮品不参与折扣范围内。","isParking":0,"isWifi":0,"price":35,"sellerAddress":"槐安东路99号","sellerCondition":0,"sellerHead":"/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/561b17c96e5849748f7bda71082ac642.jpg","sellerMeal":0,"sellerName":"绿茶风尚餐厅","sellerPhone":"18831189077","sellerScore":0,"sellerServer":0,"smsType":"","weatherorder":0,"xpoint":"38.028677","ypoint":"114.527152","sellerBigName":"美食","sellerTypeImg":"/upload/sellerTypeIcon/2017/3/2/f5588385e8114734bb7515653081262c.png","shareTitle":"我正在看陪你绿茶风尚餐厅店铺，口碑非常棒，分享给你>>>","opTimes":[{"weekNum":"周一至周五","opList":[{"oDesc":"早餐","oTime":"9:00-14:00"},{"oDesc":"晚餐","oTime":"17:00-23:00"}]},{"weekNum":"周六、周日","opList":[{"oDesc":"宵夜","oTime":"10:00-23:00"}]}],"shareContent":"地址在槐安东路99号，跟我一起GOGOGO~"}
     * resultCode : 1
     * resultDesc : 成功
     */

    private SellerInfoBean sellerInfo;
    private int resultCode;
    private String resultDesc;

    public SellerInfoBean getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(SellerInfoBean sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

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

    public static class SellerInfoBean {
        /**
         * couponJb :
         * couponMj :
         * distance : 1946
         * districtName :
         * id : 1
         * imageList : []
         * imageSrc : /upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/4daeff5586ff4ef8a4725ea83063d621.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/ca6b087dbf344da0b5a0499b63cb7463.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/69b5509a78a148d7a1d361569491e5be.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/1c4b9322d9e04b22841e550145deccb6.jpg,/upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/2b67a6bcd47a4b5abb755df4897d8fac.jpg
         * information : 随着都市生活的节奏越来越快，繁忙的工作压得城市人有点喘不过气来，到了绿茶餐厅没有富丽堂皇的建筑，绿茶餐厅有的只是清新自然，纯朴的原木搭建，少数民族及异域风情的摆设，无不让人感到放松。享受绿茶的美食，放松紧蹦的神经，感受绿茶餐厅自然，无约束的氛围，释放美好心情，这正是绿绿茶餐厅受到越来越多的人喜欢并甘愿为此等候60分钟的因素之一!  注：酒水饮品不参与折扣范围内。
         * isParking : 0
         * isWifi : 0
         * price : 35
         * sellerAddress : 槐安东路99号
         * sellerCondition : 0
         * sellerHead : /upload/business/sellerInfo/绿茶风尚餐厅/2017/3/6/561b17c96e5849748f7bda71082ac642.jpg
         * sellerMeal : 0
         * sellerName : 绿茶风尚餐厅
         * sellerPhone : 18831189077
         * sellerScore : 0
         * sellerServer : 0
         * smsType :
         * weatherorder : 0
         * xpoint : 38.028677
         * ypoint : 114.527152
         * sellerBigName : 美食
         * sellerTypeImg : /upload/sellerTypeIcon/2017/3/2/f5588385e8114734bb7515653081262c.png
         * shareTitle : 我正在看陪你绿茶风尚餐厅店铺，口碑非常棒，分享给你>>>
         * opTimes : [{"weekNum":"周一至周五","opList":[{"oDesc":"早餐","oTime":"9:00-14:00"},{"oDesc":"晚餐","oTime":"17:00-23:00"}]},{"weekNum":"周六、周日","opList":[{"oDesc":"宵夜","oTime":"10:00-23:00"}]}]
         * shareContent : 地址在槐安东路99号，跟我一起GOGOGO~
         */

        private String couponJb;
        private String couponMj;
        private int distance;
        private String districtName;
        private int id;
        private String imageSrc;
        private String information;
        private int isParking;
        private int isWifi;
        private int price;
        private String sellerAddress;
        private float sellerCondition;
        private String sellerHead;
        private float sellerMeal;
        private String sellerName;
        private String sellerPhone;
        private int sellerScore;
        private float sellerServer;
        private String smsType;
        private int weatherorder;
        private String xpoint;
        private String ypoint;
        private String sellerBigName;
        private String sellerTypeImg;
        private String shareTitle;
        private String shareContent;
        private List<?> imageList;
        private List<OpTimesBean> opTimes;
        private String remindtext;

        public void setSellerMeal(float sellerMeal) {
            this.sellerMeal = sellerMeal;
        }

        public String getRemindText() {
            return remindtext;
        }

        public void setRemindText(String remindText) {
            this.remindtext = remindText;
        }

        public String getCouponJb() {
            return couponJb;
        }

        public void setCouponJb(String couponJb) {
            this.couponJb = couponJb;
        }

        public String getCouponMj() {
            return couponMj;
        }

        public void setCouponMj(String couponMj) {
            this.couponMj = couponMj;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public int getIsParking() {
            return isParking;
        }

        public void setIsParking(int isParking) {
            this.isParking = isParking;
        }

        public int getIsWifi() {
            return isWifi;
        }

        public void setIsWifi(int isWifi) {
            this.isWifi = isWifi;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getSellerAddress() {
            return sellerAddress;
        }

        public void setSellerAddress(String sellerAddress) {
            this.sellerAddress = sellerAddress;
        }

        public float getSellerCondition() {
            return sellerCondition;
        }

        public void setSellerCondition(float sellerCondition) {
            this.sellerCondition = sellerCondition;
        }

        public String getSellerHead() {
            return sellerHead;
        }

        public void setSellerHead(String sellerHead) {
            this.sellerHead = sellerHead;
        }

        public float getSellerMeal() {
            return sellerMeal;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getSellerPhone() {
            return sellerPhone;
        }

        public void setSellerPhone(String sellerPhone) {
            this.sellerPhone = sellerPhone;
        }

        public int getSellerScore() {
            return sellerScore;
        }

        public void setSellerScore(int sellerScore) {
            this.sellerScore = sellerScore;
        }

        public float getSellerServer() {
            return sellerServer;
        }

        public void setSellerServer(float sellerServer) {
            this.sellerServer = sellerServer;
        }

        public String getSmsType() {
            return smsType;
        }

        public void setSmsType(String smsType) {
            this.smsType = smsType;
        }

        public int getWeatherorder() {
            return weatherorder;
        }

        public void setWeatherorder(int weatherorder) {
            this.weatherorder = weatherorder;
        }

        public String getXpoint() {
            return xpoint;
        }

        public void setXpoint(String xpoint) {
            this.xpoint = xpoint;
        }

        public String getYpoint() {
            return ypoint;
        }

        public void setYpoint(String ypoint) {
            this.ypoint = ypoint;
        }

        public String getSellerBigName() {
            return sellerBigName;
        }

        public void setSellerBigName(String sellerBigName) {
            this.sellerBigName = sellerBigName;
        }

        public String getSellerTypeImg() {
            return sellerTypeImg;
        }

        public void setSellerTypeImg(String sellerTypeImg) {
            this.sellerTypeImg = sellerTypeImg;
        }

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public List<?> getImageList() {
            return imageList;
        }

        public void setImageList(List<?> imageList) {
            this.imageList = imageList;
        }

        public List<OpTimesBean> getOpTimes() {
            return opTimes;
        }

        public void setOpTimes(List<OpTimesBean> opTimes) {
            this.opTimes = opTimes;
        }

        public static class OpTimesBean {
            /**
             * weekNum : 周一至周五
             * opList : [{"oDesc":"早餐","oTime":"9:00-14:00"},{"oDesc":"晚餐","oTime":"17:00-23:00"}]
             */

            private String weekNum;
            private List<OpListBean> opList;

            public String getWeekNum() {
                return weekNum;
            }

            public void setWeekNum(String weekNum) {
                this.weekNum = weekNum;
            }

            public List<OpListBean> getOpList() {
                return opList;
            }

            public void setOpList(List<OpListBean> opList) {
                this.opList = opList;
            }

            public static class OpListBean {
                /**
                 * oDesc : 早餐
                 * oTime : 9:00-14:00
                 */

                private String oDesc;
                private String oTime;

                public String getODesc() {
                    return oDesc;
                }

                public void setODesc(String oDesc) {
                    this.oDesc = oDesc;
                }

                public String getOTime() {
                    return oTime;
                }

                public void setOTime(String oTime) {
                    this.oTime = oTime;
                }
            }
        }
    }
}
