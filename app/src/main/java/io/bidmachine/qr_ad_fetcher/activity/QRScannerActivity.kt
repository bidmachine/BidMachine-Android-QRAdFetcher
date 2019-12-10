package io.bidmachine.qr_ad_fetcher.activity

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import io.bidmachine.qr_ad_fetcher.AdType
import io.bidmachine.qr_ad_fetcher.R
import kotlinx.android.synthetic.main.activity_qr_scanner.*
import java.net.URL

class QRScannerActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {

    companion object {

        private const val BUNDLE_AD_TYPE = "bundle_ad_type"

        fun getNewIntent(context: Context, adType: AdType): Intent {
            val intent = Intent(context, QRScannerActivity::class.java)
            intent.putExtra(BUNDLE_AD_TYPE, adType)
            return intent
        }

    }

    private lateinit var adType: AdType
    private var isQrCodeRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        adType = intent.getSerializableExtra(BUNDLE_AD_TYPE) as AdType

        qrCodeScanner.setQRDecodingEnabled(true)
        qrCodeScanner.setAutofocusInterval(2000)
        qrCodeScanner.setBackCamera()
        qrCodeScanner.setOnQRCodeReadListener(this)
    }

    override fun onResume() {
        super.onResume()
        isQrCodeRead = false
        qrCodeScanner.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrCodeScanner.stopCamera()
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        if (isQrCodeRead) {
            return
        }
        text?.apply {
            val url = getUrl(this)
            if (url != null) {
                isQrCodeRead = true
                val intent = AdManagerActivity.getNewIntent(
                    this@QRScannerActivity,
                    adType,
                    url
                )
                startActivity(intent)
            }
        }
    }

    private fun getUrl(value: String): URL? {
        return try {
            URL(value)
        } catch (e: Exception) {
            null
        }
    }

}