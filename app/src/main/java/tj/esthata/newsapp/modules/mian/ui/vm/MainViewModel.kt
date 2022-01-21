package tj.esthata.newsapp.modules.mian.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import tj.esthata.newsapp.modules.mian.settings.model.SettingsCategoryModel
import tj.esthata.newsapp.modules.mian.ui.model.NewResponseModelArticles

class MainViewModel : BaseViewModel() {

    private val mSettingsCategory = MutableLiveData<ArrayList<SettingsCategoryModel>>().apply {
        val items = ArrayList<SettingsCategoryModel>()
        items.add(SettingsCategoryModel("Sport", "sport", "en", "us"))
        items.add(SettingsCategoryModel("Business Insider", "business", "en", "us"))
        items.add(SettingsCategoryModel("Buzzfeed", "entertainment", "en", "us"))
        items.add(SettingsCategoryModel("Crypto Coins News", "technology", "en", "us"))
        items.add(SettingsCategoryModel("Engadget", "technology", "en", "us"))
        this.value = items
    }
    val settingsCategory: LiveData<ArrayList<SettingsCategoryModel>> = mSettingsCategory

    private val mHistory = MutableLiveData<ArrayList<NewResponseModelArticles>>()
    val history: LiveData<ArrayList<NewResponseModelArticles>> = mHistory

    private val mFavorite = MutableLiveData<ArrayList<NewResponseModelArticles>>()
    val favorite: LiveData<ArrayList<NewResponseModelArticles>> = mFavorite

    fun getHistoryNews() {
        viewModelScope.launch(Dispatchers.IO) {
            mHistory.postValue(sqlRepository.getHistoryNews())
        }
    }

    fun getFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            mFavorite.postValue(sqlRepository.getFavoriteNews())
        }
    }

    fun insertToHistory(item: NewResponseModelArticles) {
        viewModelScope.launch(Dispatchers.IO) {
            sqlRepository.setHistory(item)
        }
    }

    fun insertToFavorite(item: NewResponseModelArticles,) {
        viewModelScope.launch(Dispatchers.IO) {
            sqlRepository.setFavorite(item)
        }
    }

    fun searchByFavorite(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mFavorite.postValue(sqlRepository.searchByFavorite(q))
        }
    }

    fun searchByHistory(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mHistory.postValue(sqlRepository.searchByHistory(q))
        }
    }

    fun deleteFromHistory(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            mHistory.postValue(sqlRepository.deleteFromHisotry(id))
        }
    }

    fun deleteFromFavorite(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            mFavorite.postValue(sqlRepository.deleteFromFavorite(id))
        }
    }

}