package com.shashov.drugs.features.drugs.presentation.views

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.shashov.drugs.R
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.presentation.SplashViewModel


class SplashActivity : LifecycleActivity() {
    private val TAG = SplashActivity::class.java.simpleName

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DrugsApp).buildDrugsComponent()
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        splashViewModel.getLoading().observe(this, Observer<Boolean> { isLoading ->
            if (!isLoading!!) {
                // start main activity
                val myIntent = Intent(this, DrugsActivity::class.java)
                this.startActivity(myIntent)
            }
        })
        splashViewModel.load()
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as DrugsApp).releaseDrugsComponent()
    }
}