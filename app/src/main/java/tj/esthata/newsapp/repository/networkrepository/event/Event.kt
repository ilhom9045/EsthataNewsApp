package tj.esthata.newsapp.repository.networkrepository.event

class Event<T>(val status: Status, val data: T?) {

    companion object {
        fun <T> loading() = Event<T>(Status.LOADING, null)
        fun <T> error() = Event<T>(Status.ERROR, null)
        fun <T> success(data: T?) = Event(Status.SUCCESS, data)
    }
}

enum class Status {
    LOADING,
    ERROR,
    SUCCESS
}