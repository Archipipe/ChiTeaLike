package com.example.chitealike.di.components

import androidx.fragment.app.Fragment
import com.example.chitealike.di.qualifiers.FragmentQualifier
import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.presentation.fragments.DishItemFragment
import com.example.chitealike.presentation.fragments.DishesCatalogFragment
import com.example.chitealike.presentation.fragments.FavoritesCreateFragment
import com.example.chitealike.presentation.fragments.FavoritesEditFragment
import com.example.chitealike.presentation.fragments.FavoritesFragment
import com.example.chitealike.presentation.fragments.TeaCatalogFragment
import com.example.chitealike.presentation.fragments.TeaItemFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface CatalogComponent {

    fun inject(fragment: TeaCatalogFragment)
    fun inject(fragment: TeaItemFragment)

    fun  inject(fragment: DishesCatalogFragment)
    fun  inject(fragment: DishItemFragment)

    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: FavoritesCreateFragment)
    fun inject(fragment: FavoritesEditFragment)

    @Subcomponent.Factory
    interface Factory{

        fun create(
            @BindsInstance @FragmentQualifier fragment: Fragment
        ) : CatalogComponent

    }

}