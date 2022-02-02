package tj.esthata.newsapp.modules.main.favorite.view

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragmentWithSharedViewModel
import tj.esthata.newsapp.modules.main.home.newsdetails.view.NewsDetailsFragment
import tj.esthata.newsapp.modules.main.home.ui.adapter.CustomLinearLayoutManager
import tj.esthata.newsapp.modules.main.home.ui.adapter.NewsRecyclerViewAdapter
import tj.esthata.newsapp.modules.main.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.modules.main.ui.vm.MainViewModel
import tj.esthata.newsapp.others.d

class FavoriteFragment : BaseFragmentWithSharedViewModel<MainViewModel>(
    MainViewModel::class.java,
    R.layout.favorite_fragment
), NewsRecyclerViewAdapter.NewRecyclerViewCallBack {

    private var onToolbarChangeListener: OnToolbarChangeListener? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsRecyclerViewAdapter: NewsRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listener()
        viewmodel(savedInstanceState)
    }

    private fun viewmodel(savedInstanceState: Bundle?) {
        viewmodel.favorite.observe(viewLifecycleOwner, {
            loading.hideLoading()
            newsRecyclerViewAdapter.setItems(it)
        })

        if (savedInstanceState == null) {
            viewmodel.getFavorite()
        }
    }

    private fun listener() {
        onToolbarChangeListener?.setToolbarTitle(resources.getString(R.string.title_favorite))
        onToolbarChangeListener?.setMenu()
        onToolbarChangeListener?.setDisplayHomeEnable(false)
        loading.setLoading(R.id.base_loading_indicator).showLoading()
        newsRecyclerViewAdapter.setCallBack(this)

        recyclerView.layoutManager = CustomLinearLayoutManager(requireContext())
        recyclerView.adapter = newsRecyclerViewAdapter
    }

    private fun init(v: View) {
        recyclerView = v.findViewById(R.id.favorite_recyclerview)
        newsRecyclerViewAdapter = NewsRecyclerViewAdapter()
    }


    override fun onSearch(q: String?) {
        d("onSearch Favorite", q.toString())
        if (!q.isNullOrEmpty()) {
            viewmodel.searchByFavorite(q)
        } else {
            viewmodel.getFavorite()
        }
    }

    override fun onItemClickListener(item: NewResponseModelArticles) {
        onToolbarChangeListener?.let { NewsDetailsFragment.newInstance(it, item) }?.let {
            transaction(
                R.id.main_container,
                it
            )
        }
    }

    override fun onLongItemClickListener(item: NewResponseModelArticles, v: View) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_from_favorite -> {
                    item.id?.let { it1 -> viewmodel.deleteFromFavorite(it1) }
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.favorite_popup_menu)
        try {
            val field = PopupMenu::class.java.getDeclaredField("mPopup")
            field.isAccessible = true
            val mPopup = field.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            popupMenu.show()
        }
    }

    companion object {
        fun newInstance(onToolbarChangeListener: OnToolbarChangeListener) =
            FavoriteFragment().apply {
                this.onToolbarChangeListener = onToolbarChangeListener
            }
    }
}