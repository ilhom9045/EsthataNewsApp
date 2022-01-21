package tj.esthata.newsapp.repository.networkrepository.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import tj.esthata.newsapp.others.Configs
import tj.esthata.newsapp.others.NativeUtil

class ApiKeyInterceptor(context: Context) : BaseInterceptor(context) {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest =
            request.newBuilder().addHeader(Configs.AUTHORIZATION, NativeUtil.getApiKey()).build()
        val response = chain.proceed(newRequest)
        if (response.isSuccessful) {
            when (response.code) {
                200 -> {
                    return response
                }
                else -> {
                    throw Exception("Response is`t available ${response.code} ${response.body}")
                }
            }
        } else {
            throw Exception("Response is`t available ${response.code} ${response.body}")
        }
    }
}