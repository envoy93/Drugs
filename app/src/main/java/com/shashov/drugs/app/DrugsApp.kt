package com.shashov.drugs.app

import android.app.Application
import com.shashov.drugs.features.drugs.DaggerDrugsComponent
import com.shashov.drugs.features.drugs.DrugsComponent
import com.shashov.drugs.features.drugs.DrugsModule

class DrugsApp : Application() {

    lateinit var appComponent: AppComponent
    var drugsComponent: DrugsComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = buildAppComponent()
    }


    private fun buildAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }


    fun buildDrugsComponent(): DrugsComponent {
        drugsComponent = DaggerDrugsComponent.builder()
                .appComponent(appComponent)
                .drugsModule(DrugsModule())
                .build()
        return drugsComponent!!
    }

    fun releaseDrugsComponent() {
        drugsComponent = null
    }


}