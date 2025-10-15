package com.example.chitealike.presentation.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.chitealike.domain.entities.FavoriteItem
import javax.inject.Inject

class FavoriteItemDiffCallback @Inject constructor(): DiffUtil.ItemCallback<FavoriteItem>() {
    override fun areItemsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean {
        return oldItem == newItem
    }
}