package com.example.chitealike.data.implementations

import com.example.chitealike.data.Mappers
import com.example.chitealike.data.database.DishesListDao
import com.example.chitealike.data.database.FavoritesListDao
import com.example.chitealike.data.database.TeaListDao
import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.repositories.TeaRepository
import javax.inject.Inject

class TeaRepositoryImpl @Inject constructor(
    private val mappers: Mappers,
    private val teaListDao: TeaListDao,
    private val dishesListDao: DishesListDao,
    private val favoritesListDao: FavoritesListDao,
) : TeaRepository {
    override suspend fun getTeaList(): List<TeaItem> {
        return mappers.mapListTeaDBModelToListTeaItem(teaListDao.getTeaList())
    }

    override suspend fun getTeaItem(teaItemId: Int): TeaItem {
        return mappers.mapTeaDBModelToTeaItem(teaListDao.getTeaItem(teaItemId))
    }

    override suspend fun getDishesList(): List<DishItem> {
        return mappers.mapListDishesDBModelToListDishItem(dishesListDao.getDishesList())
    }

    override suspend fun getDishItem(dishItemId: Int): DishItem {
        return mappers.mapDishDBModelToDishItem(dishesListDao.getDishItem(dishItemId))
    }

    override suspend fun getFavoritesList(): List<FavoriteItem> {
        return mappers.mapListFavoriteDBModelToListFavoriteItem(favoritesListDao.getFavoritesList())
    }

    override suspend fun getFavoriteItem(favoriteItemId: Int): FavoriteItem {
        return mappers.mapFavoriteDBModelToFavoriteItem(favoritesListDao.getFavoriteItem(favoriteItemId))
    }

    override suspend fun createFavoriteItem(favoriteItem: FavoriteItem) {
        favoritesListDao.insertFavoriteItem(mappers.mapFavoriteItemToFavoriteDBModel(favoriteItem))
    }

    override suspend fun deleteFavoriteItem(favoriteItem: FavoriteItem) {
        val favoriteDBModel = mappers.mapFavoriteItemToFavoriteDBModel(favoriteItem)
        favoritesListDao.deleteFavoriteItem(favoriteDBModel.favorite_id)
    }
}