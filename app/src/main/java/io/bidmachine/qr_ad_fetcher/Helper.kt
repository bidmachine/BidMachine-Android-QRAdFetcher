package io.bidmachine.qr_ad_fetcher

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.explorestack.iab.utils.Utils

object Helper {

    private const val TAG = "QRAdFetcher"

    fun showToast(context: Context, message: String) {
        Log.d(TAG, message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun openBrowser(context: Context?, url: String?) {
        context?.let {
            if (url.isNullOrEmpty()) {
                showToast(it, "Url is empty")
            } else {
                Utils.openBrowser(it, url, null)
            }
        } ?: Log.d(TAG, "openBrowser error. Context is null")
    }

}