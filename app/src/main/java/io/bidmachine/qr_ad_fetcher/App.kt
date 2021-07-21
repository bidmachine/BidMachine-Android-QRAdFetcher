package io.bidmachine.qr_ad_fetcher

import android.app.Application
import android.webkit.WebView

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        WebView.setWebContentsDebuggingEnabled(true)
    }

}