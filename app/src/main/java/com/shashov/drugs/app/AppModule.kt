package com.shashov.drugs.app

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration



@Module
class AppModule(val application: Application) {
    val MIGRATION_3_4: Migration = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }

    @Singleton
    @Provides
    fun provideApp(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideDb(): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "drugs2.db")
                .openHelperFactory(com.shashov.drugs.sqlasset.AssetSQLiteOpenHelperFactory())
                .addMigrations(MIGRATION_3_4)
                .allowMainThreadQueries()
                .build()
    }
}