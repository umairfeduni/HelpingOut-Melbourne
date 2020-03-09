package com.umair.helpingout

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.umair.helpingout.data.db.HelpOutDatabase
import com.umair.helpingout.data.db.preferences.PreferenceProvider
import com.umair.helpingout.data.network.*
import com.umair.helpingout.data.repository.DataRepository
import com.umair.helpingout.data.repository.DataRepositoryImpl
import com.umair.helpingout.internal.Utils
import com.umair.helpingout.ui.categories.CategoryListViewModelFactory
import com.umair.helpingout.ui.detail.ServiceDetailViewModelFactory
import com.umair.helpingout.ui.explore.ExploreViewModelFactory
import com.umair.helpingout.ui.numbers.HelpfulNumberViewModelFactory
import com.umair.helpingout.ui.services.ServiceListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class HelpOutApplication : Application(),KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@HelpOutApplication))

        bind() from singleton { HelpOutDatabase(instance()) }

        bind() from singleton { instance<HelpOutDatabase>().placeDao() }

        bind() from singleton { instance<HelpOutDatabase>().placeTagDao() }

        bind() from singleton { instance<HelpOutDatabase>().categoryDao() }

        bind() from singleton { instance<HelpOutDatabase>().tagDao() }

        bind() from singleton { instance<HelpOutDatabase>().numberDao() }

        bind() from singleton { PreferenceProvider(instance()) }

        bind() from singleton { Utils(instance()) }

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImp(instance()) }

        bind() from singleton { HelpOutDataApiService(instance()) }

        bind<PlaceDataSource>() with  singleton { PlaceDataSourceImpl(instance()) }

        bind<CategoryDataSource>() with  singleton { CategoryDataSourceImpl(instance()) }

        bind<NumberDataSource>() with  singleton { NumberDataSourceImpl(instance()) }

        bind<DataRepository>() with  singleton { DataRepositoryImpl(instance(), instance(), instance(),instance(),instance(),instance(), instance(), instance(), instance()) }

        bind() from provider { CategoryListViewModelFactory(instance()) }

        bind() from factory { categoryId: String -> ServiceListViewModelFactory(categoryId, instance()) }

        bind() from factory { placeId: String -> ServiceDetailViewModelFactory(placeId, instance()) }

        bind() from singleton {ExploreViewModelFactory(instance()) }

        bind() from singleton {HelpfulNumberViewModelFactory(instance()) }

    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}