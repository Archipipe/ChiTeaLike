package com.example.chitealike.domain.repositories

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

interface ImageRepository {
    suspend fun loadImage(filePath: String): Drawable?
    fun getImagePath(fileName: String): String
}