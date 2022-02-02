package tj.esthata.newsapp.modules.main.ui.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragment
import tj.esthata.newsapp.core.fragment.BaseFragmentWithViewModel
import tj.esthata.newsapp.modules.main.favorite.view.FavoriteFragment
import tj.esthata.newsapp.modules.main.history.view.HistoryFragment
import tj.esthata.newsapp.modules.main.home.ui.view.HomeFragment
import tj.esthata.newsapp.modules.main.settings.view.SettingsFragment
import tj.esthata.newsapp.modules.main.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.main.ui.vm.MainViewModel

class MainFragment :
    BaseFragmentWithViewModel<MainViewModel>(MainViewModel::class.java, R.layout.activity_main),
    OnToolbarChangeListener, SearchView.OnQueryTextListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fragmentContainerView: FragmentContainerView
    private var searchView: SearchView? = null

    companion object {
        private var checkedMenu = 0
        private var selectedMenu = R.id.navigation_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listener()
        viewmodel()
    }

    private fun init(v: View) {
        fragmentContainerView = v.findViewById(R.id.main_container)
        bottomNavigation = v.findViewById(R.id.nav_view)
        toolbar.setToolbar(R.id.base_toolbar)
    }

    private fun listener() {
        bottomNavigation.setOnItemSelectedListener {
            selectedMenu = it.itemId
            when (it.itemId) {

                R.id.navigation_home -> {
                    checkedMenu = 0
                    transaction(HomeFragment.newInstance(this))
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_history -> {
                    checkedMenu = 1
                    transaction(HistoryFragment.newInstance(this))
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_favorite -> {
                    checkedMenu = 2
                    transaction(FavoriteFragment.newInstance(this))
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_settings -> {
                    checkedMenu = 3
                    transaction(SettingsFragment.newInstance(this))
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        bottomNavigation.menu.getItem(checkedMenu).isChecked = true
        bottomNavigation.selectedItemId = selectedMenu
    }

    private fun viewmodel() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        searchView = menu.findItem(R.id.app_bar_search)?.actionView as SearchView?
        searchView?.setOnQueryTextListener(this)
    }

    private fun transaction(fragment: BaseFragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment, this::class.java.simpleName)
            .commit()
    }

    override fun setDisplayHomeEnable(enable: Boolean) {
        toolbar.setDisplayHomeEnable(enable)
    }

    override fun setToolbarTitle(title: String?) {
        toolbar.setTitle(title)
    }

    override fun clearMenu() {
        toolbar.clearMenu()
    }

    override fun setMenu() {
        toolbar.setMenu()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        (requireActivity().supportFragmentManager.findFragmentByTag(this::class.java.simpleName) as BaseFragment).onSearch(
            query
        )
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == null || newText.isEmpty()) {
            (requireActivity().supportFragmentManager.findFragmentByTag(this::class.java.simpleName) as BaseFragment).onSearch(
                newText
            )
        }
        return true
    }
}