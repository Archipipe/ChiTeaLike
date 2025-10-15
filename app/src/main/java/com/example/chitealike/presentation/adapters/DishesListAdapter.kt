package com.example.chitealike.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import com.example.chitealike.R
import com.example.chitealike.databinding.DishItemBinding
import com.example.chitealike.di.qualifiers.FragmentQualifier
import com.example.chitealike.domain.entities.DishItem
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.enums.TeaType
import com.example.chitealike.domain.usecases.LoadImageUseCase
import com.example.chitealike.presentation.callbacks.DishListDiffCallback
import kotlinx.coroutines.launch
import javax.inject.Inject

class DishesListAdapter @Inject constructor(
    private val loadImageUseCase: LoadImageUseCase,
    @FragmentQualifier private val fragment: Fragment
): RecyclerView.Adapter<DishesListAdapter.DishItemViewHolder>() {

    var onItemClickListener: ((teaItem: Int) -> Unit)? = null

    var dishList = listOf<DishItem>(

    )
        set(value){
            val callback = DishListDiffCallback(dishList,value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    inner class DishItemViewHolder (val binding: DishItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dishItem: DishItem){
            with(binding){
                teaName.text = dishItem.name
                loadImage(dishItem.dishImageFileName)
                setupClickListener(dishItem)
            }

        }

        private fun loadImage(fileName: String?){
            with (binding){
                if (fileName == null){
                    teaImage.setImageResource(com.example.chitealike.R.drawable.ic_launcher_foreground)
                    return
                }


                fragment.lifecycleScope.launch {
                    val bitmap = loadImageUseCase.invoke(fileName)
                    bitmap?.let {
                        teaImage.setImageDrawable(it)
                    } ?: teaImage.setImageResource(com.example.chitealike.R.drawable.ic_launcher_foreground)
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setupClickListener(dishItem: DishItem){
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(dishItem.id)
            }

            binding.root.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(150).start()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    }
                }
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        val binding = DishItemBinding.bind(view)
        return DishItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    override fun onBindViewHolder(holder: DishItemViewHolder, position: Int) {
        val dishItem = dishList[position]
        holder.bind(dishItem)
    }

}