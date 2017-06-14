package com.jsz.peini.model.square;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by th on 2017/1/2.
 */

public class UserInfoByOtherId {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * userInfo : {"nowCityText":"石家庄市","integrityList":"0","goldList":"0","isPhone":"1","sex":"1","signWord":"","spaceBgImg":"/upload/spaceBgImg/2017/3/8/44ea49e0e65045c19e3de5c68a3b46de.jpg","isIdCard":"0","accCode":"10012","buyList":"0","is_video":"0","nickname":"陪你到永远","imageHead":"/upload/headImg/13263181110.png2017/3/9/b11e4d1b65cb43d0b11f8a2d5135b14d.jpg","nowProvinceText":"河北省","mAge":"19"}
     * imageList : [{"imageSrc":"/upload/userimage/2017/3/9/3c0fbde48ee948db985b013348f12929.jpg","id":1362},{"imageSrc":"/upload/userimage/2017/3/9/74b03c5ac07a49ee886b009f969113f9.jpg","id":1363},{"imageSrc":"/upload/userimage/2017/3/9/e33c40d10c4441f2b49576e51863a23b.jpg","id":1364},{"imageSrc":"/upload/userimage/2017/3/9/16bc65fb1ab44f629ed830662054ee8e.jpg","id":1361}]
     * lableList : [{"id":448,"labelName":"有房族"}]
     * taskLastInfo : {"sellerBigType":1,"taskName":"米莎贝尔1号","taskScore":73,"taskId":313}
     * squareLastInfo : {"squareId":368,"imageSrc":"/upload/square/2017/3/7/64118507c5fa4ae58b7e7f3264fe3e24.jpg","content":"红红火火恍恍惚惚"}
     * otherInfo : {"signStatus":0,"selfCount":30,"credit":11,"myConcern":7,"myFans":6,"gold":9679216.33,"score":6}
     */

    private int resultCode;
    private String resultDesc;
    private String imgCnt;
    private UserInfoBean userInfo;
    private TaskLastInfoBean taskLastInfo;
    private SquareLastInfoBean squareLastInfo;
    private OtherInfoBean otherInfo;
    private List<ImageListBean> imageList;
    private List<LableListBean> lableList;

    public String getImgCnt() {
        return imgCnt;
    }

