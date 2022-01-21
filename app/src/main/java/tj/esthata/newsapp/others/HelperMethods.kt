package tj.esthata.newsapp.others

import android.content.Context
import android.util.Log
import android.widget.Toast
import tj.esthata.newsapp.BuildConfig

fun d(message: String) {
    d("null", message)
}

fun d(TAG: String?, message: String) {
    d(TAG, message, null)
}

fun d(TAG: String?, message: String, exception: Throwable?) {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, message, exception)
    }
}

fun e(message: String) {
    e("null", message)
}

fun e(TAG: String?, message: String) {
    e(TAG, message, null)
}

fun e(TAG: String?, message: String, exception: Throwable?) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, message, exception)
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}