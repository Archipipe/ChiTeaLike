package com.example.chitealike

import android.app.Application
import com.example.chitealike.di.components.DaggerApplicationComponent

class MainApplication: Application() {

    val component by lazy{
        DaggerApplicationComponent.factory()
            .create(this)
    }

}