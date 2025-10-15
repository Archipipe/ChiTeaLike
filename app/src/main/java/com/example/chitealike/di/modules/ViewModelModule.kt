package com.example.chitealike.di.modules

import androidx.lifecycle.ViewModel
import com.example.chitealike.di.keys.ViewModelKey
import com.example.chitealike.presentation.viewmodels.DishItemViewModel
import com.example.chitealike.presentation.viewmodels.DishesListViewModel
import com.example.chitealike.presentation.viewmodels.FavoritesListViewModel
import com.example.chitealike.presentation.viewmodels.TeaItemViewModel
import com.example.chitealike.presentation.viewmodels.TeaListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(TeaListViewModel::class)
    @Binds
    fun bindTeaListViewModel(viewModel: TeaListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(TeaItemViewModel::class)
    @Binds
    fun bindTeaItemViewModel(viewModel: TeaItemViewModel): ViewModel

    @IntoMap
    @ViewModelKey(DishesListViewModel::class)
    @Binds
    fun bindDishesListViewModel(viewModel: DishesListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(DishItemViewModel::class)
    @Binds
    fun bindDishItemViewModel(viewModel: DishItemViewModel): ViewModel

    @IntoMap
    @ViewModelKey(FavoritesListViewModel::class)
    @Binds
    fun bindFavoritesListViewModel(viewModel: FavoritesListViewModel): ViewModel

}