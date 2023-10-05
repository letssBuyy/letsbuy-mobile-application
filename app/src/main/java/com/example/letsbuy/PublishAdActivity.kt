package com.example.letsbuy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityPublishAdBinding
import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.model.AdvertisementResponse
import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.AdversimentColorEnum.Companion.colorToEnum
import com.example.letsbuy.model.enums.AdversimentColorEnum.NOT_MAPPED
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.CategoryEnum.Companion.categoryToEnum
import com.example.letsbuy.model.enums.QualityEnum
import com.example.letsbuy.model.enums.QualityEnum.Companion.qualityToEnum
import com.example.letsbuy.service.AdversimentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PublishAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublishAdBinding
    private lateinit var selectedColor: String
    private lateinit var selectedCategory: String
    private lateinit var selectedQuality: String
    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var imageButton3: ImageButton
    private lateinit var imageButton4: ImageButton
    private var photoCounter = 0
    private val PICK_MULTIPLE_IMAGES_FROM_GALLERY_REQUEST_CODE = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_ad)
        binding = ActivityPublishAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callSpinnerColor()
        callSpinnerCategory()
        callSpinnerQuality()
        getImages()

        binding.tvPublishAdTitle.setOnClickListener {
            val back = Intent(this, ProfileViewActivity::class.java)
            startActivity(back)
        }

        binding.ivImageBack.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }


        binding.imageButtonPhoto1.setOnClickListener {
            pickImageGallery()
        }

        binding.btnPublish.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text?.toString()?.takeIf { it.isNotEmpty() } ?: "."
            val price = binding.etPrice.text.toString()
            val color = colorToEnum(selectedColor)
            val category = categoryToEnum(selectedCategory)
            val quality = qualityToEnum(selectedQuality)

            val isValid = validInputs(title, price, color, category, quality)

            if(isValid) {
                publishAd(title, description, price, color, category, quality)
            }
        }
    }

    private fun getImages() {
        imageButton1 = binding.imageButtonPhoto1
        imageButton2 = binding.imageButtonPhoto2
        imageButton3 = binding.imageButtonPhoto3
        imageButton4 = binding.imageButtonPhoto4
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

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Selecione 4 fotos"),
            PICK_MULTIPLE_IMAGES_FROM_GALLERY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_MULTIPLE_IMAGES_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if(data.clipData != null) {
                photoCounter = 0
                val count = data.clipData!!.itemCount
                for(i in 0 until count) {
                    val uri: Uri = data.clipData!!.getItemAt(i).uri
                    val file = uri.path?.let { File(it) }
                    file?.name?.let { Log.d("TAG_URI: ", it) }
                    photoCounter++

                    when(i){
                        0 -> imageButton1.setImageURI(data.clipData!!.getItemAt(0).uri)
                        1 -> imageButton2.setImageURI(data.clipData!!.getItemAt(1).uri)
                        2 -> imageButton3.setImageURI(data.clipData!!.getItemAt(2).uri)
                        3 -> imageButton4.setImageURI(data.clipData!!.getItemAt(3).uri)
                    }
                    if(photoCounter >= 4) {
                        Toast.makeText(
                            this,
                            "Você já selecionou 4 fotos",
                            Toast.LENGTH_SHORT
                        ).show()
                       return
                    }
                }
            } else {
                val uri: Uri? = data.data
                val file = uri?.path?.let { File(it) }
                file?.name?.let { Log.d("TAG_PATH: ", it) }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun validInputs(title: String, price: String, color: AdversimentColorEnum, category: CategoryEnum, quality: QualityEnum): Boolean {
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

        val ivIconErrorImage = binding.ivIconErrorImage
        val tvMessageErrorImage = binding.tvMessageErrorImage

        val imageOld1 = binding.imageButtonPhotoOld1.drawable.constantState
        val imageOld234 = binding.imageButtonPhotoOld234.drawable.constantState

        if(title.isEmpty()) {
            valid = false
            ivIconErrorTitle.visibility = View.VISIBLE
            tvMessageErrorTitle.visibility = View.VISIBLE
        } else {
            ivIconErrorTitle.visibility = View.GONE
            tvMessageErrorTitle.visibility = View.GONE
        }

        if(color == NOT_MAPPED) {
            valid = false
            ivIconErrorColor.visibility = View.VISIBLE
            tvMessageErrorColor.visibility = View.VISIBLE
        } else {
            ivIconErrorColor.visibility = View.GONE
            tvMessageErrorColor.visibility = View.GONE
        }

        if(category == CategoryEnum.NOT_MAPPED) {
            valid = false
            ivIconErrorCategory.visibility = View.VISIBLE
            tvMessageErrorCategory.visibility = View.VISIBLE
        } else {
            ivIconErrorCategory.visibility = View.GONE
            tvMessageErrorCategory.visibility = View.GONE
        }

        if(quality == QualityEnum.NOT_MAPPED) {
            valid = false
            ivIconErrorQuality.visibility = View.VISIBLE
            tvMessageErrorQuality.visibility = View.VISIBLE
        } else {
            ivIconErrorQuality.visibility = View.GONE
            tvMessageErrorQuality.visibility = View.GONE
        }

        if(price.isEmpty() || price.toDouble() <= 0.0) {
            valid = false
            ivIconErrorPrice.visibility = View.VISIBLE
            tvMessageErrorPrice.visibility = View.VISIBLE
        } else {
            ivIconErrorPrice.visibility = View.GONE
            tvMessageErrorPrice.visibility = View.GONE
        }

        if(
            imageButton1.drawable.constantState!! == imageOld1 ||
            imageButton2.drawable.constantState!! == imageOld234 ||
            imageButton3.drawable.constantState!! == imageOld234 ||
            imageButton4.drawable.constantState!! == imageOld234
        ) {
            valid = false
            ivIconErrorImage.visibility = View.VISIBLE
            tvMessageErrorImage.visibility = View.VISIBLE
        } else {
            ivIconErrorImage.visibility = View.GONE
            tvMessageErrorImage.visibility = View.GONE
        }

        return valid
    }
    private fun publishAd(title: String, description: String, price: String, color: AdversimentColorEnum, category: CategoryEnum, quality: QualityEnum) {
        val api = Rest.getInstance().create(AdversimentService::class.java)

        val adversimentDto = AdversimentDto(
            userId = 9L,
            title = title,
            description = description,
            price = price.toDouble(),
            color = color,
            category = category,
            quality = quality
        )

        api.createAdversiment(adversimentDto).enqueue(object: Callback<AdvertisementResponse> {
            override fun onResponse(call: Call<AdvertisementResponse>, response: Response<AdvertisementResponse>) {
                if(response.isSuccessful) {
                    Toast.makeText(this@PublishAdActivity, "Anúncio criado com sucesso!", Toast.LENGTH_SHORT).show()
                    val publish = Intent(this@PublishAdActivity, HomeActivity::class.java)
                    startActivity(publish)
                }
            }

            override fun onFailure(call: Call<AdvertisementResponse>, t: Throwable) {
                Toast.makeText(this@PublishAdActivity, "Verifique os campos que não foram preenchidos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

