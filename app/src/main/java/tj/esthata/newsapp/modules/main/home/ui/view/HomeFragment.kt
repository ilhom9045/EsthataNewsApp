package tj.esthata.newsapp.modules.main.home.ui.view

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragmentWithSharedViewModel
import tj.esthata.newsapp.core.fragment.BaseFragmentWithViewModel
import tj.esthata.newsapp.modules.main.home.ui.adapter.Viewpager2AndTabLayoutAdapter
import tj.esthata.newsapp.modules.main.home.ui.model.TabLayoutAndViewPagerModel
import tj.esthata.newsapp.modules.main.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.main.ui.vm.MainViewModel
import tj.esthata.newsapp.others.d

class HomeFragment :
    BaseFragmentWithSharedViewModel<MainViewModel>(MainViewModel::class.java, R.layout.fragment_home) {

    private var onToolbarChangeListener: OnToolbarChangeListener? = null

    private lateinit var tablayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewpagerAdapter: Viewpager2AndTabLayoutAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        viewmodel(savedInstanceState)
        listener()
    }

    private fun viewmodel(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewmodel.settingsCategory.value?.let {
                for (i in it) {
                    if (i.isChecked) {
                        viewmodel.addToHash(i.name)
                        onToolbarChangeListener?.let { it1 ->
                            ViewPagerFragment.newInstance(
                                it1,
                                i.category,
                                i.name
                            )
                        }?.let { it2 ->
                            TabLayoutAndViewPagerModel(
                                i.name,
                                it2
                            )
                        }?.let { it3 ->
                            viewpagerAdapter.addFragment(
                                it3
                            )
                        }
                    } else {
                        viewmodel.removeFromHash(i.name)
                    }
                }
            }
        }
    }

    override fun onSearch(q: String?) {
        d("onSearch home", q.toString())
        if (isResumed) {
            viewpagerAdapter.getFragment(tablayout.selectedTabPosition).onSearch(q)
        }
    }

    private fun init(v: View) {
        tablayout = v.findViewById(R.id.home_tablayout)
        viewPager2 = v.findViewById(R.id.home_viewpager)
        viewpagerAdapter = Viewpager2AndTabLayoutAdapter(this)
    }

    private fun listener() {
        onToolbarChangeListener?.setToolbarTitle(resources.getString(R.string.title_home))
        onToolbarChangeListener?.setDisplayHomeEnable(false)
        onToolbarChangeListener?.setMenu()
        viewPager2.adapter = viewpagerAdapter
        TabLayoutMediator(tablayout, viewPager2) { tab, position ->
            tab.text = viewpagerAdapter.getTabLayoutTitle(position)
        }.attach()
    }

    companion object {
        fun newInstance(onToolbarChangeListener: OnToolbarChangeListener) = HomeFragment().apply {
            this.onToolbarChangeListener = onToolbarChangeListener
        }
    }

}