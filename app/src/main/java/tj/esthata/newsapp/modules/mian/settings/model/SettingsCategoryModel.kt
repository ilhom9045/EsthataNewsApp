package tj.esthata.newsapp.modules.mian.settings.model

import com.google.gson.annotations.SerializedName

data class SettingsCategoryModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("country")
    val country: String,
    var isChecked:Boolean = true
)
