package com.example.chitealike.domain.usecases

import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class GetTeaListUseCase @Inject constructor(
    private val repository: TeaRepository
) {
    suspend operator fun invoke(): List<TeaItem>{
        return repository.getTeaList()
    }
}