package com.example.chitealike.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.usecases.GetTeaItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeaItemViewModel @Inject constructor(
    private val getTeaItemUseCase: GetTeaItemUseCase
) : ViewModel() {

    private val _teaItem = MutableLiveData<TeaItem>()
    val teaItem: LiveData<TeaItem> = _teaItem


    fun getTeaItem(teaItemId: Int){
        viewModelScope.launch {
            _teaItem.value = getTeaItemUseCase.invoke(teaItemId)
        }
    }
}