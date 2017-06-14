package com.jsz.peini.presenter.search;

import com.jsz.peini.model.seller.SellerTabulationBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/20.
 */

public interface SearchService {

    @POST("searchAllSeller")
    Call<SellerTabulationBean> searchAllSeller(
            @Query("searchWord") String searchWord,
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("pageNow") int pageNow,
            @Query("pageSize") String pageSize);

}
