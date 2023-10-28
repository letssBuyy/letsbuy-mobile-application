package com.example.letsbuy

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.net.toUri
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityPublishAdBinding
import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.dto.AdvertisementResponse
import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.AdversimentColorEnum.Companion.colorToEnum
import com.example.letsbuy.model.enums.AdversimentColorEnum.NOT_MAPPED
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.CategoryEnum.Companion.categoryToEnum
import com.example.letsbuy.model.enums.QualityEnum
import com.example.letsbuy.model.enums.QualityEnum.Companion.qualityToEnum
import com.example.letsbuy.service.AdversimentService
import com.example.letsbuy.service.ImageService
import com.example.letsbuy.ui.perfil.PerfilFragment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI

class PublishAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublishAdBinding
    private lateinit var selectedColor: String
    private lateinit var selectedCategory: String
    private lateinit var selectedQuality: String
    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var imageButton3: ImageButton
    private lateinit var imageButton4: ImageButton
    private lateinit var imageUri1: Uri
    private lateinit var imageUri2: Uri
    private lateinit var imageUri3: Uri
    private lateinit var imageUri4: Uri
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

        binding.ivImageBack.setOnClickListener {
            val back = Intent(this, PerfilActivity::class.java)
            startActivity(back)
        }

        binding.imageButtonPhoto1.setOnClickListener {
            pickImageGallery()
        }
        binding.imageButtonPhoto2.setOnClickListener {
            pickImageGallery()
        }
        binding.imageButtonPhoto3.setOnClickListener {
            pickImageGallery()
        }
        binding.imageButtonPhoto4.setOnClickListener {
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
                binding.progressBar.visibility = View.VISIBLE
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
                        0 -> {
                            imageButton1.setImageURI(data.clipData!!.getItemAt(0).uri)
                            imageUri1 = data.clipData!!.getItemAt(0).uri
                        }
                        1 -> {
                            imageButton2.setImageURI(data.clipData!!.getItemAt(1).uri)
                            imageUri2 = data.clipData!!.getItemAt(1).uri
                        }
                        2 -> {
                            imageButton3.setImageURI(data.clipData!!.getItemAt(2).uri)
                            imageUri3 = data.clipData!!.getItemAt(2).uri
                        }
                        3 -> {
                            imageButton4.setImageURI(data.clipData!!.getItemAt(3).uri)
                            imageUri4 = data.clipData!!.getItemAt(3).uri
                        }
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
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()

        val api = Rest.getInstance().create(AdversimentService::class.java)

        val adversimentDto = AdversimentDto(
            userId = id!!,
            title = title,
            description = description,
            price = price.toDouble(),
            color = color,
            category = category,
            quality = quality
        )

        api.createAdversiment(adversimentDto).enqueue(object: Callback<AdvertisementResponse> {
            override fun onResponse(call: Call<AdvertisementResponse>, response: Response<AdvertisementResponse>) {
                binding.progressBar.visibility = View.GONE
                if(response.isSuccessful) {
                    uploadImages(response.body()!!.id)
                    Toast.makeText(this@PublishAdActivity, "Anúncio criado com sucesso!", Toast.LENGTH_SHORT).show()
                    val publish = Intent(this@PublishAdActivity, HomeActivity::class.java)
                    startActivity(publish)
                }
            }

            override fun onFailure(call: Call<AdvertisementResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PublishAdActivity, "Ocorreu um erro ao publicar o anúncio!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uriToMultipartBodyPart(context: Context, uri: Uri, paramName: String): MultipartBody.Part? {
        val maxWidth = 800
        val maxHeight = 600
        val quality = 80

        try {
            val inputStream = context.contentResolver.openInputStream(uri)

            val fileExtension = getFileExtension(context, uri)
            val fileName = "${System.currentTimeMillis()}.$fileExtension"

            val file = File(context.cacheDir, fileName)

            inputStream?.use { input ->
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = false
                val bitmap = BitmapFactory.decodeStream(input, null, options)

                val scaledBitmap = scaleBitmap(bitmap!!, maxWidth, maxHeight)

                val outputStream = FileOutputStream(file)
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.close()
            }

            val mediaType = "image/$fileExtension".toMediaTypeOrNull()
            val requestFile = file.asRequestBody(mediaType)
            return MultipartBody.Part.createFormData(paramName, fileName, requestFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        val resolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri)) ?: "jpg"
    }

    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val scaleWidth: Float
        val scaleHeight: Float

        if (originalWidth > originalHeight) {
            scaleWidth = maxWidth.toFloat() / originalWidth
            scaleHeight = maxHeight.toFloat() / originalHeight
        } else {
            scaleWidth = maxHeight.toFloat() / originalWidth
            scaleHeight = maxWidth.toFloat() / originalHeight
        }

        val matrix = android.graphics.Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)
    }


    private fun uploadImages(id: Long) {
        val paramName1 = "img1"
        val imagePart1 = uriToMultipartBodyPart(this, imageUri1, paramName1)

        val paramName2 = "img2"
        val imagePart2 = uriToMultipartBodyPart(this, imageUri2, paramName2)

        val paramName3 = "img3"
        val imagePart3 = uriToMultipartBodyPart(this, imageUri3, paramName3)

        val paramName4 = "img4"
        val imagePart4 = uriToMultipartBodyPart(this, imageUri4, paramName4)

        val api = Rest.getInstance().create(ImageService::class.java)

        api.uploadImages(id, imagePart1!!, imagePart2!!, imagePart3!!, imagePart4!!).enqueue(object: Callback<AdvertisementResponse> {
            override fun onResponse(call: Call<AdvertisementResponse>, response: Response<AdvertisementResponse>) {
                if (response.isSuccessful) {
                    Log.d("UploadImages", "Response code: ${response.code()}")
                } else {
                    Toast.makeText(this@PublishAdActivity, "Falha ao enviar as imagens!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AdvertisementResponse>, t: Throwable) {
                Log.e("UploadImages", "Erro ao enviar imagens", t)
                Toast.makeText(this@PublishAdActivity, "Ocorreu um erro ao enviar as imagens!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

