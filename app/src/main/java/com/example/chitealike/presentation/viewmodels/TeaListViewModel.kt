package com.example.chitealike.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitealike.domain.usecases.GetTeaListUseCase
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.enums.TeaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeaListViewModel @Inject constructor(
    private val getTeaListUseCase: GetTeaListUseCase
) : ViewModel() {

    private val _teaList = MutableStateFlow<TeaListState>(TeaListState.Loading)
    val teaList = _teaList.asStateFlow()

    private lateinit var fullTeaList: List<TeaItem>

    init {
        loadTeaList()
    }

    private fun loadTeaList(){
        viewModelScope.launch {
            try{
                val teaList = getTeaListUseCase.invoke()
                _teaList.value = TeaListState.Success(teaList)
                fullTeaList = teaList
            } catch (e: Exception){
                _teaList.value = TeaListState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun teaFilterSearch(filterSearch: String?, filterTag: TeaType?) {
        _teaList.value = TeaListState.Loading

        val list = fullTeaList.filter { it.name.lowercase().contains(filterSearch?.lowercase() ?: "") &&  (filterTag == null || it.type == filterTag) }

        _teaList.value = TeaListState.Success(list)
    }

    sealed class TeaListState {
        data object Loading : TeaListState()
        data class Error(val message: String): TeaListState()
        data class Success(val result: List<TeaItem>): TeaListState()
    }
}


