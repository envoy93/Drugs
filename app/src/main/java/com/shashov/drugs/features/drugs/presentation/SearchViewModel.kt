package com.shashov.drugs.features.drugs.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.data.local.ISearchItem
import com.shashov.drugs.features.drugs.usecases.GetDrugsUseCase
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private var drugs: MutableLiveData<ArrayList<ISearchItem>>
    private var isLoading: MutableLiveData<Boolean>
    private var searchType: MutableLiveData<SearchType>
    private var searchText: MutableLiveData<String>
    private var isScrollToTop: MutableLiveData<Boolean>

    @Inject
    lateinit var getDrugsUseCase: Lazy<GetDrugsUseCase>

    init {
        (application as DrugsApp).drugsComponent!!.inject(this)

        drugs = MutableLiveData()
        isLoading = MutableLiveData()
        searchType = MutableLiveData()
        searchText = MutableLiveData()
        isScrollToTop = MutableLiveData()

        drugs.value = ArrayList()
        isLoading.value = false
        searchType.value = SearchType.DRUG
        searchText.value = ""
        isScrollToTop.value = true
    }

    internal fun getDrugs(): LiveData<ArrayList<ISearchItem>> {
        return drugs;
    }

    private fun loadDrugs(title: String) {
        isLoading.value = true
        getDrugsUseCase.get().getDrugs(
                GetDrugsUseCase.Input(title, AndroidSchedulers.mainThread()),
                SearchUseCaseSubscriber())
    }

    private fun loadSubstances(title: String) {
        isLoading.value = true
        getDrugsUseCase.get().getSubstances(
                GetDrugsUseCase.Input(title, AndroidSchedulers.mainThread()),
                SearchUseCaseSubscriber())
    }

    internal fun loadList(){
        isLoading.value = true
        if (searchType.value == SearchType.DRUG){
            loadDrugs(searchText.value!!)
        } else {
            loadSubstances(searchText.value!!)
        }
    }

    internal fun getSearchType(): LiveData<SearchType> {
        return searchType
    }

    internal fun getScrollToTop(): LiveData<Boolean> {
        return isScrollToTop
    }

    internal fun getSearchText(): LiveData<String> {
        return searchText
    }

    internal fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    internal fun changeType(type: SearchType) {
        searchType.value = type
    }

    internal fun changeText(text: String) {
        searchText.value = text
    }

    internal fun changeScrollToTop(scroll: Boolean) {
        isScrollToTop.value = scroll
    }

    override fun onCleared() {
        super.onCleared()
        // remove subscriptions if any
        getDrugsUseCase.get().cancel()
        Log.d(SearchViewModel.TAG, "onCleared")
    }

    inner class SearchUseCaseSubscriber : DisposableSubscriber<List<ISearchItem>>() {

        override fun onNext(drugs: List<ISearchItem>) {

            Log.d(TAG, "Received response for search items")
            this@SearchViewModel.drugs.value!!.clear();
            this@SearchViewModel.drugs.value!!.addAll(drugs)
            this@SearchViewModel.drugs.postValue(this@SearchViewModel.drugs.value)
            isLoading.value = false
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "Received error: " + e.toString())
            this@SearchViewModel.drugs.value = ArrayList()
            isLoading.value = false
        }

        override fun onComplete() {
            Log.d(TAG, "onComplete called")
        }
    }

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName
    }
}