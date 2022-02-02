package tj.esthata.newsapp.repository.nativerepository

import org.junit.jupiter.api.Assertions.assertEquals


class NativeRepositoryImlTest {

    @org.junit.Test
    fun getBaseUrl() {
        val expect = "https://newsapi.org"
        val actual = NativeRepositoryIml().getBaseUrl()
        assertEquals(expect, actual)
    }

    @org.junit.Test
    fun getApiKey() {
        val expect = "5769b78a970e4a4e83274770741b02c0"
        val actual = NativeRepositoryIml().getApiKey()
        assertEquals(expect, actual)
    }
}