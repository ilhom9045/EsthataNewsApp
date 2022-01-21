package tj.esthata.newsapp.modules.mian.home.ui.vm

import tj.esthata.newsapp.core.viewmodel.BaseViewModel
import tj.esthata.newsapp.modules.mian.ui.model.NewResponseModel
import tj.esthata.newsapp.others.MyLiveData
import tj.esthata.newsapp.others.MyMutableLiveData

class ViewPagerFragmentsViewModel : BaseViewModel() {

    private val mNewsResponse = MyMutableLiveData<NewResponseModel>()
    val newsResponse: MyLiveData<NewResponseModel> = mNewsResponse

    fun getNewsByCategory(category: String) {
        asynsRequest(mNewsResponse) {
            network.getApi().getNews(category)
        }
    }

    fun searchByTitle(qInTitle: String) {
        asynsRequest(mNewsResponse) {
            network.getApi().search(qInTitle)
        }
    }


}