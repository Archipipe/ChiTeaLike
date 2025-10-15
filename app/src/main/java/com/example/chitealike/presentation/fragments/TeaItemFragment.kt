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
import com.example.chitealike.databinding.TeaItemFragmentBinding
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.usecases.LoadImageUseCase
import com.example.chitealike.presentation.factories.ViewModelFactory
import com.example.chitealike.presentation.viewmodels.TeaItemViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeaItemFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var loadImageUseCase: LoadImageUseCase

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(TeaItemViewModel::class.java)
    }

    private val component by lazy {
        (requireActivity().application as MainApplication).component
            .catalogComponentFactory()
            .create(this)
    }

    private val binding by lazy {
        TeaItemFragmentBinding.inflate(layoutInflater)
    }

    private val args: TeaItemFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
        viewModel.getTeaItem(args.teaItemId)

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

        with(binding) {
            brewingMethodGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.classic_method -> {
                        classicDetails.visibility = View.VISIBLE
                        pouringDetails.visibility = View.GONE
                    }

                    R.id.pouring_method -> {
                        classicDetails.visibility = View.GONE
                        pouringDetails.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.teaItem.observe(viewLifecycleOwner) {
            with(binding) {
                if (it.imageFileName != null) {
                    this@TeaItemFragment.lifecycleScope.launch {
                        val bitmap = loadImageUseCase.invoke(it.imageFileName)
                        bitmap?.let {
                            teaImage.visibility = View.VISIBLE
                            teaImage.setImageDrawable(it)
                        }
                    }
                }

                teaName.text = it.name
                teaType.text = it.type.teaName

                classicTime.text = it.classicTime
                classicTemperature.text = it.classicTemperature
                classicAmount.text = it.classicTeaAmount
                classicBrews.text = it.classicBrewingAmount

                pouringTime.text = it.brewingTime
                pouringTemperature.text = it.brewingTemperature
                pouringTeaAmount.text = it.brewingTeaAmount
                pouringCount.text = it.brewingBrewingAmount

                teaCountry.text = it.country
                teaTaste.text = it.taste.joinToString(separator = ", ")
                teaDescription.text = it.description

            }

        }


    }

    private fun setupToolbar() {


        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }
}