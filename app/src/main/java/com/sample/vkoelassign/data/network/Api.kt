package com.sample.vkoelassign.data.network

import com.sample.vkoelassign.utility.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface Api {


    companion object {
        fun create(): Api {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()

            return retrofit.create(Api::class.java)
        }
    }

/*    //https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=eff51cba0dbe4e91b7fe7cb2095ddfca
    @GET("top-headlines")
    //query needed if there is any query
    fun getHeadLineList(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "eff51cba0dbe4e91b7fe7cb2095ddfca"
    ): Observable<HeadLineResponseModel>

    @GET("top-headlines")
    fun getArticleList2(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String = "eff51cba0dbe4e91b7fe7cb2095ddfca"
    ): LiveData<ApiResponse<ArticleResponse>>

    @GET("top-headlines")
    fun getArticleList3(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String = "eff51cba0dbe4e91b7fe7cb2095ddfca"
    ): Observable<ArticleResponse>*/
}