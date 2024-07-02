package io.bidmachine.qr_ad_fetcher.ad

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.explorestack.iab.CacheControl
import com.explorestack.iab.IabError
import com.explorestack.iab.mraid.MraidView
import com.explorestack.iab.mraid.MraidViewListener
import com.explorestack.iab.utils.IabClickCallback
import com.explorestack.iab.utils.Utils
import io.bidmachine.qr_ad_fetcher.Helper
import java.lang.ref.WeakReference

class BannerAd(private val adListener: Ad.Listener, adContainer: ViewGroup) : Ad {

    private val weakReference: WeakReference<ViewGroup> = WeakReference(adContainer)

    private var view: MraidView? = null

    override fun loadAd(context: Context, adm: String) {
        view = MraidView.Builder()
                .setCacheControl(CacheControl.FullLoad)
                .setListener(Listener(context, adListener))
                .build(context)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(Utils.dpToPx(context, 320F),
                                                          Utils.dpToPx(context, 50F))
                    load(adm)
                }
    }

    override fun showAd(activity: Activity) {
        val viewGroup = weakReference.get()
        if (viewGroup != null && view != null) {
            viewGroup.visibility = View.VISIBLE
            viewGroup.removeAllViews()
            viewGroup.addView(view)
            view!!.show(activity)
        } else {
            adListener.onAdFailedToShown()
        }
    }

    override fun destroy() {
        view?.apply {
            if (parent is ViewGroup) {
                (parent as ViewGroup).removeAllViews()
            }
            destroy()
        }
        view = null
    }


    private class Listener(private val context: Context, private val listener: Ad.Listener) : MraidViewListener {

        override fun onLoaded(mraidView: MraidView) {
            listener.onAdLoaded()
        }

        override fun onLoadFailed(mraidView: MraidView, iabError: IabError) {
            listener.onAdFailedToLoad()
        }

        override fun onShown(mraidView: MraidView) {
            listener.onAdShown()
        }

        override fun onShowFailed(mraidView: MraidView, iabError: IabError) {
            listener.onAdFailedToShown()
        }

        override fun onExpired(mraidView: MraidView, iabError: IabError) {
            listener.onAdExpired()
        }

        override fun onOpenBrowser(mraidView: MraidView, url: String, callback: IabClickCallback) {
            listener.onAdClicked()

            Helper.openBrowser(context, url) {
                callback.clickHandled()
            }
        }

        override fun onClose(mraidView: MraidView) {
            listener.onAdClosed()
        }

        override fun onExpand(mraidView: MraidView) {

        }

        override fun onPlayVideo(mraidView: MraidView, url: String) {

        }

    }

}