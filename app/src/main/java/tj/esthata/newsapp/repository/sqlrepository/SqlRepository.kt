package tj.esthata.newsapp.repository.sqlrepository

import tj.esthata.newsapp.modules.mian.ui.model.NewResponseModelArticles

interface SqlRepository {
    suspend fun getFavoriteNews(): ArrayList<NewResponseModelArticles>
    suspend fun getHistoryNews(): ArrayList<NewResponseModelArticles>
    suspend fun setFavorite(item: NewResponseModelArticles)
    suspend fun setHistory(item: NewResponseModelArticles)
    suspend fun searchByHistory(q: String): ArrayList<NewResponseModelArticles>
    suspend fun searchByFavorite(q: String): ArrayList<NewResponseModelArticles>
    suspend fun deleteFromHistory(id: Int)
    suspend fun deleteFromFavorite(id: Int)
}