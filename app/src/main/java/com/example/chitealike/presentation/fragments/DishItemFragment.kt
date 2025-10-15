package com.example.chitealike.presentation.fragments

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.chitealike.MainApplication
import com.example.chitealike.R
import com.example.chitealike.databinding.DishItemFragmentBinding
import com.example.chitealike.databinding.TeaItemFragmentBinding
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.usecases.LoadImageUseCase
import com.example.chitealike.presentation.factories.ViewModelFactory
import com.example.chitealike.presentation.viewmodels.DishItemViewModel
import com.example.chitealike.presentation.viewmodels.TeaItemViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DishItemFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var loadImageUseCase: LoadImageUseCase

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(DishItemViewModel::class.java)
    }

    private val component by lazy {
        (requireActivity().application as MainApplication).component
            .catalogComponentFactory()
            .create(this)
    }

    private val binding by lazy {
        DishItemFragmentBinding.inflate(layoutInflater)
    }

    private val args: DishItemFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
        viewModel.getDishItem(args.dishItemId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        setupToolbar()

    }

    private fun subscribeObservers() {
        viewModel.dishItem.observe(viewLifecycleOwner) {
            with(binding) {

                if (it.dishImageFileName != null) {
                    this@DishItemFragment.lifecycleScope.launch {
                        val bitmap = loadImageUseCase.invoke(it.dishImageFileName)
                        bitmap?.let {
                            dishImage.visibility = View.VISIBLE
                            dishImage.setImageDrawable(it)
                        }
                    }
                }

                dishName.text = it.name
                dishType.text = it.type
                dishDescription.text = it.description

            }

        }


    }

    private fun setupToolbar() {


        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }
}