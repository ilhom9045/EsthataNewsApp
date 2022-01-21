package tj.esthata.newsapp.core.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tj.esthata.newsapp.R
import tj.esthata.newsapp.others.NativeUtil
import tj.esthata.newsapp.others.d
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

abstract class BaseActivity(@LayoutRes layout: Int) : AppCompatActivity(layout) {

    protected val toolbar: Toolbar by lazy { Toolbar() }
    private var isNetworkDialogShow = false

    protected open inner class Toolbar {

        private var base_toolbar: MaterialToolbar? = null

        fun setToolbar(@IdRes material_toolbar_id: Int): Toolbar {
            base_toolbar = findViewById(material_toolbar_id)
            setSupportActionBar(base_toolbar)
            return toolbar
        }

        fun setTitle(title: String?): Toolbar {
            supportActionBar?.title = title
            return toolbar
        }

        fun setDisplayHomeEnable(enable: Boolean): Toolbar {
            supportActionBar?.setDisplayHomeAsUpEnabled(enable)
            return toolbar
        }

        fun clearMenu() {
            base_toolbar?.menu?.clear()
        }

        fun setMenu() {
            onCreateOptionsMenu(base_toolbar?.menu)
        }
    }

    protected fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showInternetErrorConnectionDialog(call: () -> Unit) {
        if (isNetworkDialogShow) {
            return
        }
        isNetworkDialogShow = true
        val dialog = BottomSheetDialog(this, R.style.SheetDialog)
        val v = LayoutInflater.from(this).inflate(R.layout.internet_error_connection, null)
        val button: MaterialButton = v.findViewById(R.id.verify_internet_connection)
        button.setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                val isOnline = isOnline()
                d("isOnline", isOnline.toString())
                launch(Dispatchers.Main) {
                    it.isEnabled = true
                    if (isOnline) {
                        call()
                        dialog.dismiss()
                    } else {
                        showShortToast(resources.getString(R.string.internet_error_connection_isNotOnline))
                    }
                }
            }
        }
        dialog.setOnDismissListener {
            isNetworkDialogShow = false
        }
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun showInternetErrorResponse(string: String) {
        if (isNetworkDialogShow) {
            return
        }
        isNetworkDialogShow = true
        val dialog = BottomSheetDialog(this, R.style.SheetDialog)
        val v =
            LayoutInflater.from(this)
                .inflate(R.layout.internet_error_response, null)
        val buttom: Button = v.findViewById(R.id.firm_about_dialog_button)
        val webView: WebView = v.findViewById(R.id.firm_about_dialog_webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.defaultTextEncodingName = "utf-8"
        webView.loadDataWithBaseURL(null, string, "text/html", "utf-8", null)
        buttom.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            isNetworkDialogShow = false
        }
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.create()
        dialog.show()
    }

    private suspend fun isOnline(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(NativeUtil.getBaseUrl())
                val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlConn.connectTimeout = 1000
                urlConn.connect()
                d("responseCode", urlConn.responseCode.toString())
                HttpURLConnection.HTTP_OK == urlConn.responseCode || HttpURLConnection.HTTP_FORBIDDEN == urlConn.responseCode
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }
}