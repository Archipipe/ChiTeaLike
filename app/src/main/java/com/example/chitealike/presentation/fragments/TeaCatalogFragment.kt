package com.example.chitealike.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chitealike.MainApplication
import com.example.chitealike.R
import com.example.chitealike.databinding.TeaCatalogFragmentBinding
import com.example.chitealike.domain.enums.TeaType
import com.example.chitealike.presentation.adapters.TeaListAdapter
import com.example.chitealike.presentation.viewmodels.TeaListViewModel
import com.example.chitealike.presentation.factories.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class TeaCatalogFragment : Fragment() {

    @Inject
    lateinit var adapter: TeaListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(TeaListViewModel::class.java)
    }

    private val component by lazy {
        (requireActivity().application as MainApplication).component
            .catalogComponentFactory()
            .create(this)
    }

    private val binding by lazy {
        TeaCatalogFragmentBinding.inflate(layoutInflater)
    }

    private var filterTag: TeaType? = null
    private var filterSearch: String? = null
    private var isCatalogActivated = false

    private val buttonList by lazy {
        listOf(
            R.id.rb_tea_type_any,
            R.id.rb_tea_type_red,
            R.id.rb_tea_type_green,
            R.id.rb_tea_type_black,
            R.id.rb_tea_type_yellow,
            R.id.rb_tea_type_white,
            R.id.rb_tea_type_oolong,
            R.id.rb_tea_type_puer,
        ).map {view?.findViewById<RadioButton>(it)}
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(this)

        setupRecyclerView()
        subscribeObservers()
        setupToolbar()
    }

    private fun setClickListenerToRadioButton(
        view: RadioButton,
        callback: ((teaType: TeaType?) -> Unit)
    ) {
        val type = when (view.id) {
            R.id.rb_tea_type_red -> TeaType.Red
            R.id.rb_tea_type_green -> TeaType.Green
            R.id.rb_tea_type_black -> TeaType.Black
            R.id.rb_tea_type_yellow -> TeaType.Yellow
            R.id.rb_tea_type_white -> TeaType.White
            R.id.rb_tea_type_oolong -> TeaType.Oolong
            R.id.rb_tea_type_puer -> TeaType.Puer
            else -> null
        }



        view.setOnClickListener {
            buttonList.forEach {
                if (it != view){
                    it!!.isChecked = false
                }
            }
            view.isChecked = true
            callback(type)
        }
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

        binding.rbTeaTypeAny.isChecked = true
        binding.svCatalog.setQuery("", false)


        buttonList.forEach {
            setClickListenerToRadioButton(it!!) { type ->
                filterTag = type
            }
        }



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
            viewModel.teaFilterSearch(filterSearch, filterTag)
        }

    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.teaList.collectLatest {
                when (it) {
                    is TeaListViewModel.TeaListState.Loading -> {
                        binding.pb.visibility = View.VISIBLE
                        adapter.submitList(listOf())
                    }

                    is TeaListViewModel.TeaListState.Error -> {
                        Log.d("TeaCatalogFragment", it.message)
                        binding.pb.visibility = View.GONE
                        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_LONG)
                            .show()
                    }

                    is TeaListViewModel.TeaListState.Success -> {
                        binding.pb.visibility = View.GONE
                        adapter.submitList(it.result)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val rvTeaCatalog = binding.rvTeaCatalog
        rvTeaCatalog.layoutManager = LinearLayoutManager(requireContext())
        rvTeaCatalog.adapter = adapter

        adapter.onItemClickListener = {
            TeaCatalogFragmentDirections.actionTeaCatalogFragmentToTeaItemFragment(it).apply {
                findNavController().navigate(this)
            }
        }
    }

}