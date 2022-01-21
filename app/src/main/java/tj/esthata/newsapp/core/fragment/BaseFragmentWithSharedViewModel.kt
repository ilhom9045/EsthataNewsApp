package tj.esthata.newsapp.core.fragment

import androidx.annotation.LayoutRes
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class BaseFragmentWithSharedViewModel<T : BaseViewModel>(
    clazz: KClass<T>, @LayoutRes layout: Int
) : BaseFragment(layout) {

    protected val viewmodel by sharedViewModel(clazz = clazz)

}