package com.shashov.drugs.features.drugs.presentation.views

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.shashov.drugs.features.drugs.presentation.SearchType
import com.shashov.drugs.features.drugs.presentation.SearchViewModel
import com.shashov.drugs.features.drugs.presentation.adapters.SearchListAdapter
import com.shashov.drugs.features.drugs.presentation.hide
import com.shashov.drugs.features.drugs.presentation.show
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


class SearchFragment : LifecycleFragment() {
    private val TAG = SearchFragment::class.java.simpleName
    private lateinit var searchViewModel: SearchViewModel
    private var listener: OpenAnalogsListener? = null
    private var handler = Handler(Looper.getMainLooper()) /*UI thread*/
    private var work: Runnable = Runnable {
        searchViewModel.changeScrollToTop(true)
        searchViewModel.loadList()
    }

    private val onTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handler.removeCallbacks(work)
        }

        override fun afterTextChanged(editable: Editable?) {
            var text = editable.toString().trim()

            searchViewModel.changeText(text)
            //if (text.length < 2 && !text.isEmpty()) {
            //    return
            //}

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
            if (drugs!!.isEmpty()) {
                empty.show()
            } else {
                empty.hide()
            }

            if (list.adapter == null) {
                list.adapter = SearchListAdapter(drugs, { str ->
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

        //entryTitle.setText(searchViewModel.getSearchText().value)
        //entryTitle.clearFocus()

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_drug -> {
                    if (searchViewModel.getSearchType().value != SearchType.DRUG) {
                        searchViewModel.changeType(SearchType.DRUG)
                        handler.removeCallbacks(work)
                        searchViewModel.changeScrollToTop(true)
                        searchViewModel.loadList()
                        true
                    } else {
                        false
                    }
                }
                R.id.action_substance -> {
                    if (searchViewModel.getSearchType().value != SearchType.SUBSTANCE) {
                        searchViewModel.changeType(SearchType.SUBSTANCE)
                        handler.removeCallbacks(work)
                        searchViewModel.changeScrollToTop(true)
                        searchViewModel.loadList()
                        true
                    } else {
                        false
                    }
                }
                else -> {
                    false
                }
            }
        }
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


