package com.shashov.drugs.app

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.shashov.drugs.features.drugs.data.local.DrugsDao
import com.shashov.drugs.features.drugs.data.local.Drugs

@Database(entities = arrayOf(Drugs::class), version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun drugsDao(): DrugsDao
}