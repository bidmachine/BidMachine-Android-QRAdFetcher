package io.bidmachine.qr_ad_fetcher

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.bidmachine.rendering.utils.UrlHandler
import io.bidmachine.util.SafeExecutable

object Helper {

    private const val TAG = "QRAdFetcher"

    fun showToast(context: Context, message: String) {
        Log.d(TAG, message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun openBrowser(context: Context?, url: String?, postBack: Runnable?) {
        context?.let {
            if (url.isNullOrEmpty()) {
                showToast(it, "Url is empty")
            } else {
                UrlHandler.openUrl(it, url, object : SafeExecutable<Boolean> {
                    override fun onExecute(t: Boolean) {
                        postBack?.run()
                    }
                })
            }
        } ?: Log.d(TAG, "openBrowser error. Context is null")
    }

}