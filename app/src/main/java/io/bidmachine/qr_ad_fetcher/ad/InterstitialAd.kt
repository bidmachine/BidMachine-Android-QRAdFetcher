package io.bidmachine.qr_ad_fetcher.ad

import android.app.Activity
import android.content.Context
import com.explorestack.iab.mraid.MraidActivity
import com.explorestack.iab.mraid.MraidError
import com.explorestack.iab.mraid.MraidInterstitial
import com.explorestack.iab.mraid.MraidInterstitialListener
import com.explorestack.iab.utils.IabClickCallback
import io.bidmachine.qr_ad_fetcher.Helper

class InterstitialAd(private val adListener: Ad.Listener) : Ad {

    private var interstitial: MraidInterstitial? = null

    override fun loadAd(context: Context, adm: String) {
        interstitial = MraidInterstitial.newBuilder()
            .setPreload(true)
            .setListener(Listener(context, adListener))
            .build(context)
            .apply {
                load(adm)
            }
    }

    override fun showAd(activity: Activity) {
        interstitial?.takeIf {
            it.isReady
        }?.show(activity, MraidActivity.MraidType.Static) ?: adListener.onAdFailedToShown()
    }

    override fun destroy() {
        interstitial?.destroy()
        interstitial = null
    }

    private class Listener(private val context: Context, private val listener: Ad.Listener)
        : MraidInterstitialListener {

        override fun onLoaded(mraidInterstitial: MraidInterstitial) {
            listener.onAdLoaded()
        }

        override fun onError(mraidInterstitial: MraidInterstitial, errorCode: Int) {
            if (errorCode == MraidError.SHOW_ERROR) {
                listener.onAdFailedToShown()
            } else {
                listener.onAdFailedToLoad()
            }
        }

        override fun onShown(mraidInterstitial: MraidInterstitial) {
            listener.onAdShown()
        }

        override fun onOpenBrowser(mraidInterstitial: MraidInterstitial,
                                   url: String,
                                   callback: IabClickCallback) {
            listener.onAdClicked()

            Helper.openBrowser(context, url)
        }

        override fun onClose(mraidInterstitial: MraidInterstitial) {
            listener.onAdClosed()
        }

        override fun onPlayVideo(mraidInterstitial: MraidInterstitial, url: String) {

        }

    }

}