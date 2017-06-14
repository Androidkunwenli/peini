package com.jsz.peini.model.square;

import java.io.Serializable;

/**
 * Created by th on 2016/12/26.
 */

public class ImageListBean implements Serializable {
    private int id;
    private String imageSrc;
    private String imageTime;
    private String imageSmall;
    private boolean imageIsFile = true;

    @Override
    public String toString() {
        return "ImageListBean{" +
                "id=" + id +
                ", imageSrc='" + imageSrc + '\'' +
                ", imageTime='" + imageTime + '\'' +
                ", imageSmall='" + imageSmall + '\'' +
                ", imageIsFile=" + imageIsFile +
                ", isChecked=" + isChecked +
                '}';
    }


    public ImageListBean(String imageSmall, boolean imageIsFile) {
        this.imageSmall = imageSmall;
        this.imageIsFile = imageIsFile;
    }

    public ImageListBean(String imageSmall, String imageSrc) {
        this.imageSmall = imageSmall;
        this.imageSrc = imageSrc;
    }

    public ImageListBean() {
    }

    public boolean isImageIsFile() {
        return imageIsFile;
    }

    public void setImageIsFile(boolean imageIsFile) {
        this.imageIsFile = imageIsFile;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

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

    public String getImageTime() {
        return imageTime;
    }

    public void setImageTime(String imageTime) {
        this.imageTime = imageTime;
    }

}