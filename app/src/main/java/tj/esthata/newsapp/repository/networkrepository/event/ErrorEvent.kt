package tj.esthata.newsapp.repository.networkrepository.event

class ErrorEvent(var status: ErrorStatus?, val message: String?)

enum class ErrorStatus {
    InternetConnectionException,
    ErrorException
}