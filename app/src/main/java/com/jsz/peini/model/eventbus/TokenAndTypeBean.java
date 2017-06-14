package com.jsz.peini.model.eventbus;

/**
 * Created by 15089 on 2017/2/21.
 */

public class TokenAndTypeBean {
    private String token;
    private String type;

    public TokenAndTypeBean(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public TokenAndTypeBean() {
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TokenAndTypeBean{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
