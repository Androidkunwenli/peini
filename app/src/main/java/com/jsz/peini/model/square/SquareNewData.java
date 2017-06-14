package com.jsz.peini.model.square;

import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.news.NewsList;

import java.util.List;

/**
 * Created by lenovo on 2017/6/10.
 */

public class SquareNewData {
    private int resultCode;
    private String resultDesc;
    private List<SquareNewList> mSquareNewLists;

    @Override
    public String toString() {
        return "SquareNewData{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", mSquareNewLists=" + mSquareNewLists +
                '}';
    }

    public static class SquareNewList {
        private String image;
        private String name;

        public SquareNewList(String image, String name) {
            this.image = image;
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SquareNewList{" +
                    "image='" + image + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
