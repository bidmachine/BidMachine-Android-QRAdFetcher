package io.bidmachine.qr_ad_fetcher.ad

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.explorestack.iab.mraid.MRAIDNativeFeatureListener
import com.explorestack.iab.mraid.MRAIDView
import com.explorestack.iab.mraid.MRAIDView.builder
import com.explorestack.iab.mraid.MRAIDViewListener
import com.explorestack.iab.utils.Utils
import io.bidmachine.qr_ad_fetcher.Helper
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

class BannerAd(private val adListener: Ad.Listener, adContainer: ViewGroup) : Ad {

    private val weakReference: WeakReference<ViewGroup> = WeakReference(adContainer)

    private var view: MRAIDView? = null

    override fun loadAd(context: Context, adm: String) {
        val listener = Listener(context, adListener)
        view = builder(context, adm, 320, 50)
            .setPreload(true)
            .setListener(listener)
            .setNativeFeatureListener(listener)
            .build()
            .apply {
                val density = Utils.getScreenDensity(context)
                layoutParams = ViewGroup.LayoutParams(
                    (density * 320).roundToInt(),
                    (density * 50).roundToInt()
                )
                load()
            }
    }

    override fun showAd(context: Context) {
        val viewGroup = weakReference.get()
        if (viewGroup != null && view != null) {
            viewGroup.visibility = View.VISIBLE
            viewGroup.removeAllViews()
            viewGroup.addView(view)
            view!!.show()
        } else {
            adListener.onAdFailedToShown()
        }
    }

    override fun destroy() {
        view?.apply {
            if (parent is ViewGroup) {
                (parent as ViewGroup).removeAllViews()
            }
            setListener(null)
            setNativeFeatureListener(null)
            destroy()
        }
        view = null
    }

    private class Listener(private val context: Context, private val listener: Ad.Listener) :
        MRAIDViewListener,
        MRAIDNativeFeatureListener {

        override fun mraidViewLoaded(p0: MRAIDView?) {
            listener.onAdLoaded()
        }

        override fun mraidViewNoFill(p0: MRAIDView?) {
            listener.onAdFailedToLoad()
        }

        override fun mraidViewExpand(p0: MRAIDView?) {
            listener.onAdShown()
        }

        override fun mraidNativeFeatureOpenBrowser(url: String?, p1: WebView?) {
            listener.onAdClicked()
            Helper.openBrowser(context, url)
        }

        override fun mraidViewClose(p0: MRAIDView?) {
            listener.onAdClosed()
        }

        override fun mraidViewResize(p0: MRAIDView?, p1: Int, p2: Int, p3: Int, p4: Int): Boolean {
            return false
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