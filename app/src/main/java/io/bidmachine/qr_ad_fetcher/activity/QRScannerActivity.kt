package io.bidmachine.qr_ad_fetcher.activity

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import io.bidmachine.qr_ad_fetcher.AdType
import io.bidmachine.qr_ad_fetcher.databinding.ActivityQrScannerBinding
import java.net.URL

class QRScannerActivity : BindingActivity<ActivityQrScannerBinding>(),
    QRCodeReaderView.OnQRCodeReadListener {

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

    override fun inflate(inflater: LayoutInflater) = ActivityQrScannerBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adType = intent.getSerializableExtra(BUNDLE_AD_TYPE) as AdType

        binding.qrCodeScanner.apply {
            setQRDecodingEnabled(true)
            setAutofocusInterval(2000)
            setBackCamera()
            setOnQRCodeReadListener(this@QRScannerActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        isQrCodeRead = false
        binding.qrCodeScanner.startCamera()
    }

    override fun onPause() {
        super.onPause()

        binding.qrCodeScanner.stopCamera()
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