    public void setImgCnt(String imgCnt) {
        this.imgCnt = imgCnt;
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

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public TaskLastInfoBean getTaskLastInfo() {
        return taskLastInfo;
    }

    public void setTaskLastInfo(TaskLastInfoBean taskLastInfo) {
        this.taskLastInfo = taskLastInfo;
    }

    public SquareLastInfoBean getSquareLastInfo() {
        return squareLastInfo;
    }

    public void setSquareLastInfo(SquareLastInfoBean squareLastInfo) {
        this.squareLastInfo = squareLastInfo;
    }

    public OtherInfoBean getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(OtherInfoBean otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<ImageListBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageListBean> imageList) {
        this.imageList = imageList;
    }

    public List<LableListBean> getLableList() {
        return lableList;
    }

    public void setLableList(List<LableListBean> lableList) {
        this.lableList = lableList;
    }

    @Override
    public String toString() {
        return "UserInfoByOtherId{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", imgCnt='" + imgCnt + '\'' +
                ", userInfo=" + userInfo +
                ", taskLastInfo=" + taskLastInfo +
                ", squareLastInfo=" + squareLastInfo +
                ", otherInfo=" + otherInfo +
                ", imageList=" + imageList +
                ", lableList=" + lableList +
                '}';
    }

    public static class UserInfoBean implements Parcelable {
        /**
         * nowCityText : 石家庄市
         * integrityList : 0
         * goldList : 0
         * isPhone : 1
         * sex : 1
         * signWord :
         * spaceBgImg : /upload/spaceBgImg/2017/3/8/44ea49e0e65045c19e3de5c68a3b46de.jpg
         * isIdCard : 0
         * accCode : 10012
         * buyList : 0
         * is_video : 0
         * nickname : 陪你到永远
         * imageHead : /upload/headImg/13263181110.png2017/3/9/b11e4d1b65cb43d0b11f8a2d5135b14d.jpg
         * nowProvinceText : 河北省
         * mAge : 19
         */

        private String id;
        private String nowCityText;
        private String integrityList;
        private String goldList;
        private String isPhone;
        private String sex;
        private String signWord;
        private String spaceBgImg;
        private String isIdCard;
        private String isIdcard;
        private String accCode;
        private String buyList;
        private String is_video;
        private String nickname;
        private String imageHead;
        private String nowProvinceText;
        private String age;
        private String userPhone;
        private String industryText;

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "id='" + id + '\'' +
                    ", nowCityText='" + nowCityText + '\'' +
                    ", integrityList='" + integrityList + '\'' +
                    ", goldList='" + goldList + '\'' +
                    ", isPhone='" + isPhone + '\'' +
                    ", sex='" + sex + '\'' +
                    ", signWord='" + signWord + '\'' +
                    ", spaceBgImg='" + spaceBgImg + '\'' +
                    ", isIdCard='" + isIdCard + '\'' +
                    ", isIdcard='" + isIdcard + '\'' +
                    ", accCode='" + accCode + '\'' +
                    ", buyList='" + buyList + '\'' +
                    ", is_video='" + is_video + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", imageHead='" + imageHead + '\'' +
                    ", nowProvinceText='" + nowProvinceText + '\'' +
                    ", age='" + age + '\'' +
                    ", userPhone='" + userPhone + '\'' +
                    ", industryText='" + industryText + '\'' +
                    '}';
        }

        public String getIsIdcard() {
            return isIdcard;
        }

        public void setIsIdcard(String isIdcard) {
            this.isIdcard = isIdcard;
        }

        public String getIndustryText() {
            return industryText;
        }

        public void setIndustryText(String industryText) {
            this.industryText = industryText;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNowCityText() {
            return nowCityText;
        }

        public void setNowCityText(String nowCityText) {
            this.nowCityText = nowCityText;
        }

        public String getIntegrityList() {
            return integrityList;
        }

        public void setIntegrityList(String integrityList) {
            this.integrityList = integrityList;
        }

        public String getGoldList() {
            return goldList;
        }

        public void setGoldList(String goldList) {
            this.goldList = goldList;
        }

        public String getIsPhone() {
            return isPhone;
        }

        public void setIsPhone(String isPhone) {
            this.isPhone = isPhone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSignWord() {
            return signWord;
        }

        public void setSignWord(String signWord) {
            this.signWord = signWord;
        }

        public String getSpaceBgImg() {
            return spaceBgImg;
        }

        public void setSpaceBgImg(String spaceBgImg) {
            this.spaceBgImg = spaceBgImg;
        }

        public String getIsIdCard() {
            return isIdCard;
        }

        public void setIsIdCard(String isIdCard) {
            this.isIdCard = isIdCard;
        }

        public String getAccCode() {
            return accCode;
        }

        public void setAccCode(String accCode) {
            this.accCode = accCode;
        }

        public String getBuyList() {
            return buyList;
        }

        public void setBuyList(String buyList) {
            this.buyList = buyList;
        }

        public String getIs_video() {
            return is_video;
        }

        public void setIs_video(String is_video) {
            this.is_video = is_video;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
        }

        public String getNowProvinceText() {
            return nowProvinceText;
        }

        public void setNowProvinceText(String nowProvinceText) {
            this.nowProvinceText = nowProvinceText;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.nowCityText);
            dest.writeString(this.integrityList);
            dest.writeString(this.goldList);
            dest.writeString(this.isPhone);
            dest.writeString(this.sex);
            dest.writeString(this.signWord);
            dest.writeString(this.spaceBgImg);
            dest.writeString(this.isIdCard);
            dest.writeString(this.isIdcard);
            dest.writeString(this.accCode);
            dest.writeString(this.buyList);
            dest.writeString(this.is_video);
            dest.writeString(this.nickname);
            dest.writeString(this.imageHead);
            dest.writeString(this.nowProvinceText);
            dest.writeString(this.age);
            dest.writeString(this.userPhone);
            dest.writeString(this.industryText);
        }

        public UserInfoBean() {
        }

        protected UserInfoBean(Parcel in) {
            this.id = in.readString();
            this.nowCityText = in.readString();
            this.integrityList = in.readString();
            this.goldList = in.readString();
            this.isPhone = in.readString();
            this.sex = in.readString();
            this.signWord = in.readString();
            this.spaceBgImg = in.readString();
            this.isIdCard = in.readString();
            this.isIdcard = in.readString();
            this.accCode = in.readString();
            this.buyList = in.readString();
            this.is_video = in.readString();
            this.nickname = in.readString();
            this.imageHead = in.readString();
            this.nowProvinceText = in.readString();
            this.age = in.readString();
            this.userPhone = in.readString();
            this.industryText = in.readString();
        }

        public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
            @Override
            public UserInfoBean createFromParcel(Parcel source) {
                return new UserInfoBean(source);
            }

            @Override
            public UserInfoBean[] newArray(int size) {
                return new UserInfoBean[size];
            }
        };
    }

