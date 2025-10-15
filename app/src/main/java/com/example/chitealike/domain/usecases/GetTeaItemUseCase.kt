package com.example.chitealike.domain.usecases

import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class GetTeaItemUseCase @Inject constructor(
    private val repository: TeaRepository
) {
    suspend operator fun invoke(teaItemId: Int): TeaItem {
        return repository.getTeaItem(teaItemId)
    }
}