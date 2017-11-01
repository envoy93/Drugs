package com.shashov.drugs.features.drugs.data

import com.shashov.drugs.features.drugs.data.local.Drug
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.Substance
import io.reactivex.Flowable

interface DrugsRepo {
    fun getDrugs(title: String): Flowable<List<Drug>>
    fun getSubstances(title: String): Flowable<List<Substance>>
    fun getAnalogs(substance: String): Flowable<List<Drugs>>
    fun getDrug(): Flowable<Drug>
}