    public static class TaskLastInfoBean {
        /**
         * sellerBigType : 1
         * taskName : 米莎贝尔1号
         * taskScore : 73
         * taskId : 313
         */

        private String sellerBigType;
        private String taskName;
        private String taskScore;
        private String taskId;
        private String sellerTypeImg;

        public String getSellerTypeImg() {
            return sellerTypeImg;
        }

        public void setSellerTypeImg(String sellerTypeImg) {
            this.sellerTypeImg = sellerTypeImg;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskScore() {
            return taskScore;
        }

        public void setTaskScore(String taskScore) {
            this.taskScore = taskScore;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getSellerBigType() {

            return sellerBigType;
        }

        public void setSellerBigType(String sellerBigType) {
            this.sellerBigType = sellerBigType;
        }
    }

    public static class SquareLastInfoBean {
        /**
         * squareId : 368
         * imageSrc : /upload/square/2017/3/7/64118507c5fa4ae58b7e7f3264fe3e24.jpg
         * content : 红红火火恍恍惚惚
         */

        private int squareId;
        private String imageSrc;
        private String content;

        public int getSquareId() {
            return squareId;
        }

        public void setSquareId(int squareId) {
            this.squareId = squareId;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class OtherInfoBean {
        /**
         * signStatus : 0
         * selfCount : 30
         * credit : 11
         * myConcern : 7
         * myFans : 6
         * gold : 9679216.33
         * score : 6
         */

        private int isConcern;
        private int signStatus;
        private int selfCount;
        private int credit;
        private int myConcern;
        private int myFans;
        private BigDecimal gold;
        private String score;

        public int getIsConcern() {
            return isConcern;
        }

        public void setIsConcern(int isConcern) {
            this.isConcern = isConcern;
        }

        public int getSignStatus() {
            return signStatus;
        }

        public void setSignStatus(int signStatus) {
            this.signStatus = signStatus;
        }

        public int getSelfCount() {
            return selfCount;
        }

        public void setSelfCount(int selfCount) {
            this.selfCount = selfCount;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public int getMyConcern() {
            return myConcern;
        }

        public void setMyConcern(int myConcern) {
            this.myConcern = myConcern;
        }

        public int getMyFans() {
            return myFans;
        }

        public void setMyFans(int myFans) {
            this.myFans = myFans;
        }

        public BigDecimal getGold() {
            return gold;
        }

        public void setGold(BigDecimal gold) {
            this.gold = gold;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

    public static class ImageListBean {
        /**
         * imageSrc : /upload/userimage/2017/3/9/3c0fbde48ee948db985b013348f12929.jpg
         * id : 1362
         */

        private String imageSrc;
        private int id;

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class LableListBean {
        /**
         * id : 448
         * labelName : 有房族
         */

        private int id;
        private String labelName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }
    }
}
