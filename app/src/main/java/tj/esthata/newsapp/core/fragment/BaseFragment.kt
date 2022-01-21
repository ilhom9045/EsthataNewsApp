package tj.esthata.newsapp.core.fragment

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import tj.esthata.newsapp.modules.mian.ui.callback.OnSearchViewChangeListener

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout), OnSearchViewChangeListener {

    protected val loading: Loading by lazy {
        Loading()
    }

    inner class Loading() {

        private var base_toolbar: CircularProgressIndicator? = null

        fun setLoading(@IdRes resId: Int): Loading {
            base_toolbar = requireView().findViewById(resId)
            return loading
        }

        fun showLoading(): Loading {
            base_toolbar?.show()
            return loading
        }

        fun hideLoading(): Loading {
            base_toolbar?.hide()
            return loading
        }
    }

    protected fun transaction(
        @IdRes container: Int,
        fragment: BaseFragment,
        addToBackstack: Boolean = true
    ) {
        val transaction = activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(container, fragment)
        if (addToBackstack) {
            transaction?.addToBackStack(fragment::class.simpleName)
        }
        transaction?.commit()
    }
}