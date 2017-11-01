package com.shashov.drugs.features.drugs.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface DrugsDao {

    @get:Query("SELECT * FROM drugs LIMIT 500")
    val all: Flowable<List<Drugs>>

    @Query("SELECT * FROM drugs WHERE name LIKE :arg0 COLLATE NOCASE GROUP BY name, substance ORDER BY NAME ASC")
    fun getDrugsByTitle(title: String): Flowable<List<Drug>>

    @Query("SELECT _id, substance FROM drugs WHERE substance LIKE :arg0 COLLATE NOCASE GROUP BY substance ORDER BY substance ASC")
    fun getSubstancesByTitle(title: String): Flowable<List<Substance>>

    @Query("SELECT * FROM drugs WHERE substance = :arg0 COLLATE NOCASE ORDER BY name ASC")
    fun getAnalogs(substance: String): Flowable<List<Drugs>>

    @Query("SELECT * FROM drugs LIMIT 1")
    fun getDrug(): Flowable<Drug>
}