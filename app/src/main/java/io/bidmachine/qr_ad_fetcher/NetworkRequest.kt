package io.bidmachine.qr_ad_fetcher

import io.bidmachine.iab.utils.Utils
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicBoolean

class NetworkRequest {

    companion object {
        private var networkTask: NetworkTask? = null

        fun getBodyByUrl(url: URL, listener: Listener) {
            clear()
            networkTask = NetworkTask(url, listener).apply {
                execute()
            }
        }

        fun clear() {
            networkTask?.cancel()
            networkTask = null
        }
    }


    private class NetworkTask(private val url: URL, private var listener: Listener) : Runnable {

        companion object {
            private val executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)
        }

        private val isCanceled = AtomicBoolean(false)

        private var future: Future<*>? = null

        override fun run() {
            var result: String? = null
            var httpURLConnection: HttpURLConnection? = null
            try {
                httpURLConnection = url.openConnection() as HttpURLConnection?
                result = httpURLConnection?.inputStream?.bufferedReader()?.readText()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpURLConnection?.disconnect()
            }
            if (isCanceled.get()) {
                return
            }
            Utils.onUiThread {
                if (result.isNullOrEmpty()) {
                    listener.onError()
                } else {
                    listener.onSuccess(result)
                }
            }
        }

        fun execute() {
            future = executor.submit(this)
        }

        fun cancel() {
            isCanceled.set(true)
            future?.cancel(true)
        }

    }

    interface Listener {

        fun onSuccess(body: String)

        fun onError()

    }

}