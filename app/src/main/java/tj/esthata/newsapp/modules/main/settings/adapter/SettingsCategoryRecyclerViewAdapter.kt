package tj.esthata.newsapp.modules.main.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import tj.esthata.newsapp.R
import tj.esthata.newsapp.modules.main.settings.model.SettingsCategoryModel

class SettingsCategoryRecyclerViewAdapter :
    RecyclerView.Adapter<SettingsCategoryRecyclerViewAdapter.ViewHolder>() {

    private var items = ArrayList<SettingsCategoryModel>()

    fun setItems(items: ArrayList<SettingsCategoryModel>) {
        this.items = items
        notifyItemRangeInserted(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.settings_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val checkView: MaterialCheckBox = v.findViewById(R.id.settings_checkView)

        fun bind(item: SettingsCategoryModel) {
            checkView.isChecked = item.isChecked
            checkView.text = item.name
            checkView.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
            }
        }
    }
}