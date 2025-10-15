package com.example.chitealike.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chitealike.MainApplication
import com.example.chitealike.databinding.DishesCatalogFragmentBinding
import com.example.chitealike.domain.enums.TeaType
import com.example.chitealike.presentation.adapters.DishesListAdapter
import com.example.chitealike.presentation.factories.ViewModelFactory
import com.example.chitealike.presentation.viewmodels.DishesListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DishesCatalogFragment : Fragment() {

    @Inject
    lateinit var adapter: DishesListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DishesListViewModel::class.java)
    }

    private val binding by lazy {
        DishesCatalogFragmentBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (requireActivity().application as MainApplication).component.catalogComponentFactory()
            .create(this)
    }

    private var filterSearch: String? = null
    private var isCatalogActivated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)

        setupRecyclerView()
        setupObservers()
        setupToolbar()
    }

    private fun catalogSetVisible(){
        val view = binding.llCatalog
        view.alpha = 0f
        view.visibility = View.VISIBLE


        view.post {
            view.translationY = -view.height.toFloat()
            view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(150)
                .setListener(object:  AnimatorListenerAdapter() {

                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        view.visibility = View.VISIBLE
                    }
                })
                .start()
        }

    }
    private fun catalogSetGone(){
        val view = binding.llCatalog
        view.animate()
            .translationY(-view.height.toFloat())
            .alpha(0f)
            .setDuration(150)
            .setListener(object:  AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.GONE
                    view.translationY = 0f
                    view.alpha = 1f
                }
            })
            .start()
    }
    private fun setupToolbar() {
        binding.ibCatalog.setOnClickListener {
            isCatalogActivated = !isCatalogActivated
            if (isCatalogActivated){
                catalogSetVisible()
            } else {
                catalogSetGone()
            }

        }

        binding.svCatalog.setQuery("", false)



        binding.svCatalog.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterSearch = newText
                return false
            }
        })

        binding.btnApplyFilters.setOnClickListener{
            isCatalogActivated = false
            binding.llCatalog.visibility = View.GONE
            viewModel.teaFilterSearch(filterSearch)
        }

    }

    private fun setupRecyclerView() {
        val recyclerView = binding.rvDishesCatalog
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = {
            DishesCatalogFragmentDirections.actionDishesCatalogFragmentToDishItemFragment(it).apply {
                findNavController().navigate(this)
            }
        }
    }

    private fun setupObservers(){
        lifecycleScope.launch {
            viewModel.dishesList.collectLatest {
                when(it){
                    is DishesListViewModel.DishesListState.Loading -> {
                        adapter.dishList = listOf()

                        binding.pb.visibility = View.VISIBLE
                    }

                    is DishesListViewModel.DishesListState.Success -> {
                        binding.pb.visibility = View.GONE

                        adapter.dishList = it.list
                    }

                    is DishesListViewModel.DishesListState.Error -> {
                        binding.pb.visibility = View.GONE
                        Toast.makeText(requireContext(), "Произошла ошибка загрузки", Toast.LENGTH_SHORT)
                        Log.d("DishesCatalogFragment", it.message)
                    }
                }
            }
        }
    }
}