package tj.esthata.newsapp.repository.sqlrepository

import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles

interface SqlRepository {
    suspend fun getFavoriteNews(): ArrayList<NewResponseModelArticles>
    suspend fun getHistoryNews(): ArrayList<NewResponseModelArticles>
    suspend fun setFavorite(item: NewResponseModelArticles): Boolean
    suspend fun setHistory(item: NewResponseModelArticles): Boolean
    suspend fun searchByHistory(q: String): ArrayList<NewResponseModelArticles>
    suspend fun searchByFavorite(q: String): ArrayList<NewResponseModelArticles>
    suspend fun deleteFromHistory(id: Int): Boolean
    suspend fun deleteFromFavorite(id: Int): Boolean
}