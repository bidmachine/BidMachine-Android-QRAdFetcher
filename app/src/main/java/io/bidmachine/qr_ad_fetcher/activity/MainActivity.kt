package io.bidmachine.qr_ad_fetcher.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import io.bidmachine.qr_ad_fetcher.AdType
import io.bidmachine.qr_ad_fetcher.databinding.ActivityMainBinding

class MainActivity : BindingActivity<ActivityMainBinding>() {

    companion object {

        fun getNewIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun inflate(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bBanner.setOnClickListener {
            openQRScanner(AdType.Banner)
        }
        binding.bInterstitial.setOnClickListener {
            openQRScanner(AdType.Interstitial)
        }
        binding.bVideo.setOnClickListener {
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