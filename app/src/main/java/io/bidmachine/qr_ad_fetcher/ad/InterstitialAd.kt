package io.bidmachine.qr_ad_fetcher.ad

import android.content.Context
import android.webkit.WebView
import com.explorestack.iab.mraid.MRAIDInterstitial
import com.explorestack.iab.mraid.MRAIDInterstitialListener
import com.explorestack.iab.mraid.MRAIDNativeFeatureListener
import com.explorestack.iab.mraid.activity.MraidActivity
import io.bidmachine.qr_ad_fetcher.Helper

class InterstitialAd(private val adListener: Ad.Listener) : Ad {

    private lateinit var listener: Listener

    private var interstitial: MRAIDInterstitial? = null

    override fun loadAd(context: Context, adm: String) {
        listener = Listener(context, adListener)
        interstitial = MRAIDInterstitial.newBuilder(context, adm, 320, 480)
            .setPreload(true)
            .setListener(listener)
            .setNativeFeatureListener(listener)
            .build()
            .apply {
                load()
            }
    }

    override fun showAd(context: Context) {
        if (interstitial != null && interstitial!!.isReady) {
            MraidActivity.show(context, interstitial, MraidActivity.MraidType.Static, listener)
        } else {
            adListener.onAdFailedToShown()
        }
    }

    override fun destroy() {
        interstitial?.destroy()
        interstitial = null
    }

    private class Listener(private val context: Context, private val listener: Ad.Listener) :
        MRAIDInterstitialListener,
        MRAIDNativeFeatureListener, MraidActivity.MraidActivityListener {

        override fun mraidInterstitialLoaded(p0: MRAIDInterstitial?) {
            listener.onAdLoaded()
        }

        override fun mraidInterstitialNoFill(p0: MRAIDInterstitial?) {
            listener.onAdFailedToLoad()
        }

        override fun mraidInterstitialShow(p0: MRAIDInterstitial?) {
            listener.onAdShown()
        }

        override fun onMraidActivityShowFailed() {

        }

        override fun mraidNativeFeatureOpenBrowser(url: String?, p1: WebView?) {
            listener.onAdClicked()
            Helper.openBrowser(context, url)
        }

        override fun mraidInterstitialHide(p0: MRAIDInterstitial?) {
            listener.onAdClosed()
        }

        override fun onMraidActivityClose() {
            listener.onAdClosed()
        }

        override fun mraidNativeFeaturePlayVideo(p0: String?) {

        }

        override fun mraidNativeFeatureCreateCalendarEvent(p0: String?) {

        }

        override fun mraidNativeFeatureStorePicture(p0: String?) {

        }

        override fun mraidNativeFeatureCallTel(p0: String?) {

        }

        override fun mraidNativeFeatureSendSms(p0: String?) {

        }

    }

}