package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by 15089 on 2017/2/18.
 */
public class MiLabelBean {
    private List<LabelInfoBean> labelInfoshow;
    private List<LabelInfoBean> labelInfo;

    public List<LabelInfoBean> getLabelInfoshow() {
        return labelInfoshow;
    }

    public void setLabelInfoshow(List<LabelInfoBean> labelInfoshow) {
        this.labelInfoshow = labelInfoshow;
    }

    public List<LabelInfoBean> getLabelInfo() {
        return labelInfo;
    }

    public void setLabelInfo(List<LabelInfoBean> labelInfo) {
        this.labelInfo = labelInfo;
    }

    @Override
    public String toString() {
        return "MiLabelBean{" +
                "labelInfoshow=" + labelInfoshow +
                ", labelInfo=" + labelInfo +
                '}';
    }

    public static class LabelInfoBean {
        /**
         * id : 369
         * labelName : 忠实男粉
         * labelState : 0
         */

        private int id;
        private String labelName;
        private int labelState;

        @Override
        public String toString() {
            return "LabelInfoBean{" +
                    "id=" + id +
                    ", labelName='" + labelName + '\'' +
                    ", labelState=" + labelState +
                    '}';
        }

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

        public int getLabelState() {
            return labelState;
        }

        public void setLabelState(int labelState) {
            this.labelState = labelState;
        }
    }
}
