package io.bidmachine.qr_ad_fetcher

import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

class NetworkRequest {

    companion object {

        private var taskListener: Listener? = null
        private var networkTask: NetworkTask? = null

        fun getBodyByUrl(url: URL, listener: Listener) {
            clear()
            taskListener = object : Listener {
                override fun onSuccess(body: String) {
                    listener.onSuccess(body)
                }

                override fun onError() {
                    listener.onError()
                }
            }
            networkTask = NetworkTask(taskListener)
            networkTask!!.execute(url)
        }

        fun clear() {
            taskListener = null
            networkTask?.cancel(true)
            networkTask = null
        }

    }

    private class NetworkTask(private var listener: Listener?) : AsyncTask<URL, Void, String?>() {

        override fun doInBackground(vararg params: URL?): String? {
            val httpURLConnection = prepareConnection(params[0])
            try {
                return httpURLConnection?.inputStream?.bufferedReader()?.readText()
            } catch (e: Exception) {
                e.printStackTrace()
                httpURLConnection?.apply {
                    disconnect()
                }
            }
            return null
        }

        private fun prepareConnection(vararg params: URL?): HttpURLConnection? {
            if (params.isNullOrEmpty()) {
                return null
            }
            val url = params[0] ?: return null
            return try {
                url.openConnection() as HttpURLConnection
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            listener?.apply {
                if (result.isNullOrEmpty()) {
                    onError()
                } else {
                    onSuccess(result)
                }
            }
        }

    }

    interface Listener {
        fun onSuccess(body: String)
        fun onError()
    }

}