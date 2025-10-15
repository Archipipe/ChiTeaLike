package com.example.chitealike.presentation.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chitealike.MainApplication
import com.example.chitealike.R
import com.example.chitealike.databinding.FavoritesCreateFragmentBinding
import com.example.chitealike.domain.entities.FavoriteItem
import com.example.chitealike.domain.entities.TeaItem
import com.example.chitealike.domain.usecases.CreateFavoriteItemUseCase
import com.example.chitealike.domain.usecases.GetFavoriteItemUseCase
import com.example.chitealike.domain.usecases.GetTeaItemUseCase
import com.example.chitealike.domain.usecases.GetTeaListUseCase
import com.example.chitealike.domain.usecases.LoadImageUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesEditFragment: Fragment() {

    @Inject
    lateinit var editFavoriteItemUseCase: CreateFavoriteItemUseCase

    @Inject
    lateinit var getFavoriteItemUseCase: GetFavoriteItemUseCase

    @Inject
    lateinit var getTeaListUseCase: GetTeaListUseCase

    @Inject
    lateinit var getTeaItemUseCase: GetTeaItemUseCase

    @Inject
    lateinit var loadImageUseCase: LoadImageUseCase

    lateinit var teaList: ArrayList<TeaItem>

    private val args: FavoritesEditFragmentArgs by navArgs()

    private val binding by lazy{

        FavoritesCreateFragmentBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (requireActivity().application as MainApplication).component
            .catalogComponentFactory().create(this)
    }


    private var teaId: Int? = null


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


        lifecycleScope.launch{
            val favoriteItem = getFavoriteItem(args.favoriteId)
            setupEditLayout(favoriteItem)
        }



    }

    suspend private fun getFavoriteItem(favoriteItemId: Int): FavoriteItem{
        return try {
            getFavoriteItemUseCase.invoke(favoriteItemId)
        } catch (e: Exception){
            throw RuntimeException("No such an favorite element with id ${args.favoriteId}")
        }
    }


    private fun validateInputs(): Boolean{
        with (binding){
            tilName.setError(null)
            tilDescription.setError(null)
            if (teaNameInput.text.isNullOrEmpty()){
                tilName.setError("Заполните это поле")
                return false
            }

            if (teaDescriptionInput.text.isNullOrEmpty()){
                tilDescription.setError("Заполните это поле")
                return false
            }
        }
        return true
    }



    private fun setupEditLayout(favoriteItem: FavoriteItem) {
        with (binding){
            tvFavoritesTitle.text = "Изменить элемент"
            btnSaveFavorite.text = "Сохранить"

            loadTeaData()

            favoriteItem.teaId?.let {
                teaId = favoriteItem.teaId

                lifecycleScope.launch {


                    val teaItem = getTeaItemUseCase.invoke(favoriteItem.teaId)
                    teaItem.imageFileName?.let {

                        val bitmap = loadImageUseCase.invoke(teaItem.imageFileName)
                        bitmap?.let {
                            binding.selectedTeaImage.setImageDrawable(bitmap)
                        }
                    }
                }

            }

            teaNameInput.setText(favoriteItem.name)
            teaDescriptionInput.setText(favoriteItem.description)

            binding.btnSaveFavorite.setOnClickListener{
                if (validateInputs()){
                    val name = teaNameInput.text.toString().trim()
                    val description = teaDescriptionInput.text.toString().trim()
                    val newFavoriteItem = FavoriteItem(
                        id = favoriteItem.id,
                        teaId = teaId,
                        name = name,
                        description = description
                    )
                    lifecycleScope.launch {
                        editFavoriteItemUseCase.invoke(newFavoriteItem)
                        findNavController().popBackStack(R.id.favoritesFragment, false)

                    }
                }
            }

        }
    }

    private fun loadTeaData() {
        lifecycleScope.launch {
            teaList = getTeaList()
            setupDialog()
        }
    }

    private suspend fun getTeaList(): ArrayList<TeaItem> {

        return try {
            val teas = getTeaListUseCase.invoke()
            ArrayList<TeaItem>(teas)
        } catch (e: Exception) {
            ArrayList()
        }

    }

    private fun setupDialog(){

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window?.setLayout(850, 800)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val editText = dialog.findViewById<EditText>(R.id.edit_text)
        val listView = dialog.findViewById<ListView>(R.id.list_view)

        val list = teaList.map { it.name }.toMutableList()
        list.add(0,"Без чая")

        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_list_item_1, list )

        listView.adapter = adapter

        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }
        })

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val teaName = adapter.getItem(position)
                if (teaName == "Без чая") {
                    teaId = null
                    binding.selectedTeaName.text = "Выберите чай"
                    binding.selectedTeaImage.setImageResource(R.drawable.ic_tea_placeholder)

                } else {
                    val teaItem = teaList.find { it.name == teaName } ?: throw RuntimeException("Нет такого чая с названием $teaName")

                    teaId = teaItem.id
                    binding.selectedTeaName.text = teaItem.name
                    teaItem.imageFileName?.let {
                        lifecycleScope.launch {
                            val bitmap = loadImageUseCase.invoke(teaItem.imageFileName)
                            bitmap?.let {
                                binding.selectedTeaImage.setImageDrawable(it)
                            } ?: binding.selectedTeaImage.setImageResource(R.drawable.ic_tea_placeholder)
                        }
                    } ?: {
                        binding.selectedTeaImage.setImageResource(R.drawable.ic_tea_placeholder)
                    }

                }
                Log.d("FavoritesListAdapter", "ready to dispiss")
                dialog.dismiss()
            }

        })

        binding.btnChooseTea.setOnClickListener{
            dialog.show()
        }

    }



}