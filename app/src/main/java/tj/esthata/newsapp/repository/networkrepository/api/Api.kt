package tj.esthata.newsapp.repository.networkrepository.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModel
import tj.esthata.newsapp.others.ApiConfig

interface Api {

    @GET("/v2/top-headlines")
    fun getNews(
        @Query("category") category: String,
        @Query("country") country: String = ApiConfig.countryUS
    ): Call<NewResponseModel>

    @GET("/v2/everything")
    fun search(@Query(ApiConfig.qInTitle) qInTitle: String): Call<NewResponseModel>
}