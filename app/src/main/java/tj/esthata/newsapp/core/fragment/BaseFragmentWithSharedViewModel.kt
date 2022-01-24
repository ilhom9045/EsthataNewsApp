package tj.esthata.newsapp.core.fragment

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class BaseFragmentWithSharedViewModel<T : BaseViewModel>(
    clazz: Class<T>, @LayoutRes layout: Int
) : BaseFragment(layout) {

    protected val viewmodel by lazy {
        ViewModelProvider(requireActivity()).get(clazz)
    }

}