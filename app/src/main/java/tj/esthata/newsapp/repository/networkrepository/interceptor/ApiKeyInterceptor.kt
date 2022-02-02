package tj.esthata.newsapp.repository.networkrepository.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import tj.esthata.newsapp.others.Configs
import tj.esthata.newsapp.repository.nativerepository.NativeRepository
import tj.esthata.newsapp.repository.networkrepository.exception.ResponseIsNotSuccess

class ApiKeyInterceptor(context: Context,private val nativeRepository: NativeRepository) : BaseInterceptor(context) {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest =
            request.newBuilder().addHeader(Configs.AUTHORIZATION, nativeRepository.getApiKey()).build()
        val response = chain.proceed(newRequest)
        if (response.isSuccessful) {
            when (response.code) {
                200 -> {
                    return response
                }
                else -> {
                    throw ResponseIsNotSuccess(response.body?.toString())
                }
            }
        } else {
            throw ResponseIsNotSuccess(response.body?.toString())
        }
    }
}