package io.bidmachine.qr_ad_fetcher.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BindingActivity<Binding : ViewBinding> : AppCompatActivity() {

    lateinit var binding: Binding

    abstract fun inflate(inflater: LayoutInflater): Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflate(layoutInflater).apply {
            binding = this
            setContentView(root)
        }
    }

}