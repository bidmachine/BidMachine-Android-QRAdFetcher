package io.bidmachine.qr_ad_fetcher.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import io.bidmachine.qr_ad_fetcher.AdType
import io.bidmachine.qr_ad_fetcher.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {

        fun getNewIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bBanner.setOnClickListener {
            openQRScanner(AdType.Banner)
        }
        bInterstitial.setOnClickListener {
            openQRScanner(AdType.Interstitial)
        }
        bVideo.setOnClickListener {
            openQRScanner(AdType.Video)
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            100
        )
    }

    private fun openQRScanner(adType: AdType) {
        val intent = QRScannerActivity.getNewIntent(this, adType)
        startActivity(intent)
    }

}