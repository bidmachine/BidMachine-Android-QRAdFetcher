package io.bidmachine.qr_ad_fetcher.ad

import android.content.Context
import com.explorestack.iab.vast.*
import com.explorestack.iab.vast.activity.VastActivity
import io.bidmachine.qr_ad_fetcher.Helper

class VideoAd(private val adListener: Ad.Listener) : Ad {

    private lateinit var listener: Listener

    private var vastRequest: VastRequest? = null

    override fun loadAd(context: Context, adm: String) {
        listener = Listener(context, adListener)
        vastRequest = VastRequest.newBuilder()
            .setPreCache(true)
            .build()
            .apply {
                loadVideoWithData(context, adm, listener)
            }
    }

    override fun showAd(context: Context) {
        if (vastRequest != null && vastRequest!!.checkFile()) {
            vastRequest?.display(context, VideoType.NonRewarded, listener)
        } else {
            adListener.onAdFailedToShown()
        }
    }

    override fun destroy() {
        vastRequest = null
    }

    private class Listener(private val context: Context, private val listener: Ad.Listener) :
        VastRequestListener,
        VastActivityListener {

        override fun onVastLoaded(p0: VastRequest) {
            listener.onAdLoaded()
        }

        override fun onVastError(p0: Context, p1: VastRequest, p2: Int) {
            listener.onAdFailedToLoad()
        }

        override fun onVastShown(p0: VastActivity, p1: VastRequest) {
            listener.onAdShown()
        }

        override fun onVastComplete(p0: VastActivity, p1: VastRequest) {

        }

        override fun onVastClick(
            p0: VastActivity,
            p1: VastRequest,
            p2: VastClickCallback,
            url: String?
        ) {
            listener.onAdClicked()
            Helper.openBrowser(context, url)
        }

        override fun onVastDismiss(p0: VastActivity, p1: VastRequest?, p2: Boolean) {
            listener.onAdClosed()
        }

    }

}