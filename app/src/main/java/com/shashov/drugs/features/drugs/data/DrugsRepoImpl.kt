package com.shashov.drugs.features.drugs.data

import com.shashov.drugs.app.AppDatabase
import com.shashov.drugs.features.drugs.data.local.Drug
import com.shashov.drugs.features.drugs.data.local.Drugs
import com.shashov.drugs.features.drugs.data.local.Substance
import com.shashov.drugs.features.drugs.data.remote.WikiApiService
import com.shashov.drugs.features.drugs.data.remote.WikiEntryApiResponse
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrugsRepoImpl @Inject
constructor(private val appDatabase: AppDatabase, private val wikiApiService: WikiApiService) : DrugsRepo {

    override fun getDrugs(title: String): Flowable<List<Drug>> {
        return appDatabase.drugsDao().getDrugsByTitle(title)
        //val remote = fetchFromRemote(title)
        //return local //Flowable.merge(local, remote).firstElement().toFlowable()
    }

    override fun getSubstances(title: String): Flowable<List<Substance>> {
        return appDatabase.drugsDao().getSubstancesByTitle(title)
    }

    override fun getAnalogs(substance: String): Flowable<List<Drugs>> {
        return appDatabase.drugsDao().getAnalogs(substance)
    }

    //private fun fetchFromLocal(title: String): Flowable<List<Drugs>> {

//        return appDatabase.drugsDao().all //wikiEntryDao().getByTitle(title)
        //Thread.sleep(5*1000)

        /*return entries.flatMap(Function<List<Drugs>, Flowable<Drugs>> { wikiEntryTables ->

            if (!wikiEntryTables.isEmpty()) {
                val firstEntry = wikiEntryTables[0]
                Log.d(TAG, "Found and sending entry from local")
                return@Function Flowable.just(Drugs(firstEntry._id,
                        firstEntry.name!!, firstEntry.company!!))
            }

            Log.d(TAG, "Returning flowable with invalid entry from local")
            Flowable.empty<Drugs>()
        })

    }*/


    /*private fun fetchFromRemote(title: String): Flowable<Drugs> {

        Log.d(TAG, "fetchFromRemote enter")
        val getRequest = wikiApiService.getDrugs(title)
        return getRequest.flatMap { wikiEntryApiResponse ->

            Log.d(TAG, "received response from remote")
            val pageValIterator = wikiEntryApiResponse.query!!.pages!!.values.iterator()
            val pageVal = pageValIterator.next()

            if (invalidResult(pageVal)) {
                Log.d(TAG, "Sending error from remote")
                Flowable.error(NoResultFound())
            } else {
                val wikiEntry = Drugs(pageVal.pageid!!, pageVal.title!!, pageVal.extract!!)
                addNewEntryToLocalDB(wikiEntry)
                Log.d(TAG, "Sending entry from remote")
                Flowable.just(wikiEntry)
            }
        }
    }*/


    /*private fun addNewEntryToLocalDB(wikiEntry: Drugs) {
        appDatabase.beginTransaction()
        try {
            val newEntry = Drugs()
            newEntry.pageId = wikiEntry.pageId
            newEntry.title = wikiEntry.title
            newEntry.extract = wikiEntry.extract

            val entryDao = appDatabase.wikiEntryDao()
            entryDao.insert(newEntry)
            appDatabase.setTransactionSuccessful()
        } finally {
            appDatabase.endTransaction()
        }
        Log.d(TAG, "added new entry into app database table")
    }*/

    private fun invalidResult(pageVal: WikiEntryApiResponse.Pageval): Boolean {
        return pageVal.pageid == null || pageVal.pageid!! <= 0 ||
                pageVal.title == null || pageVal.title!!.isEmpty() ||
                pageVal.extract == null || pageVal.extract!!.isEmpty()
    }

    companion object {

        private val TAG: String = DrugsRepoImpl::class.java.simpleName!!
    }

}