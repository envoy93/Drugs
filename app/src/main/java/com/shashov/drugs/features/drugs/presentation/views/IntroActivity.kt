package com.shashov.drugs.features.drugs.presentation.views

import android.arch.lifecycle.LifecycleActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.shashov.drugs.R


class IntroActivity : AppIntro() {

    companion object {
        const val VERSION: Int = 2
        const val PREFS_NAME: String = "com.shashov.drugs"
        const val PREF_VERSION_CODE_KEY: String = "VERSION"
    }

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREFS_NAME, LifecycleActivity.MODE_PRIVATE)
        val sliderPage1 = SliderPage()
        val sliderPage2 = SliderPage()

        with(sliderPage1) {
            title = getString(R.string.intro1)
            description = getString(R.string.intro1d)
            imageDrawable = R.mipmap.drugs_intro
            bgColor = getResources().getColor(R.color.intro1)
        }
        with(sliderPage2) {
            title = getString(R.string.intro2)
            description = getString(R.string.intro2d)
            imageDrawable = R.mipmap.doctor
            bgColor = getResources().getColor(R.color.intro2)
        }

        addSlide(AppIntroFragment.newInstance(sliderPage1))
        addSlide(AppIntroFragment.newInstance(sliderPage2))
        setFadeAnimation()

        // Buttons
        progressButtonEnabled = true
        skipButtonEnabled = false
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        // update version
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, VERSION).apply()
        // start main screen
        startActivity(Intent(this, DrugsActivity::class.java))
    }
}