package com.shashov.drugs.features.drugs.usecases

import android.util.Log
import com.shashov.drugs.base.usecases.UseCase
import com.shashov.drugs.features.drugs.data.DrugsRepo
import com.shashov.drugs.features.drugs.data.local.Drug
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.ISearchItem
import com.shashov.drugs.features.drugs.data.local.Substance
import com.shashov.drugs.features.drugs.presentation.SplashViewModel
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetDrugsUseCase @Inject
internal constructor(private val repo: DrugsRepo) : UseCase<GetDrugsUseCase.Input, List<ISearchItem>>() {
    private var lastSearchSubscriber: DisposableSubscriber<List<ISearchItem>>? = null
    private var lastAnalogsSubscriber: DisposableSubscriber<List<Drugs>>? = null

    fun getDrugs(input: Input, subscriber: DisposableSubscriber<List<ISearchItem>>) {
        updateSearchSubscriber(subscriber)

        Flowable.just(input.title)
                .map<List<Drug>> { title ->
                    if (title.isEmpty()) ArrayList<Drug>()
                    else repo.getDrugs("%$title%").blockingFirst()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(input.observerOnScheduler)
                .subscribe(subscriber)

        Log.d(TAG, "called subscribe on getDrugs flowable")
        disposables.add(subscriber)
    }

    fun getSubstances(input: Input, subscriber: DisposableSubscriber<List<ISearchItem>>) {
        updateSearchSubscriber(subscriber)

        Flowable.just(input.title)
                .map<List<Substance>> { title ->
                    if (title.isEmpty()) ArrayList()
                    else repo.getSubstances("%$title%").blockingFirst()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(input.observerOnScheduler)
                .subscribe(subscriber)

        Log.d(TAG, "called subscribe on getSubstances flowable")
        disposables.add(subscriber)
    }

    fun getAnalogs(input: Input, subscriber: DisposableSubscriber<List<Drugs>>) {
        updateAnalogsSubscriber(subscriber)

        Flowable.just(input.title)
                .map<List<Drugs>> { title ->
                    if (title.isEmpty()) ArrayList()
                    else repo.getAnalogs(title).blockingFirst()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(input.observerOnScheduler)
                .subscribe(subscriber)

        Log.d(TAG, "called subscribe on getAnalogs flowable")
        disposables.add(subscriber)
    }

    private fun updateSearchSubscriber(subscriber: DisposableSubscriber<List<ISearchItem>>){
        if (lastSearchSubscriber != null && !lastSearchSubscriber!!.isDisposed) {
            lastSearchSubscriber!!.dispose()
            disposables.add(lastSearchSubscriber!!)
        }

        lastSearchSubscriber = subscriber
    }

    private fun updateAnalogsSubscriber(subscriber: DisposableSubscriber<List<Drugs>>){
        if (lastAnalogsSubscriber != null && !lastAnalogsSubscriber!!.isDisposed) {
            lastAnalogsSubscriber!!.dispose()
            disposables.add(lastAnalogsSubscriber!!)
        }

        lastAnalogsSubscriber = subscriber
    }

    class Input(val title: String, val observerOnScheduler: Scheduler)

    companion object {

        private val TAG = GetDrugsUseCase::class.java.simpleName
    }

    fun getDrug(input: Input, splashUseCaseSubscriber: SplashViewModel.SplashUseCaseSubscriber) {
        Flowable.just(input.title)
                .delay(5, TimeUnit.SECONDS)
                .map<Drug?> { title ->
                    repo.getDrug().blockingFirst()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(input.observerOnScheduler)
                .subscribe(splashUseCaseSubscriber)

        Log.d(TAG, "called subscribe on getDrugs flowable")
        disposables.add(splashUseCaseSubscriber)
    }

}