package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by 15089 on 2017/2/22.
 */
public class RecentDonationData {


    /**
     * data : {"list":[{"headImg":"/upload/headImg/13703398714.png2017/2/24/877aa43a7e20404f9aaa529c7359b887.png","hisId":"69c742f490344e6db016f084bad3c724","nickName":"乐乐","otherId":"ced5672fab364ded8992466a70d1e232","relation":"陌生人"},{"hisId":"f04910b1679d422bb1314ac72a3e0d86","nickName":"陪你到永远","otherId":"a7e6016b5228492fb269ea870333f56f","relation":"双方关注"}],"serverB":"1000729"}
     * resultCode : 1
     * resultDesc : SUCCESS
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
         * list : [{"headImg":"/upload/headImg/13703398714.png2017/2/24/877aa43a7e20404f9aaa529c7359b887.png","hisId":"69c742f490344e6db016f084bad3c724","nickName":"乐乐","otherId":"ced5672fab364ded8992466a70d1e232","relation":"陌生人"},{"hisId":"f04910b1679d422bb1314ac72a3e0d86","nickName":"陪你到永远","otherId":"a7e6016b5228492fb269ea870333f56f","relation":"双方关注"}]
         * serverB : 1000729
         */

        private String serverB;
        private List<ListBean> list;

        public String getServerB() {
            return serverB;
        }

        public void setServerB(String serverB) {
            this.serverB = serverB;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * headImg : /upload/headImg/13703398714.png2017/2/24/877aa43a7e20404f9aaa529c7359b887.png
             * hisId : 69c742f490344e6db016f084bad3c724
             * nickName : 乐乐
             * otherId : ced5672fab364ded8992466a70d1e232
             * relation : 陌生人
             */

            private String headImg;
            private String hisId;
            private String nickName;
            private String otherId;
            private String relation;

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            public String getHisId() {
                return hisId;
            }

            public void setHisId(String hisId) {
                this.hisId = hisId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getOtherId() {
                return otherId;
            }

            public void setOtherId(String otherId) {
                this.otherId = otherId;
            }

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }
        }
    }
}
