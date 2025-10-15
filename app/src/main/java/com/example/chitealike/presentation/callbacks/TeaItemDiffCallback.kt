package com.example.chitealike.presentation.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.chitealike.domain.entities.TeaItem
import javax.inject.Inject

class TeaItemDiffCallback @Inject constructor(): DiffUtil.ItemCallback<TeaItem>() {
    override fun areItemsTheSame(oldItem: TeaItem, newItem: TeaItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TeaItem, newItem: TeaItem): Boolean {
        return oldItem == newItem
    }
}