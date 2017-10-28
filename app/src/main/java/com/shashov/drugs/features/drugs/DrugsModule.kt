package com.shashov.drugs.features.drugs

import com.shashov.drugs.app.AppDatabase
import com.shashov.drugs.features.drugs.data.DrugsRepoImpl
import com.shashov.drugs.features.drugs.data.local.DrugsDao
import com.shashov.drugs.features.drugs.data.remote.WikiApiService
import com.shashov.drugs.features.drugs.data.DrugsRepo
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DrugsModule {

    @DrugsScope
    @Provides
    internal fun provideDrugsDao(db: AppDatabase): DrugsDao {
        return db.drugsDao()
    }

    @DrugsScope
    @Provides
    internal fun provideWikiApiService(): WikiApiService {
        return Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WikiApiService::class.java)
    }

    @DrugsScope
    @Provides
    internal fun provideDrugsRepo(appDatabase: AppDatabase, apiService: WikiApiService): DrugsRepo {
        return DrugsRepoImpl(appDatabase, apiService)
    }
}