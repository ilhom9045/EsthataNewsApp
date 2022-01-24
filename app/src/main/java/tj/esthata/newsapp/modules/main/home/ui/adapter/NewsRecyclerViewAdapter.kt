package tj.esthata.newsapp.modules.main.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.esthata.newsapp.R
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles

class NewsRecyclerViewAdapter :
    RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    private var items = ArrayList<NewResponseModelArticles>()

    //    private val diffCallback = DiffCallback()
//    private val differ = AsyncListDiffer(this, diffCallback)
    private var onNewRecyclerViewCallBack: NewRecyclerViewCallBack? = null

    fun setCallBack(onNewRecyclerViewCallBack: NewRecyclerViewCallBack) {
        this.onNewRecyclerViewCallBack = onNewRecyclerViewCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun removeAllItems() {
        val oldSize = ArrayList<NewResponseModelArticles>(items).size
        items.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    fun setItems(items: ArrayList<NewResponseModelArticles>) {
        if (items.isNullOrEmpty()) {
            removeAllItems()
            return
        }
        this.items = items
        notifyItemRangeChanged(0, itemCount)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val newsImageView: ImageView = v.findViewById(R.id.newsImageView)
        private val newsTitleTextView: TextView = v.findViewById(R.id.newsTitleTextView)
        private val newsDescTextView: TextView = v.findViewById(R.id.newsDescTextView)
        private val newsContainer: CardView = v.findViewById(R.id.newsContainer)

        fun bind(item: NewResponseModelArticles) {
            item.urlToImage?.let {
                Glide.with(newsImageView).load(it).into(newsImageView)
            }
            newsTitleTextView.text = item.title
            newsDescTextView.text = item.description

            newsContainer.setOnClickListener {
                onNewRecyclerViewCallBack?.onItemClickListener(item)
            }

            newsContainer.setOnLongClickListener {
                onNewRecyclerViewCallBack?.onLongItemClickListener(item, it)
                true
            }
        }
    }

    private inner class DiffCallback : DiffUtil.ItemCallback<NewResponseModelArticles>() {
        override fun areItemsTheSame(
            oldItem: NewResponseModelArticles,
            newItem: NewResponseModelArticles
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NewResponseModelArticles,
            newItem: NewResponseModelArticles
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface NewRecyclerViewCallBack {
        fun onItemClickListener(item: NewResponseModelArticles)
        fun onLongItemClickListener(item: NewResponseModelArticles, v: View)
    }
}