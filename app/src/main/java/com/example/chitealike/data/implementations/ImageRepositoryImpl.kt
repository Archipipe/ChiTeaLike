package com.example.chitealike.data.implementations

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.example.chitealike.domain.repositories.ImageRepository
import java.io.File
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val application: Application
): ImageRepository {
    override suspend fun loadImage(filePath: String): Drawable? {
        return try {
            val inputStream = application.applicationContext.assets.open(filePath)
            Drawable.createFromStream(inputStream,null)
        } catch (e: Exception){
            null
        }
    }

    override fun getImagePath(fileName: String): String {
        return File(application.applicationContext.filesDir, fileName).absolutePath
    }

}