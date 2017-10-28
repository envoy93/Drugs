package com.shashov.drugs.base.usecases

import io.reactivex.disposables.CompositeDisposable

abstract class UseCase<InputType, OutputType> {

    protected var disposables = CompositeDisposable()

    //abstract fun execute(input: InputType, subscriber: DisposableSubscriber<OutputType>)

    fun cancel() {
        disposables.clear()
    }

}