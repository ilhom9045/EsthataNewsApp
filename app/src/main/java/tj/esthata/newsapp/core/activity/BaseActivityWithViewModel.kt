package tj.esthata.newsapp.core.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import tj.esthata.newsapp.repository.networkrepository.event.ErrorStatus

abstract class BaseActivityWithViewModel<T : BaseViewModel>(
    clazz: Class<T>,
    @LayoutRes layout: Int
) : BaseActivity(layout) {

    protected val viewmodel by lazy{
        ViewModelProvider(this)[clazz]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.responseErrorHandler.observe(this, {
            when (it.status) {

                ErrorStatus.InternetConnectionException -> {
                    showInternetErrorConnectionDialog() {
                        viewmodel.continueRequest()
                    }
                }

                ErrorStatus.ErrorException -> {
                    it.message?.let { it1 -> showInternetErrorResponse(it1) }
                }

                null -> {

                }
            }
        })
    }
}