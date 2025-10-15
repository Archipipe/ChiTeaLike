package com.example.chitealike.di.modules

import com.example.chitealike.data.implementations.ImageRepositoryImpl
import com.example.chitealike.data.implementations.TeaRepositoryImpl
import com.example.chitealike.di.scopes.ApplicationScope
import com.example.chitealike.domain.repositories.ImageRepository
import com.example.chitealike.domain.repositories.TeaRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindTeaRepository(impl: TeaRepositoryImpl): TeaRepository

    @ApplicationScope
    @Binds
    fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository
}