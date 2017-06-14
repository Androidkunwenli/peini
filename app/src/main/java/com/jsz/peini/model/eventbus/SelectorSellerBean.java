package com.jsz.peini.model.eventbus;

/**
 * Created by th on 2017/1/23.
 */

public class SelectorSellerBean {
    //任务id
    private String id;
    //小标签
    private String tyge;
    //任务名字
    private String name;

    public SelectorSellerBean(String id, String tyge, String name) {
        this.id = id;
        this.tyge = tyge;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTyge() {
        return tyge;
    }

    public void setTyge(String tyge) {
        this.tyge = tyge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SelectorSellerBean{" +
                "id='" + id + '\'' +
                ", tyge='" + tyge + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
