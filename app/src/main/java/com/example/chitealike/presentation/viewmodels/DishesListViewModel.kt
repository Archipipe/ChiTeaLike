package com.example.chitealike.presentation.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.usecases.GetDishesListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DishesListViewModel @Inject constructor(
    private val getDishesListUseCase: GetDishesListUseCase
): ViewModel() {

    private val _dishesList = MutableStateFlow<DishesListState>(DishesListState.Loading)
    val dishesList = _dishesList.asStateFlow()

    private lateinit var fullDishesList: List<DishItem>

    init {
        loadDishesListFromDB()
    }

    private fun loadDishesListFromDB() {
        viewModelScope.launch {
            try{
                val dishesListFromDB = getDishesListUseCase.invoke()
                _dishesList.value = DishesListState.Success(dishesListFromDB)
                fullDishesList = dishesListFromDB
            } catch (e:Exception){
                _dishesList.value = DishesListState.Error(e.message ?: "Ошибка загрузки приборов")
            }

        }
    }

    fun teaFilterSearch(filterSearch: String?) {
        _dishesList.value = DishesListState.Loading

        val list = fullDishesList.filter{ it.name.lowercase().contains(filterSearch?.lowercase() ?: "") }
        _dishesList.value = DishesListState.Success(list)

    }

    sealed class DishesListState{
        data object Loading: DishesListState()
        data class Success(val list: List<DishItem>): DishesListState()
        data class Error(val message: String): DishesListState()
    }

}