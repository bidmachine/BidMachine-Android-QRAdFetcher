package io.bidmachine.qr_ad_fetcher.ad

import android.content.Context
import com.explorestack.iab.utils.Utils
import io.bidmachine.qr_ad_fetcher.Helper

interface Ad {

    fun loadAd(context: Context, adm: String)
    fun showAd(context: Context)
    fun destroy()

    interface Listener {
        fun onAdLoaded()
        fun onAdFailedToLoad()
        fun onAdShown()
        fun onAdFailedToShown()
        fun onAdClicked()
        fun onAdClosed()
    }

}