package com.example.chitealike.domain.usecases

import android.util.Log
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class DeleteFavoriteItemUseCase @Inject constructor(
    private val repository: TeaRepository
) {
    suspend operator fun invoke(favoriteItem: FavoriteItem){
        Log.d("FavoritesFragment", "${favoriteItem.name}")
        repository.deleteFavoriteItem(favoriteItem)
    }
}