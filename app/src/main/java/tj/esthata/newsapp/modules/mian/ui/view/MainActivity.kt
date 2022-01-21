package tj.esthata.newsapp.modules.mian.ui.view

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.activity.BaseActivityWithViewModel
import tj.esthata.newsapp.core.fragment.BaseFragment
import tj.esthata.newsapp.modules.mian.favorite.view.FavoriteFragment
import tj.esthata.newsapp.modules.mian.history.view.HistoryFragment
import tj.esthata.newsapp.modules.mian.home.ui.view.HomeFragment
import tj.esthata.newsapp.modules.mian.settings.view.SettingsFragment
import tj.esthata.newsapp.modules.mian.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.mian.ui.model.BottomNavigationFragmentModel
import tj.esthata.newsapp.modules.mian.ui.vm.MainViewModel
import tj.esthata.newsapp.others.d

class MainActivity :
    BaseActivityWithViewModel<MainViewModel>(MainViewModel::class, R.layout.activity_main),
    OnToolbarChangeListener, SearchView.OnQueryTextListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private var searchView: SearchView? = null

    companion object {
        private var checkedMenu = 0
        private var selectedMenu = R.id.navigation_home
    }

    private val bottomNavigationItemList =
        ArrayList<BottomNavigationFragmentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        listener()
        viewmodel()
     }

    private fun init() {
        bottomNavigation = findViewById(R.id.nav_view)
    }

    private fun listener() {
        toolbar.setToolbar(R.id.base_toolbar)
        setBottomNavigationFragments()
        bottomNavigation.setOnItemSelectedListener {
            selectedMenu = it.itemId
            when (it.itemId) {
                R.id.navigation_home -> {
                    checkedMenu = 0
                    transaction(bottomNavigationItemList[checkedMenu].fragment)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_history -> {
                    checkedMenu = 1
                    transaction(bottomNavigationItemList[checkedMenu].fragment)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_favorite -> {
                    checkedMenu = 2
                    transaction(bottomNavigationItemList[checkedMenu].fragment)
                    return@setOnItemSelectedListener true
                }


                R.id.navigation_settings -> {
                    checkedMenu = 3
                    transaction(bottomNavigationItemList[checkedMenu].fragment)
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
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomNavigationFragments() {
        if (bottomNavigationItemList.isNullOrEmpty()) {
            bottomNavigationItemList.add(
                BottomNavigationFragmentModel(
                    0,
                    HomeFragment.newInstance(this)
                )
            )
            bottomNavigationItemList.add(
                BottomNavigationFragmentModel(
                    1,
                    HistoryFragment.newInstance(this)
                )
            )
            bottomNavigationItemList.add(
                BottomNavigationFragmentModel(
                    2,
                    FavoriteFragment.newInstance(this)
                )
            )
            bottomNavigationItemList.add(
                BottomNavigationFragmentModel(
                    3,
                    SettingsFragment.newInstance(this)
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.main_menu, menu)
        searchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    private fun transaction(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
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
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            d("newText", it)
            d("fragment", bottomNavigationItemList[checkedMenu].fragment::class.java.simpleName)
            bottomNavigationItemList[checkedMenu].fragment.onSearch(newText)
        }
        return true
    }
}