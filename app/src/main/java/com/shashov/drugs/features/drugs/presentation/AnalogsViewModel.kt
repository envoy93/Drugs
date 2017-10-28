package com.shashov.drugs.features.drugs.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.ISearchItem
import com.shashov.drugs.features.drugs.usecases.GetDrugsUseCase
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class AnalogsViewModel(application: Application) : AndroidViewModel(application) {

    private var drugs: MutableLiveData<List<Drugs>>
    private var substance: MutableLiveData<String>
    private var isLoading: MutableLiveData<Boolean>
    private var isScrollToTop: MutableLiveData<Boolean>
    @Inject
    lateinit var getDrugsUseCase: Lazy<GetDrugsUseCase>

    init {
        (application as DrugsApp).drugsComponent!!.inject(this)
        drugs = MutableLiveData()
        substance = MutableLiveData()
        isLoading = MutableLiveData()
        isScrollToTop = MutableLiveData()

        drugs.value = ArrayList()
        substance.value = ""
        isLoading.value = false
        isScrollToTop.value = false
    }

    internal fun getAnalogs(): LiveData<List<Drugs>> {
        return drugs
    }

    internal fun getSubstance(): LiveData<String> {
        return substance;
    }
    internal fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    internal fun getScrollToTop(): LiveData<Boolean> {
        return isScrollToTop
    }

    internal fun changeScrollToTop(scroll: Boolean){
        isScrollToTop.value = scroll
    }

    fun isCleared() : Boolean {
        return substance.value!!.isEmpty()
    }

    fun clear() {
        (drugs.value!! as ArrayList).clear()
        substance.value = ""
        isLoading.value = false
        drugs.postValue(drugs.value!!)
    }

    internal fun loadAnalogs(sub: String) {
        isLoading.value = true
        substance.value = sub
        getDrugsUseCase.get().getAnalogs(
                GetDrugsUseCase.Input(sub, AndroidSchedulers.mainThread()),
                SearchUseCaseSubscriber())
    }

    override fun onCleared() {
        super.onCleared()
        // remove subscriptions if any
        getDrugsUseCase.get().cancel()
        Log.d(TAG, "onCleared")
    }

    inner class SearchUseCaseSubscriber : DisposableSubscriber<List<Drugs>>() {

        override fun onNext(list: List<Drugs>) {
            Log.d(TAG, "Received response for drugs")
            with (drugs){
                isLoading.value = false
                with (value!! as ArrayList<Drugs>) {
                    clear()
                    addAll(list)
                }
                postValue(this.value!!)
            }
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "Received error: " + e.toString())
            (drugs.value!! as ArrayList).clear()
            isLoading.value = false
            drugs.postValue(drugs.value!!)
        }
        override fun onComplete() {
            Log.d(TAG, "onComplete called")
        }

    }

    companion object {
        private val TAG = AnalogsViewModel::class.java.simpleName
    }
}
