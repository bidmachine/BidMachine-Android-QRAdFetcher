package io.bidmachine.qr_ad_fetcher

import android.app.Application
import android.webkit.WebView
import io.bidmachine.iab.mraid.MraidLog
import io.bidmachine.iab.utils.Logger
import io.bidmachine.iab.vast.VastLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        WebView.setWebContentsDebuggingEnabled(true)

        MraidLog.setLoggingLevel(Logger.LogLevel.debug)
        VastLog.setLoggingLevel(Logger.LogLevel.debug)
    }

}