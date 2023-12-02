package com.example.letsbuy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.letsbuy.databinding.BottomsheetFilterFragmentBinding
import com.example.letsbuy.listener.BottomSheetFilterListener
import com.example.letsbuy.model.enums.AdversimentColorEnum.Companion.colorToEnum
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFilterFragment(): BottomSheetDialogFragment() {
    private lateinit var binding: BottomsheetFilterFragmentBinding
    private lateinit var selectedColor: String
    private lateinit var selectQuality: String
    private lateinit var selectedCategory: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetFilterFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private var bottomSheetFilterListener: BottomSheetFilterListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callSpinnerColor()
        callSpinnerCategory()
        selectQuality = "Selecione a qualidade"

        binding.rdAll.setOnClickListener {
            binding.rdGood.isChecked = false
            binding.rdAverage.isChecked = false
            binding.rdBad.isChecked = false
            selectQuality = "Selecione a qualidade"
        }

        binding.rdGood.setOnClickListener {
            binding.rdAll.isChecked = false
            binding.rdAverage.isChecked = false
            binding.rdBad.isChecked = false
            selectQuality = "Novo"
        }

        binding.rdAverage.setOnClickListener {
            binding.rdAll.isChecked = false
            binding.rdGood.isChecked = false
            binding.rdBad.isChecked = false
            selectQuality = "Semi Novo"
        }

        binding.rdBad.setOnClickListener {
            binding.rdAll.isChecked = false
            binding.rdGood.isChecked = false
            binding.rdAverage.isChecked = false
            selectQuality = "Usado"
        }

        binding.btnClear.setOnClickListener {
            binding.spinnerCategory.setSelection(0)
            binding.spinnerColor.setSelection(0)
            //binding.spinnerColor.getItemAtPosition(0)
            //binding.spinnerColor.getItemAtPosition(0)
            binding.editLocation.setText("")
            binding.editPriceMin.setText("")
            binding.editPriceMax.setText("")
            binding.rdAll.isChecked = true
            binding.rdGood.isChecked = false
            binding.rdAverage.isChecked = false
            binding.rdBad.isChecked = false
            val search = Intent(context, SearchActivity::class.java)
            search.putExtra("LOCATION", "")
            search.putExtra("PRICEMIN", "")
            search.putExtra("PRICEMAX", "")
            search.putExtra("QUALITY", "")
            search.putExtra("CATEGORY", "")
            search.putExtra("COLOR", "")
            search.putExtra("FILTER", 1)
        }

        binding.btnSearch.setOnClickListener {
            val location = binding.editLocation.text.toString()
            val priceMin = binding.editPriceMin.text.toString()
            val priceMax = binding.editPriceMax.text.toString()
            val quality = selectQuality
            val category = selectedCategory
            val color = selectedColor
            val filter = 1
            val search = Intent(context, SearchActivity::class.java)
            search.putExtra("LOCATION", location)
            search.putExtra("PRICEMIN", if(priceMin.isNullOrEmpty()) null else priceMin.toDouble())
            search.putExtra("PRICEMAX", if(priceMax.isNullOrEmpty()) null else priceMax.toDouble())
            search.putExtra("QUALITY", if (quality == ("Selecione a qualidade")) null else quality)
            search.putExtra("CATEGORY", if (category == ("Selecione uma categoria")) null else category)
            search.putExtra("COLOR", if(color == ("Selecione uma cor")) null else color)
            search.putExtra("FILTER", filter)
            startActivity(search)
        }
    }

    private fun callSpinnerColor() {
        val types = resources.getStringArray(R.array.colors_array)
        val spinnerColor = binding.spinnerColor
        val arrayColorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)
        spinnerColor.adapter = arrayColorAdapter

        spinnerColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedColor = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun callSpinnerCategory() {
        val types = resources.getStringArray(R.array.category_array)
        val spinnerCategory = binding.spinnerCategory
        val arrayCategoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)
        spinnerCategory.adapter = arrayCategoryAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
}
