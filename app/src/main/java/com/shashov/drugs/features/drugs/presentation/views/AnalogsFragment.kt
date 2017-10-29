package com.shashov.drugs.features.drugs.presentation.views

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.shashov.drugs.R
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.presentation.AnalogsViewModel
import com.shashov.drugs.features.drugs.presentation.adapters.AnalogsListAdapter
import com.shashov.drugs.features.drugs.presentation.hide
import com.shashov.drugs.features.drugs.presentation.show
import kotlinx.android.synthetic.main.fragment_analogs.*


class AnalogsFragment : LifecycleFragment() {
    private val TAG = AnalogsFragment::class.java.simpleName
    private lateinit var analogsViewModel: AnalogsViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_analogs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        analogsViewModel = ViewModelProviders.of(activity).get(AnalogsViewModel::class.java)
        list.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        list?.isNestedScrollingEnabled = false

        analogsViewModel.getSubstance().observe(this, Observer<String> { substance ->
            substanceName.text =
                    if (!substance!!.isEmpty()) {
                        substance
                    } else {
                        getString(R.string.no_choice)
                    }
        })

        analogsViewModel.getLoading().observe(this, Observer<Boolean> { isloading ->
            if (isloading!!) progressBar.show() else progressBar.hide()
        })

        analogsViewModel.getAnalogs().observe(this, Observer<List<Drugs>> { analogs ->
            Log.d(TAG, "received update for analogs")
            if (list.adapter == null) {
                list.adapter = AnalogsListAdapter(analogs!!)
            } else {
                list.adapter.notifyDataSetChanged()
            }
            if (analogsViewModel.getScrollToTop().value!!) {
                analogsCoordinator?.scrollTo(0, 0)
                analogsCoordinator?.fullScroll(View.FOCUS_UP)
            }
            analogsViewModel.changeScrollToTop(false)
        })
    }
}
