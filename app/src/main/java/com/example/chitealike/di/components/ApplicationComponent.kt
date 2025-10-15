package com.example.chitealike.di.components

import android.app.Application
import com.example.chitealike.di.modules.DataModule
import com.example.chitealike.di.modules.DomainModule
import com.example.chitealike.di.modules.ViewModelModule
import com.example.chitealike.di.scopes.ApplicationScope
import com.example.chitealike.presentation.fragments.TeaCatalogFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class, DomainModule::class])
interface ApplicationComponent {

    fun catalogComponentFactory(): CatalogComponent.Factory

    @Component.Factory
    interface ApplicationComponentFactory{

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}