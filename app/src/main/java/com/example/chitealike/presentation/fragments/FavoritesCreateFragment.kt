package com.example.chitealike.presentation.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.Insets.add
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
import android.widget.Spinner
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
import com.example.chitealike.domain.usecases.GetTeaListUseCase
import com.example.chitealike.domain.usecases.LoadImageUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesCreateFragment : Fragment() {

    @Inject
    lateinit var createFavoriteItemUseCase: CreateFavoriteItemUseCase

    @Inject
    lateinit var getTeaListUseCase: GetTeaListUseCase

    @Inject
    lateinit var loadImageUseCase: LoadImageUseCase

    lateinit var teaList: ArrayList<TeaItem>

    private val binding by lazy {
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


        setupCreateLayout()


    }



    private fun setupCreateLayout() {

        binding.tvFavoritesTitle.text = "Добавить в избранное"
        binding.btnSaveFavorite.text = "Сохранить в избранное"

        loadTeaData()

        binding.btnSaveFavorite.setOnClickListener {
            if (validateInputs()) {
                val name = binding.teaNameInput.text.toString().trim()
                val description = binding.teaDescriptionInput.text.toString().trim()
                val favoriteItem = FavoriteItem(
                    id = 0,
                    teaId = teaId,
                    name = name,
                    description = description
                )
                lifecycleScope.launch {
                    createFavoriteItemUseCase.invoke(favoriteItem)
                    findNavController().popBackStack(R.id.favoritesFragment, false)
                }
            }
        }

    }

    private fun loadTeaData() {
        lifecycleScope.launch {
            teaList = getTeaList()
            Log.d("FavoritesCreateFragment", teaList.toString())
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

        editText.addTextChangedListener(object: TextWatcher{
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
                Log.d("FavoritesListAdapter", "readydismiss")
                dialog.dismiss()
            }


        })

        binding.btnChooseTea.setOnClickListener{
            dialog.show()
        }

    }

    private fun validateInputs(): Boolean {
        with(binding) {
            tilName.setError(null)
            tilDescription.setError(null)
            if (teaNameInput.text.isNullOrEmpty()) {
                tilName.setError("Заполните это поле")
                return false
            }

            if (teaDescriptionInput.text.isNullOrEmpty()) {
                tilDescription.setError("Заполните это поле")
                return false
            }
        }
        return true
    }

}

