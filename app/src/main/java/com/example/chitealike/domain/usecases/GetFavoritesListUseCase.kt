package com.example.chitealike.domain.usecases

import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class GetFavoritesListUseCase @Inject constructor(
    private val repository: TeaRepository
) {
    suspend operator fun invoke(): List<FavoriteItem>{
        return repository.getFavoritesList()
    }
}