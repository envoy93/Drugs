package com.shashov.drugs.features.drugs.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.usecases.GetDrugsUseCase
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class AnalogsViewModel(application: Application) : AndroidViewModel(application) {

    private var analogs: MutableLiveData<List<Drugs>>
    private var substance: MutableLiveData<String>
    private var isLoading: MutableLiveData<Boolean>

    @Inject
    lateinit var getDrugsUseCase: Lazy<GetDrugsUseCase>

    init {
        (application as DrugsApp).drugsComponent!!.inject(this)
        analogs = MutableLiveData()
        substance = MutableLiveData()
        isLoading = MutableLiveData()

        analogs.value = ArrayList()
        substance.value = ""
        isLoading.value = false
    }

    internal fun getAnalogs(): LiveData<List<Drugs>> {
        return analogs
    }

    internal fun getSubstance(): LiveData<String> {
        return substance;
    }
    internal fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun isCleared() : Boolean {
        return substance.value!!.isEmpty()
    }

    fun clear() {
        substance.value = ""
        isLoading.value = false
        clearAnalogs()
    }

    internal fun loadAnalogs(sub: String) {
        clearAnalogs()
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

    private fun clearAnalogs() {
        (analogs.value!! as ArrayList).clear()
        analogs.postValue(analogs.value!!)
    }

    inner class SearchUseCaseSubscriber : DisposableSubscriber<List<Drugs>>() {

        override fun onNext(list: List<Drugs>) {
            Log.d(TAG, "Received response for drugs")
            with(analogs) {
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
            isLoading.value = false
            clearAnalogs()
        }
        override fun onComplete() {
            Log.d(TAG, "onComplete called")
        }

    }

    companion object {
        private val TAG = AnalogsViewModel::class.java.simpleName
    }
}
