package tj.esthata.newsapp.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tj.esthata.newsapp.others.MyMutableLiveData
import tj.esthata.newsapp.repository.networkrepository.NetworkRepositry
import tj.esthata.newsapp.repository.networkrepository.event.ErrorEvent
import tj.esthata.newsapp.repository.networkrepository.event.ErrorStatus
import tj.esthata.newsapp.repository.networkrepository.event.Event
import tj.esthata.newsapp.repository.networkrepository.exception.InternetConnectionException
import tj.esthata.newsapp.repository.sqlrepository.SqlRepository
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

abstract class BaseViewModel : ViewModel(), KoinComponent {
    private val mutex = Mutex()
    protected val network: NetworkRepositry by inject()
    protected val sqlRepository: SqlRepository by inject()

    private val _responseErrorHandler = MutableLiveData<ErrorEvent>()
    val responseErrorHandler: LiveData<ErrorEvent> = _responseErrorHandler

    private val requestStack = ArrayList<ContinueRequestModel>()

    fun continueRequest() {
        try {
            for (i in requestStack) {
                i.continueUnit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun <T> asynsRequest(
        liveData: MyMutableLiveData<T>,
        request: () -> Call<T>
    ) {
        liveData.postValue(Event.loading())
        val model = ContinueRequestModel {
            asynsRequest(liveData, request)
        }
        requestStack.add(model)
        try {
            request.invoke().enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        requestStack.remove(model)
                        liveData.value = Event.success(response.body())
                    } else {
                        liveData.value = Event.error()
                        _responseErrorHandler.value =
                            ErrorEvent(ErrorStatus.ErrorException, response.message())
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    errorHandler(t, liveData)
                }
            })
        } catch (e: Exception) {
            errorHandler(e, liveData)
        }
    }

    protected fun <T> synsRequest(
        liveData: MutableLiveData<Event<T>>,
        request: suspend () -> Call<T>
    ) {
        liveData.value = Event.loading()
        val model = ContinueRequestModel {
            synsRequest(liveData, request)
        }
        requestStack.add(model)
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    val res = request.invoke().execute()
                    if (res.isSuccessful) {
                        requestStack.remove(model)
                        liveData.postValue(Event.success(res.body()))
                    } else {
                        liveData.postValue(Event.error())
                        _responseErrorHandler.value =
                            ErrorEvent(ErrorStatus.ErrorException, res.message())
                    }
                } catch (e: Throwable) {
                    launch(Dispatchers.Main) {
                        errorHandler(e, liveData)
                    }
                }
            }
        }
    }

    private fun <T> errorHandler(
        e: Throwable,
        liveData: MutableLiveData<Event<T>>
    ) {
        e.printStackTrace()
        liveData.value = Event.error()
        when (e) {

            is InternetConnectionException -> {
                _responseErrorHandler.value =
                    ErrorEvent(ErrorStatus.InternetConnectionException, null)

            }

            is SocketTimeoutException -> {
                _responseErrorHandler.value =
                    ErrorEvent(
                        ErrorStatus.InternetConnectionException,
                        null
                    )
            }

            is SocketException -> {
                _responseErrorHandler.value =
                    ErrorEvent(
                        ErrorStatus.InternetConnectionException,
                        null
                    )
            }

            is ConnectException -> {
                _responseErrorHandler.value =
                    ErrorEvent(ErrorStatus.InternetConnectionException, null)
            }

            else -> {
                _responseErrorHandler.postValue(
                    ErrorEvent(ErrorStatus.ErrorException, e.message)
                )
            }
        }
    }

    private inner class ContinueRequestModel(val continueUnit: () -> Unit)
}