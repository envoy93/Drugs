package com.shashov.drugs.features.drugs.data

import com.shashov.drugs.app.AppDatabase
import com.shashov.drugs.features.drugs.data.local.Drug
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.Substance
import com.shashov.drugs.features.drugs.data.remote.WikiApiService
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrugsRepoImpl @Inject
constructor(private val appDatabase: AppDatabase, private val wikiApiService: WikiApiService) : DrugsRepo {

    override fun getDrugs(title: String): Flowable<List<Drug>> {
        return appDatabase.drugsDao().getDrugsByTitle(title)
    }

    override fun getSubstances(title: String): Flowable<List<Substance>> {
        return appDatabase.drugsDao().getSubstancesByTitle(title)
    }

    override fun getAnalogs(substance: String): Flowable<List<Drugs>> {
        return appDatabase.drugsDao().getAnalogs(substance)
    }

    override fun getDrug(): Flowable<Drug> {
        return appDatabase.drugsDao().getDrug()
    }

    companion object {
        private val TAG: String = DrugsRepoImpl::class.java.simpleName!!
    }

}