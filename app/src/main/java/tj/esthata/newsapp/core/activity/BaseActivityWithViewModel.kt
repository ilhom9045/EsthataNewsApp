package tj.esthata.newsapp.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import tj.esthata.newsapp.others.NativeUtil
import tj.esthata.newsapp.repository.networkrepository.event.ErrorStatus
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.reflect.KClass

abstract class BaseActivityWithViewModel<T : BaseViewModel>(
    clazz: KClass<T>,
    @LayoutRes layout: Int
) : BaseActivity(layout) {

    protected val viewmodel by viewModel(clazz = clazz)

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