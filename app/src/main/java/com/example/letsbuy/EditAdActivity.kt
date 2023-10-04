package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityEditAdBinding
import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.model.AdvertisementResponse
import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.AdversimentColorEnum.Companion.colorToEnum
import com.example.letsbuy.model.enums.AdversimentColorEnum.Companion.enumColorToPosition
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.CategoryEnum.Companion.categoryToEnum
import com.example.letsbuy.model.enums.CategoryEnum.Companion.enumCategoryToPosition
import com.example.letsbuy.model.enums.QualityEnum
import com.example.letsbuy.model.enums.QualityEnum.Companion.enumQualityToPosition
import com.example.letsbuy.model.enums.QualityEnum.Companion.qualityToEnum
import com.example.letsbuy.service.AdversimentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAdActivity: AppCompatActivity() {

    private lateinit var binding: ActivityEditAdBinding
    private lateinit var userId: String
    private lateinit var adversimentId: String
    private lateinit var adversimentTitle: String
    private lateinit var adversimentDescription: String
    private lateinit var adversimentPrice: String
    private lateinit var selectedColor: String
    private lateinit var selectedCategory: String
    private lateinit var selectedQuality: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ad)
        binding = ActivityEditAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callSpinnerColor()
        callSpinnerCategory()
        callSpinnerQuality()

        val idAdversiment = intent.getLongExtra("idAdvertisement", 0)
        getAdversimentById(idAdversiment)

        binding.ivImageBack.setOnClickListener {
            val back = Intent(this, MyAdvertisementsActivity::class.java)
            startActivity(back)
        }

        binding.btnUpdate.setOnClickListener {
            adversimentTitle = binding.etTitle.text.toString()
            adversimentDescription = binding.etDescription.text?.toString()?.takeIf { it.isNotEmpty() } ?: "."
            adversimentPrice = binding.etPrice.text.toString()

            val isValid = validInputs()
            if(isValid) {
                updateAd()
            }
        }
    }

    private fun callSpinnerColor() {
        val types = resources.getStringArray(R.array.colors_array)
        val spinnerColor = binding.spinnerColor
        val arrayColorAdapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, types)
        spinnerColor.adapter = arrayColorAdapter

        spinnerColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedColor = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun callSpinnerCategory() {
        val types = resources.getStringArray(R.array.category_array)
        val spinnerCategory = binding.spinnerCategory
        val arrayCategoryAdapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, types)
        spinnerCategory.adapter = arrayCategoryAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun callSpinnerQuality() {
        val types = resources.getStringArray(R.array.quality_array)
        val spinnerQuality = binding.spinnerQuality
        val arrayQualityAdapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, types)
        spinnerQuality.adapter = arrayQualityAdapter

        spinnerQuality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedQuality = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun getAdversimentById(idAdversiment: Long) {
        val api = Rest.getInstance().create(AdversimentService::class.java)
        api.getAdversimentById(idAdversiment).enqueue(object: Callback<List<AllAdversimentsAndLikeDtoResponse>> {

            override fun onResponse(
                call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                response: Response<List<AllAdversimentsAndLikeDtoResponse>>
            ) {
                if(response.isSuccessful) {
                    val adversimentList = response.body()

                    if(!adversimentList.isNullOrEmpty()) {
                        val adversiment = adversimentList[0].adversiments
                        val editTitle = binding.etTitle
                        val editDescription = binding.etDescription
                        val editPrice = binding.etPrice
                        val spinnerColor = binding.spinnerColor
                        val spinnerCategory = binding.spinnerCategory
                        val spinnerQuality = binding.spinnerQuality

                        editTitle.setText(adversiment.title)
                        editDescription.setText(adversiment.description)
                        editPrice.setText(adversiment.price.toString())
                        spinnerColor.setSelection(enumColorToPosition(adversiment.color))
                        spinnerCategory.setSelection(enumCategoryToPosition(adversiment.category))
                        spinnerQuality.setSelection(enumQualityToPosition(adversiment.quality))

                        adversimentId = adversiment.id.toString()
                        userId = adversimentList[0].userId.toString()
                    }
                }
            }

            override fun onFailure(
                call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                t: Throwable
            ) {
                Toast.makeText(this@EditAdActivity, "Ocorreu um erro ao tentar editar o anúncio", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateAd() {
        val api = Rest.getInstance().create(AdversimentService::class.java)

        val adversimentDto = AdversimentDto(
            userId = userId.toLong(),
            title = adversimentTitle,
            description = adversimentDescription,
            price = adversimentPrice.toDouble(),
            color = colorToEnum(selectedColor),
            category = categoryToEnum(selectedCategory),
            quality = qualityToEnum(selectedQuality)
        )

        api.updateAdversiment(adversimentId.toLong(), adversimentDto).enqueue(object: Callback<AdvertisementResponse> {
            override fun onResponse(call: Call<AdvertisementResponse>, response: Response<AdvertisementResponse>) {
                if(response.isSuccessful) {
                    Toast.makeText(this@EditAdActivity, "Anúncio atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    val publish = Intent(this@EditAdActivity, MyAdvertisementsActivity::class.java)
                    startActivity(publish)
                }
            }

            override fun onFailure(call: Call<AdvertisementResponse>, t: Throwable) {
                Toast.makeText(this@EditAdActivity, "Verifique os campos que não foram preenchidos", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun validInputs(): Boolean {
        var valid = true
        val ivIconErrorTitle = binding.ivIconErrorTitle
        val tvMessageErrorTitle = binding.tvMessageErrorTitle

        val ivIconErrorColor = binding.ivIconErrorColor
        val tvMessageErrorColor = binding.tvMessageErrorColor

        val ivIconErrorCategory = binding.ivIconErrorCategory
        val tvMessageErrorCategory = binding.tvMessageErrorCategory

        val ivIconErrorQuality = binding.ivIconErrorQuality
        val tvMessageErrorQuality = binding.tvMessageErrorQuality

        val ivIconErrorPrice = binding.ivIconErrorPrice
        val tvMessageErrorPrice = binding.tvMessageErrorPrice

        if(adversimentTitle.isEmpty()) {
            valid = false
            ivIconErrorTitle.visibility = View.VISIBLE
            tvMessageErrorTitle.visibility = View.VISIBLE
        } else {
            ivIconErrorTitle.visibility = View.GONE
            tvMessageErrorTitle.visibility = View.GONE
        }

        if(colorToEnum(selectedColor) == AdversimentColorEnum.NOT_MAPPED) {
            valid = false
            ivIconErrorColor.visibility = View.VISIBLE
            tvMessageErrorColor.visibility = View.VISIBLE
        } else {
            ivIconErrorColor.visibility = View.GONE
            tvMessageErrorColor.visibility = View.GONE
        }

        if(categoryToEnum(selectedCategory) == CategoryEnum.NOT_MAPPED) {
            valid = false
            ivIconErrorCategory.visibility = View.VISIBLE
            tvMessageErrorCategory.visibility = View.VISIBLE
        } else {
            ivIconErrorCategory.visibility = View.GONE
            tvMessageErrorCategory.visibility = View.GONE
        }

        if(qualityToEnum(selectedQuality) == QualityEnum.NOT_MAPPED) {
            valid = false
            ivIconErrorQuality.visibility = View.VISIBLE
            tvMessageErrorQuality.visibility = View.VISIBLE
        } else {
            ivIconErrorQuality.visibility = View.GONE
            tvMessageErrorQuality.visibility = View.GONE
        }

        if(adversimentPrice.isEmpty() || adversimentPrice.toDouble() <= 0.0) {
            valid = false
            ivIconErrorPrice.visibility = View.VISIBLE
            tvMessageErrorPrice.visibility = View.VISIBLE
        } else {
            ivIconErrorPrice.visibility = View.GONE
            tvMessageErrorPrice.visibility = View.GONE
        }

        return valid
    }

}