package com.example.chitealike.domain.usecases

import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class GetDishesListUseCase @Inject constructor(
    private val repository: TeaRepository
) {
    suspend operator fun invoke(): List<DishItem>{
        return repository.getDishesList()
    }
}