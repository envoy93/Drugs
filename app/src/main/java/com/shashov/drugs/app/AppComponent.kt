package com.shashov.drugs.app

import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun provideApp(): Application
    fun provideDb(): AppDatabase
}