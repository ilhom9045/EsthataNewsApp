package tj.esthata.newsapp.repository.networkrepository

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tj.esthata.newsapp.BuildConfig
import tj.esthata.newsapp.others.NativeUtil
import tj.esthata.newsapp.repository.networkrepository.api.Api
import tj.esthata.newsapp.repository.networkrepository.interceptor.ApiKeyInterceptor

class NetworkRepositoryImpl(private val context: Context) : NetworkRepositry {

    override fun getApi(): Api {
        return Retrofit
            .Builder()
            .baseUrl(getBaseUrl())
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    private fun getClient() = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        })
        .addInterceptor(ApiKeyInterceptor(context))
        .build()

    private fun getBaseUrl() = NativeUtil.getBaseUrl()
}