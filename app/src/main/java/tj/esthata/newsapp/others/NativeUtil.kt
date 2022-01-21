package tj.esthata.newsapp.others

class NativeUtil {
    companion object {
        init {
            System.loadLibrary("newsapp")
        }
        external fun getBaseUrl(): String
        external fun getApiKey(): String
    }
}