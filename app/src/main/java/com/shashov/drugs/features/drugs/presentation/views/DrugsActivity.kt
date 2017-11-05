package com.shashov.drugs.features.drugs.presentation.views


import android.app.Activity
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.shashov.drugs.R
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.presentation.AnalogsViewModel
import com.shashov.drugs.features.drugs.presentation.hide
import com.shashov.drugs.features.drugs.presentation.show
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.banner.*
import kotlinx.android.synthetic.main.fragment_search.*


class DrugsActivity : LifecycleActivity(), SearchFragment.OpenAnalogsListener {
    private val TAG = DrugsActivity::class.java.simpleName
    private lateinit var analogsViewModel: AnalogsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DrugsApp).buildDrugsComponent()
        setContentView(R.layout.activity_main)
        analogsViewModel = ViewModelProviders.of(this).get(AnalogsViewModel::class.java)

        initAd()

        analogsViewModel.getSubstance().observe(this, Observer<String> { text ->
            if (!isTwoPanel()) {
                if (analogsViewModel.isCleared()) {
                    hideAnalogsFragment()
                } else {
                    showAnalogsFragment()
                }
            }
        })
    }

    private fun initAd() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4753930175100613~1882454432")

        if (adView != null && adView.visibility == View.VISIBLE) {
            adView.loadAd(AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("33A2300990D02B93B1E241C13945C4BA") //tablet
                    .addTestDevice("123") //TODO phone
                    .build()
            )
        }
    }

    override fun openAnalogs(item: String) {
        analogsViewModel.loadAnalogs(item)
    }

    private fun isTwoPanel(): Boolean {
        return resources.getBoolean(R.bool.twoPaneMode)
    }

    override fun onBackPressed() {
        with(supportFragmentManager) {
            if (!isTwoPanel() && (analogsLayout?.visibility == View.VISIBLE)) {
                analogsViewModel.clear()
            } else {
                super.onBackPressed()
            }
        }

    }

    private fun showAnalogsFragment() {
        hideSoftKeyboard(entryTitle)
        if (analogsLayout?.visibility != View.VISIBLE) {
            analogsLayout?.show()
            //analogsLayout?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
        }
    }

    private fun hideAnalogsFragment() {
        if (analogsLayout?.visibility != View.GONE) {
            analogsLayout?.hide()
            analogsLayout?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_down))
        }
    }

    override fun hideSoftKeyboard(view: View?) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (view != null) {
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as DrugsApp).releaseDrugsComponent()
    }

}
