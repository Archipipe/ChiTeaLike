package com.example.chitealike.domain.usecases

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.example.chitealike.domain.repositories.ImageRepository
import javax.inject.Inject

class LoadImageUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(filePath: String): Drawable? {
        return repository.loadImage(filePath)
    }
}