package tj.esthata.newsapp.modules.main.ui.model

import com.google.gson.annotations.SerializedName

data class NewResponseModel(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    var totalResults: Int,
    @SerializedName("articles")
    var articles: ArrayList<NewResponseModelArticles>,
    @SerializedName("code")
    var code: String?,
    @SerializedName("message")
    var message: String?
)

data class NewResponseModelArticles(
    val id: Int? = 0,
    @SerializedName("source")
    var source: NewResponseModelArticlesSource?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("content")
    val content: String
)

data class NewResponseModelArticlesSource(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String
)