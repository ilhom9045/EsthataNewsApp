package tj.esthata.newsapp.repository.sqlrepository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticles
import tj.esthata.newsapp.modules.main.ui.model.NewResponseModelArticlesSource

class SqlRepositoryImplTest {

    val context = mockk<Context>(relaxed = true)
    val sqlRepository: SqlRepository = SqlRepositoryImpl(context)
    val sql = object : MySQLiteOpenHelper(context) {}

    @Test
    fun getFavoriteNews() {
        val expect = sql.getNewsByQuery("SELECT * from favorite ORDER by id DESC")
        val actual = runBlocking { sqlRepository.getFavoriteNews() }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun getHistoryNews() {
        val expect = sql.getNewsByQuery("SELECT * from news ORDER by id DESC")
        val actual = runBlocking { sqlRepository.getHistoryNews() }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun searchByHistory() {
        val expect = sql.getNewsByQuery("SELECT * from news WHERE title LIKE \"%A%\"")
        val actual = runBlocking { sqlRepository.searchByHistory("A") }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun searchByFavorite() {
        val expect = sql.getNewsByQuery("SELECT * from favorite title LIKE \"%A%\"")
        val actual = runBlocking { sqlRepository.searchByFavorite("A") }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun deleteFromHistory() {
        val expect = sql.ExecuteWithResult("DELETE FROM news WHERE id = 1")
        val actual = runBlocking { sqlRepository.deleteFromHistory(1) }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun deleteFromFavorite() {
        val expect = sql.ExecuteWithResult("DELETE FROM favorite WHERE id = 1")
        val actual = runBlocking { sqlRepository.deleteFromFavorite(1) }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun setFavorite() {
        val item = NewResponseModelArticles(
            1,
            NewResponseModelArticlesSource(
                "",
                ""
            ),
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        val getLatestID = fun(query: String, id: String): Int? {
            var latestId: Int? = null
            val mCursor = sql.Query(query)
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

        val getLatestId = "SELECT id_source from source ORDER by ROWID DESC LIMIT 1"
        val latestID = getLatestID(getLatestId, "id_source")
        val sqlQuery =
            "INSERT OR REPLACE INTO favorite(author,content, description, publishedAt,source,title,url,urlToImage,isFavorite) VALUES(\"${item.author ?: ""}\",\"${item.content ?: ""}\",\"${item.description ?: ""}\",\"${item.publishedAt ?: ""}\",${latestID ?: ""},\"${item.title ?: ""}\",\"${item.url ?: ""}\",\"${item.urlToImage ?: ""}\",1);"

        val expect = sql.ExecuteWithResult(sqlQuery)
        val actual = runBlocking { sqlRepository.setFavorite(item) }
        Assertions.assertEquals(expect, actual)
    }

    @Test
    fun setHistory() {
        val item = NewResponseModelArticles(
            1,
            NewResponseModelArticlesSource(
                "",
                ""
            ),
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        val getLatestID = fun(query: String, id: String): Int? {
            var latestId: Int? = null
            val mCursor = sql.Query(query)
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

        val sourceSql =
            "INSERT OR REPLACE INTO source(id,name) VALUES(\"${item.source?.id ?: ""}\",\"${item.source?.name ?: ""}\");"
        sql.Execute(sourceSql)
        val getLatestIdInSource = "SELECT id_source from source ORDER by ROWID DESC LIMIT 1"
        val latestID = getLatestID(getLatestIdInSource, "id_source")
        val sqlQuery =
            "INSERT OR REPLACE INTO news(author,content, description, publishedAt,source,title,url,urlToImage,isFavorite) VALUES(\"${item.author ?: ""}\",\"${item.content ?: ""}\",\"${item.description ?: ""}\",\"${item.publishedAt ?: ""}\",${latestID},\"${item.title ?: ""}\",\"${item.url ?: ""}\",\"${item.urlToImage ?: ""}\",0);"
        val expect = sql.ExecuteWithResult(sqlQuery)
        val actual = runBlocking { sqlRepository.setHistory(item) }
        Assertions.assertEquals(expect, actual)
    }

    @SuppressLint("Range")
    fun MySQLiteOpenHelper.getNewsByQuery(query: String): ArrayList<NewResponseModelArticles> {
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
}