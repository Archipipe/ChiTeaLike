package com.example.chitealike.presentation.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chitealike.R
import com.example.chitealike.databinding.FavoritesItemBinding
import com.example.chitealike.di.qualifiers.FragmentQualifier
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.usecases.GetTeaItemUseCase
import com.example.chitealike.domain.usecases.LoadImageUseCase
import com.example.chitealike.presentation.callbacks.FavoriteItemDiffCallback
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesListAdapter @Inject constructor(
    favoriteItemDiffCallback: FavoriteItemDiffCallback,
    private val loadImageUseCase: LoadImageUseCase,
    private val getTeaItemUseCase: GetTeaItemUseCase,
    @FragmentQualifier private val fragment: Fragment
) : ListAdapter<FavoriteItem, FavoritesListAdapter.FavoriteItemViewHolder>(favoriteItemDiffCallback) {


    inner class FavoriteItemViewHolder(val binding: FavoritesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false

        fun bind(favoriteItem: FavoriteItem) {
            binding.tvName.text = favoriteItem.name
            val description = binding.tvDescription
            description.text = favoriteItem.description

            favoriteItem.teaId?.let {

                fragment.lifecycleScope.launch {

                    val teaItem = getTeaItemUseCase.invoke(favoriteItem.teaId)

                    binding.tvTeaName.visibility = View.VISIBLE
                    binding.tvTeaName.text = teaItem.name
                    teaItem.imageFileName?.let {

                        val bitmap = loadImageUseCase.invoke(teaItem.imageFileName)
                        bitmap?.let {
                            binding.ivTeaImage.visibility = View.VISIBLE
                            binding.ivTeaImage.setImageDrawable(bitmap)
                        }
                    }


                }

            }


            binding.root.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded){
                    setDescriptionVisible()
                    description.visibility = View.VISIBLE
                } else {
                    setDescriptionGone()
                    description.visibility = View.GONE
                }
            }

        }

        private fun setDescriptionVisible() {
            val view = binding.ivExpand

            view.post {
                view.animate()
                    .rotation(-90f)
                    .setDuration(150)
                    .start()
            }
        }

        private fun setDescriptionGone(){
            val view = binding.ivExpand

            view.post {
                view.animate()
                    .rotation(0f)
                    .setDuration(150)
                    .start()
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.favorites_item,
            parent,
            false
        )
        val binding = FavoritesItemBinding.bind(view)
        return FavoriteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        val favoriteItem = getItem(position)
        holder.bind(favoriteItem)
    }


}