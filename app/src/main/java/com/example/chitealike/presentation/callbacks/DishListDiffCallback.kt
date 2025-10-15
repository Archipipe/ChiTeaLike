package com.example.chitealike.presentation.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.chitealike.domain.entities.DishItem
import javax.inject.Inject

class DishListDiffCallback @Inject constructor(
    private val oldList: List<DishItem>,
    private val newList: List<DishItem>,
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}