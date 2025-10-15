package com.example.chitealike.di.modules

import android.app.Application
import com.example.chitealike.data.database.AppDatabase
import com.example.chitealike.data.database.DishesListDao
import com.example.chitealike.data.database.FavoritesListDao
import com.example.chitealike.data.database.TeaListDao
import com.example.chitealike.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @ApplicationScope
    @Provides
    fun provideTeaListDao(application: Application): TeaListDao{
        return AppDatabase.getInstance(application).teaListDao()
    }

    @ApplicationScope
    @Provides
    fun provideDishesListDao(application: Application): DishesListDao{
        return AppDatabase.getInstance(application).dishesListDao()
    }

    @ApplicationScope
    @Provides
    fun provideFavoritesListDao(application: Application): FavoritesListDao{
        return AppDatabase.getInstance(application).favoritesListDao()
    }
}