package com.jsz.peini.presenter.task;

import com.jsz.peini.presenter.IpConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: 赵亚坤
 * @description: 这个是空间的一个访问网络的基类
 */
public class TaskRetrofitHttp {
    private static TaskService SERVICE;

    public static TaskService getHttp() {
        if (SERVICE == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            SERVICE = new Retrofit.Builder()
                    .baseUrl(IpConfig.HttpPeiniIp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build().create(TaskService.class);
        }
        return SERVICE;
    }


}
