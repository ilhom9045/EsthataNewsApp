package tj.esthata.newsapp.modules.mian.history.view

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragmentWithSharedViewModel
import tj.esthata.newsapp.modules.mian.home.newsdetails.view.NewsDetailsFragment
import tj.esthata.newsapp.modules.mian.home.ui.adapter.CustomLinearLayoutManager
import tj.esthata.newsapp.modules.mian.home.ui.adapter.NewsRecyclerViewAdapter
import tj.esthata.newsapp.modules.mian.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.mian.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.modules.mian.ui.vm.MainViewModel
import tj.esthata.newsapp.others.d

class HistoryFragment : BaseFragmentWithSharedViewModel<MainViewModel>(
    MainViewModel::class,
    R.layout.history_fragment
) {

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
        viewmodel.history.observe(viewLifecycleOwner, {
            loading.hideLoading()
            newsRecyclerViewAdapter.setItems(it)
        })

        if (savedInstanceState == null) {
            viewmodel.getHistoryNews()
        }
    }

    private fun listener() {
        loading.setLoading(R.id.base_loading_indicator).showLoading()
        onToolbarChangeListener?.setToolbarTitle(resources.getString(R.string.title_history))
        onToolbarChangeListener?.setMenu()
        onToolbarChangeListener?.setDisplayHomeEnable(false)
        newsRecyclerViewAdapter.setCallBack(object :
            NewsRecyclerViewAdapter.NewRecyclerViewCallBack {
            override fun onItemClickListener(item: NewResponseModelArticles) {
                onToolbarChangeListener?.let { NewsDetailsFragment.newInstance(it, item) }?.let {
                    transaction(R.id.main_container, it)
                }
            }

            override fun onLongItemClickListener(item: NewResponseModelArticles, v: View) {
                val popupMenu = PopupMenu(context, v)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete_from_history -> {
                            item.id?.let { it1 -> viewmodel.deleteFromHistory(it1) }
                            true
                        }
                        R.id.add_to_favorite -> {
                            viewmodel.insertToFavorite(item)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.inflate(R.menu.history_popup_menu)
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
        })

        recyclerView.layoutManager = CustomLinearLayoutManager(requireContext())
        recyclerView.adapter = newsRecyclerViewAdapter
    }

    private fun init(v: View) {
        recyclerView = v.findViewById(R.id.history_recyclerview)
        newsRecyclerViewAdapter = NewsRecyclerViewAdapter()
    }

    override fun onSearch(q: String?) {
        d("onSearch History", q.toString())
        if (!q.isNullOrEmpty()) {
            viewmodel.searchByHistory(q)
        } else {
            viewmodel.getHistoryNews()
        }
    }

    companion object {
        fun newInstance(onToolbarChangeListener: OnToolbarChangeListener) =
            HistoryFragment().apply {
                this.onToolbarChangeListener = onToolbarChangeListener
            }
    }
}