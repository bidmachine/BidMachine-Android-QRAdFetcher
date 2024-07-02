package io.bidmachine.qr_ad_fetcher.ad

import android.app.Activity
import android.content.Context

interface Ad {

    fun loadAd(context: Context, adm: String)

    fun showAd(activity: Activity)

    fun destroy()


    interface Listener {

        fun onAdLoaded()

        fun onAdFailedToLoad()

        fun onAdShown()

        fun onAdFailedToShown()

        fun onAdExpired()

        fun onAdClicked()

        fun onAdClosed()

    }

}