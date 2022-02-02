package tj.esthata.newsapp.repository.nativerepository

interface NativeRepository {
    fun getBaseUrl(): String
    fun getApiKey(): String
}