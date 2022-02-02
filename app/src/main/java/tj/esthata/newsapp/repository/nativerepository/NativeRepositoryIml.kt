package tj.esthata.newsapp.repository.nativerepository

class NativeRepositoryIml : NativeRepository {

    companion object {

        init {
            System.loadLibrary("newsapp")
        }

        private external fun getBaseUrlFromNdk(): String
        private external fun getApiKeyFromNdk(): String
    }

    override fun getBaseUrl(): String {
        return getBaseUrlFromNdk()
    }

    override fun getApiKey(): String {
        return getApiKeyFromNdk()
    }
}