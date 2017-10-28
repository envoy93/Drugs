package com.shashov.drugs.features.drugs.presentation.views

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.shashov.drugs.R
import com.shashov.drugs.features.drugs.data.local.ISearchItem
import com.shashov.drugs.features.drugs.presentation.*
import com.shashov.drugs.features.drugs.presentation.adapters.SearchListAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import android.os.Looper
import android.app.Activity
import android.view.inputmethod.InputMethodManager


class SearchFragment : LifecycleFragment() {
    private val TAG = SearchFragment::class.java.simpleName
    private lateinit var searchViewModel: SearchViewModel
    private var listener: OpenAnalogsListener? = null
    private var handler = Handler(Looper.getMainLooper()) /*UI thread*/
    private var work: Runnable = Runnable { searchViewModel.loadList() }

    private val onTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handler.removeCallbacks(work)
        }

        override fun afterTextChanged(editable: Editable?) {
            var text = editable.toString().trim()
            searchViewModel.changeScrollToTop(true)
            searchViewModel.changeText(text)
            if (text.length < 2 && !text.isEmpty()) {
                return
            }

            // run search
            handler.postDelayed(work, 700)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        searchViewModel = ViewModelProviders.of(activity).get(SearchViewModel::class.java)
        searchViewModel.changeScrollToTop(false)

        searchViewModel.getDrugs().observe(this, Observer<ArrayList<ISearchItem>> { drugs ->
            Log.d(TAG, "received update for drugs")
            if (list.adapter == null) {
                list.adapter = SearchListAdapter(drugs!!, { str ->
                    entryTitle?.clearFocus()
                    listener?.hideSoftKeyboard(entryTitle)
                    listener?.openAnalogs(str!!)
                })
            } else {
                list.adapter.notifyDataSetChanged()
            }
            if (searchViewModel.getScrollToTop().value!!) {
                list.scrollToPosition(0)
            }
            searchViewModel.changeScrollToTop(false)
        })

        searchViewModel.getLoading().observe(this, Observer<Boolean> { isloading ->
            if (isloading!!) progressBar.show() else progressBar.hide()
        })

        searchViewModel.getSearchType().observe(this, Observer<SearchType> { searchType ->
            when (searchType!!) {
                SearchType.DRUG -> {
                    filterDrug.on()
                    filterSubstance.off()
                }
                SearchType.SUBSTANCE -> {
                    filterDrug.off()
                    filterSubstance.on()
                }
            }
        })

        //entryTitle.setText(searchViewModel.getSearchText().value)
        //entryTitle.clearFocus()

        filterDrug.setOnClickListener({ _ ->
            if (searchViewModel.getSearchType().value != SearchType.DRUG) {
                searchViewModel.changeType(SearchType.DRUG)
                handler.removeCallbacks(work)
                searchViewModel.loadList()
            }
        })

        filterSubstance.setOnClickListener({ _ ->
            if (searchViewModel.getSearchType().value != SearchType.SUBSTANCE) {
                searchViewModel.changeType(SearchType.SUBSTANCE)
                handler.removeCallbacks(work)
                searchViewModel.loadList()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        entryTitle?.addTextChangedListener(onTextChangeListener)
        entryTitle?.clearFocus()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OpenAnalogsListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OpenAnalogsListener")
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface OpenAnalogsListener {
        fun openAnalogs(item: String)
        fun hideSoftKeyboard(view: View?)
    }
}


