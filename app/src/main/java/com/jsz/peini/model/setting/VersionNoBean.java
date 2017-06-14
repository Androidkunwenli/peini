package com.jsz.peini.model.setting;

/**
 * Created by lenovo on 2017/3/11.
 */

public class VersionNoBean {

    /**
     * data : {"address":"http://app.91peini.com/pnservice/app/pn.apk","version":"2"}
     * resultCode : 1
     * resultDesc :
     */

    private DataBean data;
    private int resultCode;
    private String resultDesc;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * address : http://app.91peini.com/pnservice/app/pn.apk
         * version : 2
         */

        private String address;
        private int version;
        private int forceUpdate;
        /**
        *活动用的
        */
        private String shareTitle;
        private String shareContent;

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public int getForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(int forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }
}
