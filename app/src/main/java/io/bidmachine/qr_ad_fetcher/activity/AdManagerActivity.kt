package io.bidmachine.qr_ad_fetcher.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.bidmachine.qr_ad_fetcher.AdType
import io.bidmachine.qr_ad_fetcher.Helper
import io.bidmachine.qr_ad_fetcher.NetworkRequest
import io.bidmachine.qr_ad_fetcher.R
import io.bidmachine.qr_ad_fetcher.ad.Ad
import io.bidmachine.qr_ad_fetcher.ad.BannerAd
import io.bidmachine.qr_ad_fetcher.ad.InterstitialAd
import io.bidmachine.qr_ad_fetcher.ad.VideoAd
import kotlinx.android.synthetic.main.activity_ad_manager.*
import java.net.URL

class AdManagerActivity : AppCompatActivity(), NetworkRequest.Listener, Ad.Listener {

    companion object {

        private const val BUNDLE_AD_TYPE = "bundle_ad_type"
        private const val BUNDLE_URL = "bundle_url"

        fun getNewIntent(context: Context, adType: AdType, url: URL): Intent {
            val intent = Intent(context, AdManagerActivity::class.java)
            intent.putExtra(BUNDLE_AD_TYPE, adType)
            intent.putExtra(BUNDLE_URL, url)
            return intent
        }

    }

    private lateinit var adType: AdType
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_manager)

        adType = intent.getSerializableExtra(BUNDLE_AD_TYPE) as AdType
        val url = intent.getSerializableExtra(BUNDLE_URL) as URL

        toolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.home) {
                val intent = MainActivity.getNewIntent(this)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
        bLoad.setOnClickListener {
            loadAd(url)
        }
        bShow.setOnClickListener {
            showAd()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyAd()
        NetworkRequest.clear()
    }

    private fun loadAd(url: URL) {
        bShow.isEnabled = false
        progressBar.visibility = View.VISIBLE
        destroyAd()
        NetworkRequest.getBodyByUrl(url, this)
    }

    private fun prepareAd(adType: AdType, adm: String) {
        when (adType) {
            AdType.Banner -> BannerAd(this, adContainer)
            AdType.Interstitial -> InterstitialAd(this)
            AdType.Video -> VideoAd(this)
        }.apply {
            ad = this
            loadAd(this@AdManagerActivity, adm)
        }
    }

    private fun showAd() {
        if (ad != null) {
            ad!!.showAd(this)
        } else {
            Helper.showToast(this, "Ad Object is null")
        }
    }

    private fun destroyAd() {
        ad?.apply {
            destroy()
        }
    }

    override fun onSuccess(body: String) {
        prepareAd(adType, body)
    }

    override fun onError() {
        bShow.isEnabled = false
        progressBar.visibility = View.INVISIBLE
        Helper.showToast(this, "Can't load creative by url")
    }

    override fun onAdLoaded() {
        bShow.isEnabled = true
        progressBar.visibility = View.INVISIBLE
        Helper.showToast(this, "Ad Loaded")
    }

    override fun onAdFailedToLoad() {
        bShow.isEnabled = false
        progressBar.visibility = View.INVISIBLE
        Helper.showToast(this, "Ad Failed To Load")
    }

    override fun onAdShown() {
        Helper.showToast(this, "Ad Shown")
    }

    override fun onAdFailedToShown() {
        Helper.showToast(this, "Ad Failed To Shown")
    }

    override fun onAdClicked() {
        Helper.showToast(this, "Ad Clicked")
    }

    override fun onAdClosed() {
        Helper.showToast(this, "Ad Closed")
    }

}