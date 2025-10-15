package com.example.chitealike.domain.repositories

import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.entities.TeaItem

interface TeaRepository {
    suspend fun getTeaList(): List<TeaItem>
    suspend fun getTeaItem(teaItemId: Int): TeaItem

    suspend fun getDishesList(): List<DishItem>
    suspend fun getDishItem(dishItemId: Int): DishItem

    suspend fun getFavoritesList(): List<FavoriteItem>
    suspend fun getFavoriteItem(favoriteItemId: Int): FavoriteItem
    suspend fun createFavoriteItem(favoriteItem: FavoriteItem)
    suspend fun deleteFavoriteItem(favoriteItem: FavoriteItem)
}
