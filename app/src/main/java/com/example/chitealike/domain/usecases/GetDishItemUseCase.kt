package com.example.chitealike.domain.usecases

import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class GetDishItemUseCase @Inject constructor(
    private val repository: TeaRepository
) {
    suspend operator fun invoke(dishItemId: Int): DishItem {
        return repository.getDishItem(dishItemId)
    }
}