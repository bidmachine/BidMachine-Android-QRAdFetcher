package io.bidmachine.qr_ad_fetcher

import android.app.Application
import android.webkit.WebView
import com.explorestack.iab.mraid.MraidLog
import com.explorestack.iab.utils.Logger
import com.explorestack.iab.vast.VastLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        WebView.setWebContentsDebuggingEnabled(true)

        MraidLog.setLoggingLevel(Logger.LogLevel.debug)
        VastLog.setLoggingLevel(Logger.LogLevel.debug)
    }

}