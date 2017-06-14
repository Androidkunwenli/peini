package com.jsz.peini.presenter.serach;

import com.jsz.peini.model.filter.HotWordBean;
import com.jsz.peini.model.search.SearchHot;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/16.
 */

public interface SerachService {
    @POST("getHotWord")
    Call<HotWordBean> getHotWord();

    @POST("searchFastSeller")
    Call<SearchHot> searchFastSeller(
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("searchWord") String searchWord);

}
