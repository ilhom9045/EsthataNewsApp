package tj.esthata.newsapp.modules.mian.settings.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragmentWithSharedViewModel
import tj.esthata.newsapp.modules.mian.settings.adapter.SettingsCategoryRecyclerViewAdapter
import tj.esthata.newsapp.modules.mian.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.mian.ui.vm.MainViewModel

class SettingsFragment : BaseFragmentWithSharedViewModel<MainViewModel>(
    MainViewModel::class,
    R.layout.settings_fragment
) {
    private var onToolbarChangeListener: OnToolbarChangeListener? = null
    private lateinit var recycerlview: RecyclerView
    private lateinit var settingsCategoryRecyclerViewAdapter: SettingsCategoryRecyclerViewAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onToolbarChangeListener?.setToolbarTitle(resources.getString(R.string.title_settings))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listener()
        viewmodel()
    }

    override fun onSearch(q: String?) {

    }

    private fun viewmodel() {
        viewmodel.settingsCategory.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                settingsCategoryRecyclerViewAdapter.setItems(it)
            }
        })
    }

    private fun listener() {
        recycerlview.layoutManager = LinearLayoutManager(requireContext())
        recycerlview.adapter = settingsCategoryRecyclerViewAdapter
    }

    private fun init(v: View) {
        recycerlview = v.findViewById(R.id.settings_recycerlview)
        settingsCategoryRecyclerViewAdapter = SettingsCategoryRecyclerViewAdapter()
    }

    companion object {
        fun newInstance(onToolbarChangeListener: OnToolbarChangeListener) =
            SettingsFragment().apply {
                this.onToolbarChangeListener = onToolbarChangeListener
            }
    }
}