package com.shashov.drugs.features.drugs.presentation.views

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.shashov.drugs.R
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.presentation.SplashViewModel
import com.shashov.drugs.features.drugs.presentation.views.IntroActivity.Companion.PREFS_NAME
import com.shashov.drugs.features.drugs.presentation.views.IntroActivity.Companion.PREF_VERSION_CODE_KEY
import com.shashov.drugs.features.drugs.presentation.views.IntroActivity.Companion.VERSION


class SplashActivity : LifecycleActivity() {
    private val TAG = SplashActivity::class.java.simpleName

    private lateinit var splashViewModel: SplashViewModel
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DrugsApp).buildDrugsComponent()
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        splashViewModel.getLoading().observe(this, Observer<Boolean> { isLoading ->
            if (!isLoading!!) {
                this.startActivity(
                        if (prefs.getInt(PREF_VERSION_CODE_KEY, 0) >= VERSION) {
                            // start main activity
                            Intent(this, DrugsActivity::class.java)
                        } else {
                            // show intro
                            Intent(this, IntroActivity::class.java)
                        })

            }
        })
        splashViewModel.load()
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as DrugsApp).releaseDrugsComponent()
    }
}