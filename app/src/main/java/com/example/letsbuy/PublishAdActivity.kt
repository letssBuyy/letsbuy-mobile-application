package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
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

class PublishAdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublishAdBinding
    private lateinit var imageButton: ImageButton
    private lateinit var selectedColor: String
    private lateinit var selectedCategory: String
    private lateinit var selectedQuality: String

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_ad)

        binding = ActivityPublishAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPublish.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val price = binding.etPrice.text.toString()
            val color = colorToEnum(selectedColor)
            val category = categoryToEnum(selectedCategory)
            val quality = qualityToEnum(selectedQuality)

            val isValid = validInputs(title, price, color, category, quality)

            if(isValid) {
                publishAd(title, description, price, color, category, quality)
            }
        }

        binding.ivImageBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        // select color
        val spinnerColor: Spinner = findViewById(R.id.spinner_color)

        ArrayAdapter.createFromResource(
            this,
            R.array.colors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerColor.adapter = adapter
        }

        spinnerColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedColor = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // select category
        val spinnerCategory: Spinner = findViewById(R.id.spinner_category)

        ArrayAdapter.createFromResource(
            this,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //select quality
        val spinnerQuality: Spinner = findViewById(R.id.spinner_quality)

        ArrayAdapter.createFromResource(
            this,
            R.array.quality_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerQuality.adapter = adapter
        }

        spinnerQuality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedQuality = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        imageButton = findViewById(R.id.image_button_photo1)
        imageButton.setOnClickListener {
            pickImageGallery()
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageButton.setImageURI(data?.data)
        }
    }

    private fun validInputs(
        title: String,
        price: String,
        color: AdversimentColorEnum,
        category: CategoryEnum,
        quality: QualityEnum
    ): Boolean {
        var valid = true
        val ivIconErrorTitle = findViewById<ImageView>(R.id.iv_icon_error_title)
        val tvMessageErrorTitle = findViewById<TextView>(R.id.tv_message_error_title)

        val ivIconErrorColor = findViewById<ImageView>(R.id.iv_icon_error_color)
        val tvMessageErrorColor = findViewById<TextView>(R.id.tv_message_error_color)

        val ivIconErrorCategory = findViewById<ImageView>(R.id.iv_icon_error_category)
        val tvMessageErrorCategory = findViewById<TextView>(R.id.tv_message_error_category)

        val ivIconErrorQuality = findViewById<ImageView>(R.id.iv_icon_error_quality)
        val tvMessageErrorQuality = findViewById<TextView>(R.id.tv_message_error_quality)

        val ivIconErrorPrice = findViewById<ImageView>(R.id.iv_icon_error_price)
        val tvMessageErrorPrice = findViewById<TextView>(R.id.tv_message_error_price)

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

        return valid
    }
    private fun publishAd(
        title: String,
        description: String,
        price: String,
        color: AdversimentColorEnum,
        category: CategoryEnum,
        quality: QualityEnum
    ) {
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

            override fun onResponse(
                call: Call<AdvertisementResponse>,
                response: Response<AdvertisementResponse>
            ) {
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

