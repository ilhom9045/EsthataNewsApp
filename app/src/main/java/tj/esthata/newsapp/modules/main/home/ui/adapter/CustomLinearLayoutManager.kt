package tj.esthata.newsapp.modules.main.home.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}