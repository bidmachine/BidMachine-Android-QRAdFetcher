package io.bidmachine.qr_ad_fetcher.ad

import android.app.Activity
import android.content.Context
import com.explorestack.iab.CacheControl
import com.explorestack.iab.IabError
import com.explorestack.iab.mraid.MraidInterstitial
import com.explorestack.iab.mraid.MraidInterstitialListener
import com.explorestack.iab.mraid.MraidType
import com.explorestack.iab.utils.IabClickCallback
import io.bidmachine.qr_ad_fetcher.Helper

class InterstitialAd(private val adListener: Ad.Listener) : Ad {

    private var interstitial: MraidInterstitial? = null

    override fun loadAd(context: Context, adm: String) {
        interstitial = MraidInterstitial.newBuilder()
                .setCacheControl(CacheControl.FullLoad)
                .setListener(Listener(context, adListener))
                .build(context)
                .apply {
                    load(adm)
                }
    }

    override fun showAd(activity: Activity) {
        interstitial?.takeIf {
            it.isReady
        }?.show(activity, MraidType.Static)
            ?: adListener.onAdFailedToShown()
    }

    override fun destroy() {
        interstitial?.destroy()
        interstitial = null
    }


    private class Listener(private val context: Context, private val listener: Ad.Listener) :
            MraidInterstitialListener {

        override fun onLoaded(mraidInterstitial: MraidInterstitial) {
            listener.onAdLoaded()
        }

        override fun onLoadFailed(mraidInterstitial: MraidInterstitial, iabError: IabError) {
            listener.onAdFailedToLoad()
        }

        override fun onShown(mraidInterstitial: MraidInterstitial) {
            listener.onAdShown()
        }

        override fun onShowFailed(mraidInterstitial: MraidInterstitial, iabError: IabError) {
            listener.onAdFailedToShown()
        }

        override fun onExpired(mraidInterstitial: MraidInterstitial, iabError: IabError) {
            listener.onAdExpired()
        }

        override fun onOpenBrowser(mraidInterstitial: MraidInterstitial,
                                   url: String,
                                   callback: IabClickCallback) {
            listener.onAdClicked()

            Helper.openBrowser(context, url) {
                callback.clickHandled()
            }
        }

        override fun onClose(mraidInterstitial: MraidInterstitial) {
            listener.onAdClosed()
        }

        override fun onPlayVideo(mraidInterstitial: MraidInterstitial, url: String) {

        }

    }

}