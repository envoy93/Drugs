package com.shashov.drugs.features.drugs

import com.shashov.drugs.app.AppComponent
import com.shashov.drugs.features.drugs.presentation.AnalogsViewModel
import com.shashov.drugs.features.drugs.presentation.SearchViewModel
import dagger.Component

@DrugsScope
@Component(modules = arrayOf(DrugsModule::class), dependencies = arrayOf(AppComponent::class))
interface DrugsComponent {
    fun inject(target: AnalogsViewModel)
    fun inject(target: SearchViewModel)
}