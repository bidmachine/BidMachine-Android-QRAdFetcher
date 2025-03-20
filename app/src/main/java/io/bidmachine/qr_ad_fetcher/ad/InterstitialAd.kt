package io.bidmachine.qr_ad_fetcher.ad

import android.app.Activity
import android.content.Context
import io.bidmachine.iab.CacheControl
import io.bidmachine.iab.IabError
import io.bidmachine.iab.mraid.MraidInterstitial
import io.bidmachine.iab.mraid.MraidInterstitialListener
import io.bidmachine.iab.mraid.MraidType
import io.bidmachine.iab.utils.IabClickCallback
import io.bidmachine.qr_ad_fetcher.Helper
import io.bidmachine.rendering.model.PrivacySheetParams

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

        override fun onOpenUrl(
            mraidInterstitial: MraidInterstitial,
            url: String,
            callback: IabClickCallback
        ) {
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

        override fun onStorePicture(
            mraidInterstitial: MraidInterstitial,
            url: String,
            iabClickCallback: IabClickCallback
        ) {

        }

        override fun onCalendarEvent(
            mraidInterstitial: MraidInterstitial,
            url: String,
            iabClickCallback: IabClickCallback
        ) {

        }

        override fun onOpenPrivacySheet(
            mraidInterstitial: MraidInterstitial,
            privacySheetParams: PrivacySheetParams
        ) {

        }

    }

}