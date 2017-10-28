package com.shashov.drugs.features.drugs.presentation.views


import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.shashov.drugs.R
import com.shashov.drugs.app.DrugsApp
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.shashov.drugs.features.drugs.presentation.*
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_search.*
import android.view.animation.AnimationUtils




class DrugsActivity : LifecycleActivity(), SearchFragment.OpenAnalogsListener {
    private val TAG = DrugsActivity::class.java.simpleName
    private lateinit var analogsViewModel: AnalogsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DrugsApp).buildDrugsComponent()
        setContentView(R.layout.activity_main)
        analogsViewModel = ViewModelProviders.of(this).get(AnalogsViewModel::class.java)

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

    override fun openAnalogs(item: String) {
        //hideSoftKeyboard()
        analogsViewModel.changeScrollToTop(true)
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
            analogsLayout?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
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
