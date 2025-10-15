package com.example.chitealike.presentation.fragments

import android.R
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chitealike.MainApplication
import com.example.chitealike.databinding.FavoritesFragmentBinding
import com.example.chitealike.domain.usecases.DeleteFavoriteItemUseCase
import com.example.chitealike.presentation.adapters.FavoritesListAdapter
import com.example.chitealike.presentation.factories.ViewModelFactory
import com.example.chitealike.presentation.viewmodels.FavoritesListViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavoritesFragment : Fragment() {

    @Inject
    lateinit var adapter: FavoritesListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var deleteFavoriteItemUseCase: DeleteFavoriteItemUseCase


    private val binding by lazy {
        FavoritesFragmentBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (requireActivity().application as MainApplication).component
            .catalogComponentFactory()
            .create(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(FavoritesListViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavoritesListFromDB()


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
        component.inject(this)

        setupRecyclerView()
        setupObservers()
        setupListeners()


    }

    private fun setupListeners() {
        binding.fbtnAdd.setOnClickListener {
            FavoritesFragmentDirections.actionFavoritesFragmentToFavoritesCreateFragment().apply {
                findNavController().navigate(this)
            }

        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.favoritesList.collectLatest {
                when (it) {
                    is FavoritesListViewModel.FavoritesListState.Loading -> {
                        adapter.submitList(listOf())
                        binding.pb.visibility = View.VISIBLE
                        binding.rvFavorites.visibility = View.GONE
                    }

                    is FavoritesListViewModel.FavoritesListState.Success -> {
                        adapter.submitList(it.result)
                        binding.pb.visibility = View.GONE
                        binding.rvFavorites.visibility = View.VISIBLE

                    }

                    is FavoritesListViewModel.FavoritesListState.Error -> {
                        adapter.submitList(listOf())
                        Toast.makeText(
                            requireContext(),
                            "Произошла ошибка загрузки",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d("FavoritesFragment", it.message)
                        binding.pb.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun setupRecyclerView() {
        val rv = binding.rvFavorites
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        rv.itemAnimator = null
        rv.setHasFixedSize(true)


        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction){
                    ItemTouchHelper.LEFT -> {
                        lifecycleScope.launch {
                            val position = viewHolder.bindingAdapterPosition
                            deleteFavoriteItemUseCase.invoke(adapter.currentList[position])
                            viewModel.loadFavoritesListFromDB()
                        }
                    }

                    ItemTouchHelper.RIGHT -> {
                        val favoriteItem = adapter.currentList[viewHolder.bindingAdapterPosition]
                        FavoritesFragmentDirections.actionFavoritesFragmentToFavoritesEditFragment(favoriteItem.id).apply {
                            findNavController().navigate(this)
                        }
                    }
                }



            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.holo_red_light
                        )
                    )

                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.holo_orange_dark
                        )
                    )

                    .addSwipeLeftActionIcon(
                        R.drawable.ic_menu_delete
                    )
                    .addSwipeRightActionIcon(
                        R.drawable.ic_menu_edit
                    )
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        })

        itemTouchHelper.attachToRecyclerView(rv)
    }


}