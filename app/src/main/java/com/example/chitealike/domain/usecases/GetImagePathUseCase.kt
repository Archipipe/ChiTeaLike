package com.example.chitealike.domain.usecases

import com.example.chitealike.domain.repositories.ImageRepository
import javax.inject.Inject

class GetImagePathUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    operator fun invoke(fileName: String): String {
        return repository.getImagePath(fileName)
    }
}