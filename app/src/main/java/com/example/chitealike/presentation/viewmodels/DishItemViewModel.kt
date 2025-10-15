package com.example.chitealike.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.usecases.GetDishItemUseCase
import com.example.chitealike.domain.usecases.GetTeaItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DishItemViewModel @Inject constructor(
    private val getDishItemUseCase: GetDishItemUseCase
) : ViewModel() {

    private val _dishItem = MutableLiveData<DishItem>()
    val dishItem: LiveData<DishItem> = _dishItem


    fun getDishItem(dishItemId: Int){
        viewModelScope.launch {
            _dishItem.value = getDishItemUseCase.invoke(dishItemId)
        }
    }
}