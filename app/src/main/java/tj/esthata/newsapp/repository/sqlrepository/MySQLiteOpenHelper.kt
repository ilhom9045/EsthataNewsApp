package tj.esthata.newsapp.repository.sqlrepository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

abstract class MySQLiteOpenHelper(private val context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null,
    DB_VERSION
) {
    @SuppressLint("SdCardPath")
    private var DB_PATH: String = "/data/data/" + context.packageName + "/databases/"
    private var mDataBase: SQLiteDatabase? = null

    init {
        val file = File(DB_PATH)
        if (file.exists() && file.isDirectory) {
            Log.e(TAG, "Directory Exists")
        } else {
            Log.e(TAG, "Creating directory")
            file.mkdir()
        }
        try {
            createDataBase()
            open()
        } catch (ex: Exception) {
            throw Error("ErrorCopyingDataBase")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists()) {
                try {
                    dbFile.delete()
                    copyDataBase()
                    open()
                } catch (ex: Exception) {
                    throw Error("ErrorCopyingDataBase")
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createDataBase() {
        //If the database does not exist, copy it from the assets.
        if (!checkDataBase()) {
            this.readableDatabase
            close()
            try {
                copyDataBase()
                Log.e(TAG, "createDatabase database created")
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    //Copy the database from assets
    @Throws(IOException::class)
    private fun copyDataBase() {
        try {
            val mInput = context.assets.open(DB_NAME)
            val outFileName = DB_PATH + DB_NAME
            val mOutput: OutputStream = FileOutputStream(outFileName)
            val mBuffer = ByteArray(1024)
            var mLength: Int
            while (mInput.read(mBuffer).also { mLength = it } > 0) {
                mOutput.write(mBuffer, 0, mLength)
            }
            mOutput.flush()
            mOutput.close()
            mInput.close()
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }

    @Throws(SQLException::class)
    fun open(): Boolean {
        try {
            mDataBase =
                SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
        return mDataBase != null
    }

    fun Execute(sql: String?) {
        try {
            mDataBase?.execSQL(sql)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }

    fun ExecuteWithResult(sql: String?): Boolean {
        return try {
            mDataBase?.execSQL(sql)
            true
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
            false
        }
    }

    fun Query(sql: String?): Cursor? {
        var cur: Cursor? = null
        try {
            cur = mDataBase?.rawQuery(sql, null)
        } catch (ex: Exception) {
            val message = ex.message
            Log.e(TAG, message!!)
        }
        return cur
    }

    @Synchronized
    override fun close() {
        try {
            if (mDataBase != null) mDataBase!!.close()
            super.close()
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }

    companion object {
        private const val TAG = "MySQLiteOpenHelper"
        private const val DB_NAME = "news.db"
        private const val DB_VERSION = 1
    }
}