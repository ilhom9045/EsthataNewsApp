package tj.esthata.newsapp.modules.mian.home.newsdetails.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragment
import tj.esthata.newsapp.modules.mian.ui.callback.OnToolbarChangeListener
import tj.esthata.newsapp.modules.mian.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.others.d

class NewsDetailsFragment : BaseFragment(R.layout.news_details) {

    private var onToolbarChangeListener: OnToolbarChangeListener? = null
    private var newsResponseModelArticles: NewResponseModelArticles? = null

    private lateinit var imageView: AppCompatImageView
    private lateinit var title: TextView
    private lateinit var decs: TextView
    private lateinit var body: WebView
    private lateinit var url: WebView
    private lateinit var published: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listener()
        viewmodel()
    }

    private fun viewmodel() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun listener() {

        onToolbarChangeListener?.clearMenu()
        onToolbarChangeListener?.setToolbarTitle(resources.getString(R.string.news_details_title))
        onToolbarChangeListener?.setDisplayHomeEnable(true)

        body.settings.javaScriptEnabled = true
        body.settings.defaultTextEncodingName = "utf-8"
        newsResponseModelArticles?.content?.let {
            body.loadDataWithBaseURL(
                null,
                it, "text/html", "utf-8", null
            )
        }
        title.text = newsResponseModelArticles?.title
        decs.text = newsResponseModelArticles?.description
        url.settings.javaScriptEnabled = true
        newsResponseModelArticles?.url?.let {
            url.loadDataWithBaseURL(
                null,
                "<a href='${it}'>${it}</a>", "text/html", "utf-8", null
            )
        }
        published.text = newsResponseModelArticles?.publishedAt
        newsResponseModelArticles?.urlToImage?.let {
            Glide.with(requireContext()).load(it).into(imageView)
        }
    }

    private fun init(v: View) {
        imageView = v.findViewById(R.id.news_details_imageView)
        title = v.findViewById(R.id.news_details_titleTextView)
        decs = v.findViewById(R.id.news_details_descTextView)
        body = v.findViewById(R.id.news_details_bodyWebView)
        url = v.findViewById(R.id.news_details_urlWebView)
        published = v.findViewById(R.id.news_details_publishedTextView)
    }

    override fun onSearch(q: String?) {

    }

    companion object {
        fun newInstance(
            onToolbarChangeListener: OnToolbarChangeListener,
            items: NewResponseModelArticles
        ) = NewsDetailsFragment().apply {
            this.onToolbarChangeListener = onToolbarChangeListener
            this.newsResponseModelArticles = items
        }
    }

}