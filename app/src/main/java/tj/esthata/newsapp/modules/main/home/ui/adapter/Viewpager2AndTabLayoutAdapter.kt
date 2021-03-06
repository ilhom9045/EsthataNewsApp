package tj.esthata.newsapp.modules.main.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tj.esthata.newsapp.core.fragment.BaseFragment
import tj.esthata.newsapp.modules.main.home.ui.model.TabLayoutAndViewPagerModel

class Viewpager2AndTabLayoutAdapter(fm: Fragment) :
    FragmentStateAdapter(fm.childFragmentManager, fm.lifecycle) {

    private var items = ArrayList<TabLayoutAndViewPagerModel>()

    fun getTabLayoutTitle(position: Int): String {
        return items[position].title
    }

    fun getFragment(position: Int): BaseFragment {
        return items[position].fragment
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position].fragment
    }

    fun addFragment(fragmentWithTabName: TabLayoutAndViewPagerModel) {
        items.add(fragmentWithTabName)
    }

}