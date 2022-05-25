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

class QRScannerActivity : BindingActivity<ActivityQrScannerBinding>(), QRCodeReaderView.OnQRCodeReadListener {

    companion object {
        private const val BUNDLE_AD_TYPE = "bundle_ad_type"

        fun createIntent(context: Context, adType: AdType): Intent {
            return Intent(context, QRScannerActivity::class.java).apply {
                putExtra(BUNDLE_AD_TYPE, adType)
            }
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

        text?.let {
            getUrl(text)
        }?.let {
            AdManagerActivity.createIntent(this@QRScannerActivity, adType, it)
        }?.also {
            isQrCodeRead = true
            startActivity(it)
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