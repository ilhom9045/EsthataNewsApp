package tj.esthata.newsapp.repository.networkrepository


import android.content.Context
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tj.esthata.newsapp.repository.nativerepository.NativeRepository
import tj.esthata.newsapp.repository.networkrepository.api.Api

class NetworkRepositoryImplTest {

    @Test
    fun getApi() {
        val nativeRepository = mock<NativeRepository>()
        Mockito.`when`(nativeRepository.getBaseUrl()).thenReturn("https://newsapi.org")
        Mockito.`when`(nativeRepository.getApiKey()).thenReturn("5769b78a970e4a4e83274770741b02c0")

        val context = mockk<Context>(relaxed = true)

        val expect:Api = Retrofit.Builder()
            .baseUrl(nativeRepository.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

        val networkRepository: NetworkRepository =
            NetworkRepositoryImpl(context, nativeRepository)
        val actual = networkRepository.getApi()
        Assertions.assertEquals(expect, actual)
    }
}