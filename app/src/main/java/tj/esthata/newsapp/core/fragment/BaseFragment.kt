package tj.esthata.newsapp.core.fragment

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.CircularProgressIndicator
import tj.esthata.newsapp.core.activity.BaseActivity

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    protected val loading: Loading by lazy { Loading() }

    protected val toolbar: Toolbar by lazy { Toolbar() }

    protected open inner class Toolbar {

        private var base_toolbar: MaterialToolbar? = null

        fun setToolbar(@IdRes material_toolbar_id: Int): Toolbar {
            base_toolbar = requireView().findViewById(material_toolbar_id)
            setHasOptionsMenu(true)
            if (activity is BaseActivity) {
                (activity as BaseActivity).setSupportActionBar(base_toolbar)
            }
            return toolbar
        }

        fun setTitle(title: String?): Toolbar {
            if (activity is BaseActivity) {
                (activity as BaseActivity).supportActionBar?.title = title
            }
            return toolbar
        }

        fun setDisplayHomeEnable(enable: Boolean): Toolbar {
            if (activity is BaseActivity) {
                (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(enable)
            }
            return toolbar
        }

        fun clearMenu() {
            base_toolbar?.menu?.clear()
        }

        fun setMenu() {
            base_toolbar?.menu?.let { onCreateOptionsMenu(it, requireActivity().menuInflater) }
        }
    }

    open fun onSearch(q: String?) {

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