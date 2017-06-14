package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by 15089 on 2017/2/15.
 */
public class MiSignDataBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * score : 242
     * marks : ["发送到","3453"]
     * signDays : 0
     * signList : ["1","0","0","1","0","0","0","1","1","1","1","1","1","1","1","0","0","0","0","0","0","0","0","0","0","0","0","0"]
     */

    private int resultCode;
    private String resultDesc;
    private String score;
    private String signDays;
    private List<String> marks;
    private List<String> signList;

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSignDays() {
        return signDays;
    }

    public void setSignDays(String signDays) {
        this.signDays = signDays;
    }

    public List<String> getMarks() {
        return marks;
    }

    public void setMarks(List<String> marks) {
        this.marks = marks;
    }

    public List<String> getSignList() {
        return signList;
    }

    public void setSignList(List<String> signList) {
        this.signList = signList;
    }

    @Override
    public String toString() {
        return "MiSignDataBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", score='" + score + '\'' +
                ", signDays='" + signDays + '\'' +
                ", marks=" + marks +
                ", signList=" + signList +
                '}';
    }
}
