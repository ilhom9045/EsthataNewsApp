package tj.esthata.newsapp.modules.main.home.ui.view

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragmentWithSharedViewModel
import tj.esthata.newsapp.modules.main.home.newsdetails.view.NewsDetailsFragment
import tj.esthata.newsapp.modules.main.home.ui.adapter.CustomLinearLayoutManager
import tj.esthata.newsapp.modules.main.home.ui.adapter.NewsRecyclerViewAdapter
import tj.esthata.newsapp.modules.main.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticlesSource
import tj.esthata.newsapp.modules.main.ui.vm.MainViewModel
import tj.esthata.newsapp.others.ApiConfig
import tj.esthata.newsapp.others.showToast
import tj.esthata.newsapp.repository.networkrepository.event.Status

class ViewPagerFragment : BaseFragmentWithSharedViewModel<MainViewModel>(
    MainViewModel::class.java,
    R.layout.viewpager_fragment
),NewsRecyclerViewAdapter.NewRecyclerViewCallBack {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsRecyclerViewAdapter: NewsRecyclerViewAdapter
    private var category: String? = null
    private var onToolbarChangeListener: OnToolbarChangeListener? = null
    private var key: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listener()
        viewmodel()
    }

    override fun onSearch(q: String?) {
        if (!q.isNullOrEmpty()) {
            key?.let { viewmodel.searchByTitle(q, it) }
        } else {
            viewmodel()
        }
    }

    private fun viewmodel() {
        viewmodel.newsLivedataMap[key]?.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    loading.showLoading()
                    newsRecyclerViewAdapter.removeAllItems()
                }
                Status.ERROR -> {
                    loading.hideLoading()
                }
                Status.SUCCESS -> {
                    loading.hideLoading()
                    it.data?.let { response ->
                        if (response.status == ApiConfig.statusOK) {
                            newsRecyclerViewAdapter.setItems(response.articles)
                        } else {
                            response.message?.let { it1 -> requireContext().showToast(it1) }
                        }
                    }
                }
            }
        })

        viewmodel.newsResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    loading.showLoading()
                    newsRecyclerViewAdapter.removeAllItems()
                }
                Status.ERROR -> {
                    loading.hideLoading()
                }
                Status.SUCCESS -> {
                    loading.hideLoading()
                    it.data?.let { response ->
                        if (response.status == ApiConfig.statusOK) {
                            newsRecyclerViewAdapter.setItems(response.articles)
                        } else {
                            response.message?.let { it1 -> requireContext().showToast(it1) }
                        }
                    }
                }
            }
        })

        if (viewmodel.newsLivedataMap[key]?.value == null) {
            category?.let { key?.let { it1 -> viewmodel.getNewsByCategory(it, it1) } }
        }
    }

    private fun listener() {
        loading.setLoading(R.id.base_loading_indicator)
        newsRecyclerViewAdapter.setCallBack(this)
        recyclerView.layoutManager = CustomLinearLayoutManager(requireContext())
        recyclerView.adapter = newsRecyclerViewAdapter
    }

    private fun init(v: View) {
        recyclerView = v.findViewById(R.id.history_recyclerview)
        newsRecyclerViewAdapter = NewsRecyclerViewAdapter()
    }

    override fun onItemClickListener(item: NewResponseModelArticles) {
        onToolbarChangeListener?.let {
            NewsDetailsFragment.newInstance(
                onToolbarChangeListener = it,
                item
            )
        }?.let {
            viewmodel.insertToHistory(item)
            transaction(R.id.main_container, it)
        }
    }

    override fun onLongItemClickListener(item: NewResponseModelArticles, v: View) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.popup_favorite -> {
                    viewmodel.insertToFavorite(item)
                    true
                }
                else -> false
            }
        }
        popupMenu.inflate(R.menu.news_popup_menu)
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
        fun newInstance(
            onToolbarChangeListener: OnToolbarChangeListener,
            category: String,
            key: String
        ) =
            ViewPagerFragment().apply {
                this.key = key
                this.category = category
                this.onToolbarChangeListener = onToolbarChangeListener
            }
    }

}