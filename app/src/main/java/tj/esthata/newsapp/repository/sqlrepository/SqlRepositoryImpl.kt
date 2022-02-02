package tj.esthata.newsapp.repository.sqlrepository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticlesSource

class SqlRepositoryImpl(context: Context) : MySQLiteOpenHelper(context), SqlRepository {

    @SuppressLint("Range")
    override suspend fun getFavoriteNews(): ArrayList<NewResponseModelArticles> {
        val sql = "SELECT * from favorite ORDER by id DESC"
        return getNewsByQuery(sql)
    }

    @SuppressLint("Range")
    override suspend fun getHistoryNews(): ArrayList<NewResponseModelArticles> {
        val sql = "SELECT * from news ORDER by id DESC"
        return getNewsByQuery(sql)
    }

    @SuppressLint("Range")
    override suspend fun searchByHistory(q: String): ArrayList<NewResponseModelArticles> {
        val sql = "SELECT * from news WHERE title LIKE \"%$q%\""
        return getNewsByQuery(sql)
    }

    @SuppressLint("Range")
    override suspend fun searchByFavorite(q: String): ArrayList<NewResponseModelArticles> {
        val sql = "SELECT * from favorite title LIKE \"%$q%\""
        return getNewsByQuery(sql)
    }

    override suspend fun deleteFromHistory(id: Int): Boolean {
        val deleteQuery = "DELETE FROM news WHERE id = $id"
        return ExecuteWithResult(deleteQuery)
    }

    override suspend fun deleteFromFavorite(id: Int): Boolean {
        val deleteQuery = "DELETE FROM favorite WHERE id = $id"
        return ExecuteWithResult(deleteQuery)
    }

    @SuppressLint("Range")
    fun getNewsByQuery(query: String): ArrayList<NewResponseModelArticles> {
        val arrayList = ArrayList<NewResponseModelArticles>()
        val mCursor: Cursor? = Query(query)
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    val id = mCursor.getInt(mCursor.getColumnIndex("id"))
                    val sourceId = mCursor.getString(mCursor.getColumnIndex("source"))
                    var source: NewResponseModelArticlesSource? = null
                    val sourceSql = "SELECT * FROM source WHERE id_source = $sourceId"
                    val sourceCursor: Cursor? = Query(sourceSql)
                    if (sourceCursor != null) {
                        if (sourceCursor.moveToFirst()) {
                            val sourceid = sourceCursor.getString(sourceCursor.getColumnIndex("id"))
                            val name =
                                sourceCursor.getString(sourceCursor.getColumnIndex("name"))
                            source = NewResponseModelArticlesSource(sourceid, name)
                        }
                        sourceCursor.close()
                    }
                    val author = mCursor.getString(mCursor.getColumnIndex("author"))
                    val title = mCursor.getString(mCursor.getColumnIndex("title"))
                    val desc = mCursor.getString(mCursor.getColumnIndex("description"))
                    val url = mCursor.getString(mCursor.getColumnIndex("url"))
                    val urlToImage = mCursor.getString(mCursor.getColumnIndex("urlToImage"))
                    val publishedAt = mCursor.getString(mCursor.getColumnIndex("publishedAt"))
                    val content = mCursor.getString(mCursor.getColumnIndex("content"))
                    arrayList.add(
                        NewResponseModelArticles(
                            id,
                            source,
                            author,
                            title,
                            desc,
                            url,
                            urlToImage,
                            publishedAt,
                            content
                        )
                    )
                } while (mCursor.moveToNext())
            }
            mCursor.close()
        }
        return arrayList
    }

    @SuppressLint("Range")
    private fun getLatestId(query: String, id: String): Int? {
        var latestId: Int? = null
        val mCursor = Query(query)
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    latestId = mCursor.getInt(mCursor.getColumnIndex(id))
                } while (mCursor.moveToNext())
            }
            mCursor.close()
        }
        return latestId
    }

    override suspend fun setFavorite(item: NewResponseModelArticles): Boolean {
        val getLatestId = "SELECT id_source from source ORDER by ROWID DESC LIMIT 1"
        val latestID = getLatestId(getLatestId, "id_source")
        val sql =
            "INSERT OR REPLACE INTO favorite(author,content, description, publishedAt,source,title,url,urlToImage,isFavorite) VALUES(\"${item.author ?: ""}\",\"${item.content ?: ""}\",\"${item.description ?: ""}\",\"${item.publishedAt ?: ""}\",${latestID ?: ""},\"${item.title ?: ""}\",\"${item.url ?: ""}\",\"${item.urlToImage ?: ""}\",1);"
        return ExecuteWithResult(sql)
    }

    override suspend fun setHistory(item: NewResponseModelArticles): Boolean {
        val sourceSql =
            "INSERT OR REPLACE INTO source(id,name) VALUES(\"${item.source?.id ?: ""}\",\"${item.source?.name ?: ""}\");"
        Execute(sourceSql)
        val getLatestIdInSource = "SELECT id_source from source ORDER by ROWID DESC LIMIT 1"
        val latestID = getLatestId(getLatestIdInSource, "id_source")
        val sql =
            "INSERT OR REPLACE INTO news(author,content, description, publishedAt,source,title,url,urlToImage,isFavorite) VALUES(\"${item.author ?: ""}\",\"${item.content ?: ""}\",\"${item.description ?: ""}\",\"${item.publishedAt ?: ""}\",${latestID},\"${item.title ?: ""}\",\"${item.url ?: ""}\",\"${item.urlToImage ?: ""}\",0);"
        return ExecuteWithResult(sql)
    }

}