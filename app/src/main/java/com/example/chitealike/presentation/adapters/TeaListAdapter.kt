package com.example.chitealike.presentation.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chitealike.R
import com.example.chitealike.databinding.TeaItemBinding
import com.example.chitealike.di.qualifiers.FragmentQualifier
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.enums.TeaType
import com.example.chitealike.domain.usecases.LoadImageUseCase
import com.example.chitealike.presentation.callbacks.TeaItemDiffCallback
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeaListAdapter @Inject constructor(
    private val teaItemDiffCallback: TeaItemDiffCallback,
    private val loadImageUseCase: LoadImageUseCase,
    @FragmentQualifier private val fragment: Fragment
) : ListAdapter<TeaItem, TeaListAdapter.TeaItemViewHolder>(teaItemDiffCallback) {

    var onItemClickListener: ((teaItem: Int) -> Unit)? = null

    inner class TeaItemViewHolder(val binding: TeaItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(teaItem: TeaItem){
                with(binding){
                    teaName.text = teaItem.name
                    teaType.text = teaItem.type.teaName.uppercase()
                    setTeaTypeTextColor(teaType,teaItem.type)


                    loadImage(teaItem.imageFileName)
                    setupClickListener(teaItem)
            }

        }

        private fun setTeaTypeTextColor(view: TextView, teaType: TeaType){
            val colorsMap = mapOf(
                TeaType.Red to R.color.red_tea,
                TeaType.Puer to R.color.puer_tea,
                TeaType.Black to R.color.black_tea,
                TeaType.Green to R.color.green_tea,
                TeaType.White to R.color.white_tea,
                TeaType.Oolong to R.color.oolong_tea,
                TeaType.Yellow to R.color.yellow_tea,
            )

            colorsMap[teaType]?.let { view.setTextColor(ContextCompat.getColor(binding.root.context, it)) }
        }

        private fun loadImage(fileName: String?){
            with (binding){
                if (fileName == null){
                    teaImage.setImageResource(R.drawable.ic_launcher_foreground)
                    return
                }


                fragment.lifecycleScope.launch {
                    val bitmap = loadImageUseCase.invoke(fileName)
                    bitmap?.let {
                        teaImage.setImageDrawable(it)
                    } ?: teaImage.setImageResource(R.drawable.ic_launcher_foreground)
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setupClickListener(teaItem: TeaItem){
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(teaItem.id)
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



    override fun onBindViewHolder(holder: TeaItemViewHolder, position: Int) {
        val teaItem = getItem(position)

        holder.bind(teaItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeaItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.tea_item,
            parent,
            false
        )
        val binding =  TeaItemBinding.bind(view)
        return TeaItemViewHolder(binding)

    }




}