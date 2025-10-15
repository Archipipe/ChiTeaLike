package com.example.chitealike.data

import com.example.chitealike.data.database.DishDBModel
import com.example.chitealike.data.database.FavoriteDBModel
import com.example.chitealike.data.database.TeaDBModel
import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.entities.TeaItem
import javax.inject.Inject

class Mappers @Inject constructor() {

    fun mapTeaDBModelToTeaItem(teaDBModel: TeaDBModel): TeaItem {
        return TeaItem(
            id = teaDBModel.tea_id,
            name = teaDBModel.tea_name,
            type = teaDBModel.tea_type,
            imageFileName = teaDBModel.tea_image_file_name,
            country = teaDBModel.tea_country,
            taste = teaDBModel.tea_taste,
            description = teaDBModel.tea_description,
            classicTime = teaDBModel.tea_classic_time,
            classicTemperature = teaDBModel.tea_classic_temperature,
            classicTeaAmount = teaDBModel.tea_classic_tea_amount,
            classicBrewingAmount = teaDBModel.tea_classic_brewing_amount,
            brewingTime = teaDBModel.tea_brewing_time,
            brewingTemperature = teaDBModel.tea_brewing_temperature,
            brewingTeaAmount = teaDBModel.tea_brewing_tea_amount,
            brewingBrewingAmount = teaDBModel.tea_classic_brewing_amount
        )
    }

    fun mapTeaItemToTeaDBModel(teaItem: TeaItem): TeaDBModel {
        return TeaDBModel(
            tea_id = teaItem.id,
            tea_name = teaItem.name,
            tea_type = teaItem.type,
            tea_image_file_name = teaItem.imageFileName,
            tea_country = teaItem.country,
            tea_taste = teaItem.taste,
            tea_description = teaItem.description,
            tea_classic_time = teaItem.classicTime,
            tea_classic_temperature = teaItem.classicTemperature,
            tea_classic_tea_amount = teaItem.classicTeaAmount,
            tea_classic_brewing_amount = teaItem.classicBrewingAmount,
            tea_brewing_time = teaItem.brewingTime,
            tea_brewing_temperature = teaItem.brewingTemperature,
            tea_brewing_tea_amount = teaItem.brewingTeaAmount,
            tea_brewing_brewing_amount = teaItem.brewingBrewingAmount
        )
    }

    fun mapListTeaDBModelToListTeaItem(list: List<TeaDBModel>): List<TeaItem> {
        return list.map { mapTeaDBModelToTeaItem(it) }
    }

    fun mapDishDBModelToDishItem(dishDBModel: DishDBModel): DishItem{
        return DishItem(
            id = dishDBModel.dish_id,
            name = dishDBModel.dish_name,
            type = dishDBModel.dish_type,
            dishImageFileName = dishDBModel.dish_image_file_name,
            description = dishDBModel.dish_description
        )
    }
    fun mapListDishesDBModelToListDishItem(list: List<DishDBModel>): List<DishItem>{
        return list.map{ mapDishDBModelToDishItem(it) }
    }

    fun mapFavoriteDBModelToFavoriteItem(favoriteDBModel: FavoriteDBModel): FavoriteItem{
        return FavoriteItem(
            id = favoriteDBModel.favorite_id,
            teaId = favoriteDBModel.tea_id,
            name = favoriteDBModel.favorite_name,
            description = favoriteDBModel.favorite_description
        )
    }

    fun mapFavoriteItemToFavoriteDBModel(favoriteItem: FavoriteItem): FavoriteDBModel{
        return FavoriteDBModel(
            favorite_id = favoriteItem.id,
            tea_id = favoriteItem.teaId,
            favorite_name = favoriteItem.name,
            favorite_description = favoriteItem.description
        )
    }

    fun mapListFavoriteDBModelToListFavoriteItem(list: List<FavoriteDBModel>): List<FavoriteItem>{
        return list.map { mapFavoriteDBModelToFavoriteItem(it) }
    }
}