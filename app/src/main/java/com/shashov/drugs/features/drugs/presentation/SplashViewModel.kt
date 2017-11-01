package com.shashov.drugs.features.drugs.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.shashov.drugs.app.DrugsApp
import com.shashov.drugs.features.drugs.data.local.Drug
import com.shashov.drugs.features.drugs.usecases.GetDrugsUseCase
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private var isLoading: MutableLiveData<Boolean>
    @Inject
    lateinit var getDrugsUseCase: Lazy<GetDrugsUseCase>

    init {
        (application as DrugsApp).drugsComponent!!.inject(this)

        isLoading = MutableLiveData()
        isLoading.value = true
    }

    internal fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    internal fun load() {
        isLoading.value = true
        getDrugsUseCase.get().getDrug(
                GetDrugsUseCase.Input("", AndroidSchedulers.mainThread()),
                SplashUseCaseSubscriber())
    }

    override fun onCleared() {
        super.onCleared()
        // remove subscriptions if any
        getDrugsUseCase.get().cancel()
        Log.d(TAG, "onCleared")
    }

    inner class SplashUseCaseSubscriber : DisposableSubscriber<Drug?>() {

        override fun onNext(list: Drug?) {
            Log.d(TAG, "Received response for drugs")
            isLoading.value = false
        }

        override fun onError(e: Throwable) {
            Log.d(TAG, "Received error: " + e.toString())
            isLoading.value = false
        }

        override fun onComplete() {
            Log.d(TAG, "onComplete called")
        }

    }

    companion object {
        private val TAG = SplashViewModel::class.java.simpleName
    }
}