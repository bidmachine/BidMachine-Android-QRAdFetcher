package io.bidmachine.qr_ad_fetcher.ad

import android.app.Activity
import android.content.Context
import com.explorestack.iab.CacheControl
import com.explorestack.iab.utils.IabClickCallback
import com.explorestack.iab.vast.*
import com.explorestack.iab.vast.activity.VastActivity
import io.bidmachine.qr_ad_fetcher.Helper

class VideoAd(private val adListener: Ad.Listener) : Ad {

    private lateinit var listener: Listener

    private var vastRequest: VastRequest? = null

    override fun loadAd(context: Context, adm: String) {
        listener = Listener(context, adListener)
        vastRequest = VastRequest.newBuilder()
                .setCacheControl(CacheControl.FullLoad)
                .build()
                .apply {
                    loadVideoWithData(context, adm, listener)
                }
    }

    override fun showAd(activity: Activity) {
        vastRequest?.takeIf {
            it.checkFile()
        }?.display(activity, VideoType.NonRewarded, listener)
            ?: adListener.onAdFailedToShown()
    }

    override fun destroy() {
        vastRequest = null
    }


    private class Listener(private val context: Context, private val listener: Ad.Listener) : VastRequestListener,
            VastActivityListener {

        override fun onVastLoaded(vastRequest: VastRequest) {
            listener.onAdLoaded()
        }

        override fun onVastError(context: Context, vastRequest: VastRequest, errorCode: Int) {
            if (errorCode == VastError.ERROR_CODE_SHOW_FAILED) {
                listener.onAdFailedToShown()
            } else {
                listener.onAdFailedToLoad()
            }
        }

        override fun onVastShown(vastActivity: VastActivity, vastRequest: VastRequest) {
            listener.onAdShown()
        }

        override fun onVastComplete(vastActivity: VastActivity, vastRequest: VastRequest) {

        }

        override fun onVastClick(vastActivity: VastActivity,
                                 vastRequest: VastRequest,
                                 callback: IabClickCallback,
                                 url: String?) {
            listener.onAdClicked()

            Helper.openBrowser(context, url) {
                callback.clickHandled()
            }
        }

        override fun onVastDismiss(vastActivity: VastActivity,
                                   vastRequest: VastRequest?,
                                   finished: Boolean) {
            listener.onAdClosed()
        }

    }

}