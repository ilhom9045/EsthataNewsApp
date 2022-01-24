package tj.esthata.newsapp.modules.main.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import tj.esthata.newsapp.modules.main.settings.model.SettingsCategoryModel
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModel
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.others.MyLiveData
import tj.esthata.newsapp.others.MyMutableLiveData

class MainViewModel : BaseViewModel() {

    private val mSettingsCategory = MutableLiveData<ArrayList<SettingsCategoryModel>>().apply {
        val items = ArrayList<SettingsCategoryModel>()
        items.add(SettingsCategoryModel("Sport", "sport", "en", "us"))
        items.add(SettingsCategoryModel("Business Insider", "business", "en", "us"))
        items.add(SettingsCategoryModel("Buzzfeed", "entertainment", "en", "us"))
        items.add(SettingsCategoryModel("Crypto Coins News", "technology", "en", "us"))
        this.value = items
    }

    val settingsCategory: LiveData<ArrayList<SettingsCategoryModel>> = mSettingsCategory

    private val mHistory = MutableLiveData<ArrayList<NewResponseModelArticles>>()
    val history: LiveData<ArrayList<NewResponseModelArticles>> = mHistory

    private val mFavorite = MutableLiveData<ArrayList<NewResponseModelArticles>>()
    val favorite: LiveData<ArrayList<NewResponseModelArticles>> = mFavorite

    private val mNewsResponse = MyMutableLiveData<NewResponseModel>()
    val newsResponse: MyLiveData<NewResponseModel> = mNewsResponse

    private val mNewsMutableLiveDataHash: HashMap<String, MyMutableLiveData<NewResponseModel>> =
        HashMap()

    val newsLivedataMap: Map<String, MyLiveData<NewResponseModel>> = mNewsMutableLiveDataHash

    fun addToHash(key: String) {
        val get = mNewsMutableLiveDataHash[key]
        if (get == null) {
            mNewsMutableLiveDataHash[key] = MyMutableLiveData()
        }
    }

    fun removeFromHash(key: String) {
        val get = mNewsMutableLiveDataHash[key]
        get?.let {
            mNewsMutableLiveDataHash.remove(key)
        }
    }

    fun getNewsByCategory(category: String, key: String) {
        mNewsMutableLiveDataHash[key]?.let {
            asynsRequest(it) {
                network.getApi().getNews(category)
            }
        }
    }

    fun searchByTitle(qInTitle: String, key: String) {
        mNewsMutableLiveDataHash[key]?.let {
            asynsRequest(it) {
                network.getApi().search(qInTitle)
            }
        }
    }

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

    fun insertToFavorite(item: NewResponseModelArticles) {
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

    fun deleteFromHistory(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sqlRepository.deleteFromHistory(id)
            mHistory.postValue(sqlRepository.getHistoryNews())
        }
    }

    fun deleteFromFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sqlRepository.deleteFromFavorite(id)
            mFavorite.postValue(sqlRepository.getFavoriteNews())
        }
    }

}