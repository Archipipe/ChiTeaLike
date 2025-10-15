package com.example.chitealike.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.usecases.GetFavoritesListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesListViewModel @Inject constructor(
    private val getFavoritesListUseCase: GetFavoritesListUseCase
): ViewModel() {

    private val _favoritesList = MutableStateFlow<FavoritesListState>(FavoritesListState.Loading)
    val favoritesList = _favoritesList.asStateFlow()

    init{
        loadFavoritesListFromDB()
    }

    fun loadFavoritesListFromDB() {
        viewModelScope.launch {
            try {
                val favoritesListFromDB = getFavoritesListUseCase.invoke()
                _favoritesList.value = FavoritesListState.Success(favoritesListFromDB)
            } catch (e: Exception){
                _favoritesList.value = FavoritesListState.Error(e.message ?: "Произошла ошибка")
            }
        }

    }


    sealed class FavoritesListState {
        data object Loading : FavoritesListState()
        data class Error(val message: String): FavoritesListState()
        data class Success(val result: List<FavoriteItem>): FavoritesListState()
    }
}