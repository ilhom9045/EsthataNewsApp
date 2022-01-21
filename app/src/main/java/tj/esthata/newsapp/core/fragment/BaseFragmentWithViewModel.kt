package tj.esthata.newsapp.core.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import tj.esthata.newsapp.core.activity.BaseActivity
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import tj.esthata.newsapp.repository.networkrepository.event.ErrorStatus
import kotlin.reflect.KClass

abstract class BaseFragmentWithViewModel<T : BaseViewModel>(
    val clazz: Class<T>, @LayoutRes layout: Int
) : BaseFragment(layout) {

    protected lateinit var viewmodel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this).get(clazz)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.responseErrorHandler.observe(viewLifecycleOwner, {
            when (it.status) {

                ErrorStatus.InternetConnectionException -> {
                    (activity as BaseActivity).showInternetErrorConnectionDialog() {
                        it.status = null
                        viewmodel.continueRequest()
                    }
                }

                ErrorStatus.ErrorException -> {
                    it.message?.let { it1 ->
                        it.status = null
                        (activity as BaseActivity).showInternetErrorResponse(
                            it1
                        )
                    }
                }
                null -> {

                }
            }
        })
    }

}