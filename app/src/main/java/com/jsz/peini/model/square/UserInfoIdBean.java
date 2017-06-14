package com.jsz.peini.model.square;

/**
 * Created by th on 2017/1/4.
 */

public class UserInfoIdBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * userInfo : {"id":1,"userLoginId":1,"nickname":"大驴","signWord":"我是二二","imageHead":"/upload/user/1/20161211145644.png","sex":1,"sexText":"男","mAge":35,"height":null,"weight":null,"oldProvince":null,"oldProvinceText":null,"oldCity":null,"oldCityText":null,"oldCounty":null,"oldCountyText":null,"nowProvince":130000,"nowProvinceText":"河北省","nowCity":130100,"nowCityText":"石家庄市","nowCounty":null,"nowCountyText":null,"constellation":1,"constellationText":"白羊座","nation":1,"nationText":"汉族","smallIncome":7000,"bigIncome":null,"degree":1,"degreeText":"小学","industry":1,"industryText":"计算机/互联网/通信","isHouse":0,"isHouseText":null,"isCar":1,"isCarText":"已购车","isPhone":0,"isIdcard":1,"isVideo":0,"birthday":"2016-12-09","emotion":1,"emotionText":"单身","reputation":null,"goldList":1,"buyList":0,"integrityList":0,"selfNum":70,"idcardNum":40,"taskNum":600,"isRank":null}
     */

    private int resultCode;
    private String resultDesc;
    private UserInfoBean userInfo;

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

    @Override
    public String toString() {
        return "UserInfoIdBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }

    public static class UserInfoBean {
        /**
         * id : 1
         * userLoginId : 1
         * nickname : 大驴
         * signWord : 我是二二
         * imageHead : /upload/user/1/20161211145644.png
         * sex : 1
         * sexText : 男
         * mAge : 35
         * height : null
         * weight : null
         * oldProvince : null
         * oldProvinceText : null
         * oldCity : null
         * oldCityText : null
         * oldCounty : null
         * oldCountyText : null
         * nowProvince : 130000
         * nowProvinceText : 河北省
         * nowCity : 130100
         * nowCityText : 石家庄市
         * nowCounty : null
         * nowCountyText : null
         * constellation : 1
         * constellationText : 白羊座
         * nation : 1
         * nationText : 汉族
         * smallIncome : 7000
         * bigIncome : null
         * degree : 1
         * degreeText : 小学
         * industry : 1
         * industryText : 计算机/互联网/通信
         * isHouse : 0
         * isHouseText : null
         * isCar : 1
         * isCarText : 已购车
         * isPhone : 0
         * isIdcard : 1
         * isVideo : 0
         * birthday : 2016-12-09
         * emotion : 1
         * emotionText : 单身
         * reputation : null
         * goldList : 1
         * buyList : 0
         * integrityList : 0
         * selfNum : 70
         * idcardNum : 40
         * taskNum : 600
         * isRank : null
         */

        private String id;
        private String userLoginId;
        private String nickname;
        private String signWord;
        private String imageHead;
        private int sex;
        private int age;
        private String height;
        private String weight;
        private int oldProvince;
        private String oldProvinceText;
        private int oldCity;
        private String oldCityText;
        private int oldCounty;
        private String oldCountyText;
        private int nowProvince;
        private String nowProvinceText;
        private int nowCity;
        private String nowCityText;
        private int nowCounty;
        private String nowCountyText;
        private int constellation;
        private String constellationText;
        private int nation;
        private String nationText;
        private int smallIncome;
        private int bigIncome;
        private int degree;
        private String degreeText;
        private int industry;
        private String industryText;
        private int isHouse;
        private Object isHouseText;
        private int isCar;
        private String isCarText;
        private int isPhone;
        private int isIdcard;
        private int isVideo;
        private String birthday;
        private int emotion;
        private String emotionText;
        private Object reputation;
        private int goldList;
        private int buyList;
        private int integrityList;
        private int selfNum;
        private int idcardNum;
        private int taskNum;
        private Object isRank;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(String userLoginId) {
            this.userLoginId = userLoginId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSignWord() {
            return signWord;
        }

        public void setSignWord(String signWord) {
            this.signWord = signWord;
        }

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public int getOldProvince() {
            return oldProvince;
        }

        public void setOldProvince(int oldProvince) {
            this.oldProvince = oldProvince;
        }

        public String getOldProvinceText() {
            return oldProvinceText;
        }

        public void setOldProvinceText(String oldProvinceText) {
            this.oldProvinceText = oldProvinceText;
        }

        public int getOldCity() {
            return oldCity;
        }

        public void setOldCity(int oldCity) {
            this.oldCity = oldCity;
        }

        public String getOldCityText() {
            return oldCityText;
        }

        public void setOldCityText(String oldCityText) {
            this.oldCityText = oldCityText;
        }

        public int getOldCounty() {
            return oldCounty;
        }

        public void setOldCounty(int oldCounty) {
            this.oldCounty = oldCounty;
        }

        public String getOldCountyText() {
            return oldCountyText;
        }

        public void setOldCountyText(String oldCountyText) {
            this.oldCountyText = oldCountyText;
        }

        public int getNowProvince() {
            return nowProvince;
        }

        public void setNowProvince(int nowProvince) {
            this.nowProvince = nowProvince;
        }

        public String getNowProvinceText() {
            return nowProvinceText;
        }

        public void setNowProvinceText(String nowProvinceText) {
            this.nowProvinceText = nowProvinceText;
        }

        public int getNowCity() {
            return nowCity;
        }

        public void setNowCity(int nowCity) {
            this.nowCity = nowCity;
        }

        public String getNowCityText() {
            return nowCityText;
        }

        public void setNowCityText(String nowCityText) {
            this.nowCityText = nowCityText;
        }

        public int getNowCounty() {
            return nowCounty;
        }

        public void setNowCounty(int nowCounty) {
            this.nowCounty = nowCounty;
        }

        public String getNowCountyText() {
            return nowCountyText;
        }

        public void setNowCountyText(String nowCountyText) {
            this.nowCountyText = nowCountyText;
        }

        public int getConstellation() {
            return constellation;
        }

        public void setConstellation(int constellation) {
            this.constellation = constellation;
        }

        public String getConstellationText() {
            return constellationText;
        }

        public void setConstellationText(String constellationText) {
            this.constellationText = constellationText;
        }

        public int getNation() {
            return nation;
        }

        public void setNation(int nation) {
            this.nation = nation;
        }

        public String getNationText() {
            return nationText;
        }

        public void setNationText(String nationText) {
            this.nationText = nationText;
        }

        public int getSmallIncome() {
            return smallIncome;
        }

        public void setSmallIncome(int smallIncome) {
            this.smallIncome = smallIncome;
        }

        public int getBigIncome() {
            return bigIncome;
        }

        public void setBigIncome(int bigIncome) {
            this.bigIncome = bigIncome;
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

        public String getDegreeText() {
            return degreeText;
        }

        public void setDegreeText(String degreeText) {
            this.degreeText = degreeText;
        }

        public int getIndustry() {
            return industry;
        }

        public void setIndustry(int industry) {
            this.industry = industry;
        }

        public String getIndustryText() {
            return industryText;
        }

        public void setIndustryText(String industryText) {
            this.industryText = industryText;
        }

        public int getIsHouse() {
            return isHouse;
        }

        public void setIsHouse(int isHouse) {
            this.isHouse = isHouse;
        }

        public Object getIsHouseText() {
            return isHouseText;
        }

        public void setIsHouseText(Object isHouseText) {
            this.isHouseText = isHouseText;
        }

        public int getIsCar() {
            return isCar;
        }

        public void setIsCar(int isCar) {
            this.isCar = isCar;
        }

        public String getIsCarText() {
            return isCarText;
        }

        public void setIsCarText(String isCarText) {
            this.isCarText = isCarText;
        }

        public int getIsPhone() {
            return isPhone;
        }

        public void setIsPhone(int isPhone) {
            this.isPhone = isPhone;
        }

        public int getIsIdcard() {
            return isIdcard;
        }

        public void setIsIdcard(int isIdcard) {
            this.isIdcard = isIdcard;
        }

        public int getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(int isVideo) {
            this.isVideo = isVideo;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getEmotion() {
            return emotion;
        }

        public void setEmotion(int emotion) {
            this.emotion = emotion;
        }

        public String getEmotionText() {
            return emotionText;
        }

        public void setEmotionText(String emotionText) {
            this.emotionText = emotionText;
        }

        public Object getReputation() {
            return reputation;
        }

        public void setReputation(Object reputation) {
            this.reputation = reputation;
        }

        public int getGoldList() {
            return goldList;
        }

        public void setGoldList(int goldList) {
            this.goldList = goldList;
        }

        public int getBuyList() {
            return buyList;
        }

        public void setBuyList(int buyList) {
            this.buyList = buyList;
        }

        public int getIntegrityList() {
            return integrityList;
        }

        public void setIntegrityList(int integrityList) {
            this.integrityList = integrityList;
        }

        public int getSelfNum() {
            return selfNum;
        }

        public void setSelfNum(int selfNum) {
            this.selfNum = selfNum;
        }

        public int getIdcardNum() {
            return idcardNum;
        }

        public void setIdcardNum(int idcardNum) {
            this.idcardNum = idcardNum;
        }

        public int getTaskNum() {
            return taskNum;
        }

        public void setTaskNum(int taskNum) {
            this.taskNum = taskNum;
        }

        public Object getIsRank() {
            return isRank;
        }

        public void setIsRank(Object isRank) {
            this.isRank = isRank;
        }

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "id=" + id +
                    ", userLoginId=" + userLoginId +
                    ", nickname='" + nickname + '\'' +
                    ", signWord='" + signWord + '\'' +
                    ", imageHead='" + imageHead + '\'' +
                    ", sex=" + sex +
                    ", mAge=" + age +
                    ", height=" + height +
                    ", weight=" + weight +
                    ", oldProvince=" + oldProvince +
                    ", oldProvinceText=" + oldProvinceText +
                    ", oldCity=" + oldCity +
                    ", oldCityText=" + oldCityText +
                    ", oldCounty=" + oldCounty +
                    ", oldCountyText=" + oldCountyText +
                    ", nowProvince=" + nowProvince +
                    ", nowProvinceText='" + nowProvinceText + '\'' +
                    ", nowCity=" + nowCity +
                    ", nowCityText='" + nowCityText + '\'' +
                    ", nowCounty=" + nowCounty +
                    ", nowCountyText=" + nowCountyText +
                    ", constellation=" + constellation +
                    ", constellationText='" + constellationText + '\'' +
                    ", nation=" + nation +
                    ", nationText='" + nationText + '\'' +
                    ", smallIncome=" + smallIncome +
                    ", bigIncome=" + bigIncome +
                    ", degree=" + degree +
                    ", degreeText='" + degreeText + '\'' +
                    ", industry=" + industry +
                    ", industryText='" + industryText + '\'' +
                    ", isHouse=" + isHouse +
                    ", isHouseText=" + isHouseText +
                    ", isCar=" + isCar +
                    ", isCarText='" + isCarText + '\'' +
                    ", isPhone=" + isPhone +
                    ", isIdcard=" + isIdcard +
                    ", isVideo=" + isVideo +
                    ", birthday='" + birthday + '\'' +
                    ", emotion=" + emotion +
                    ", emotionText='" + emotionText + '\'' +
                    ", reputation=" + reputation +
                    ", goldList=" + goldList +
                    ", buyList=" + buyList +
                    ", integrityList=" + integrityList +
                    ", selfNum=" + selfNum +
                    ", idcardNum=" + idcardNum +
                    ", taskNum=" + taskNum +
                    ", isRank=" + isRank +
                    '}';
        }
    }
}