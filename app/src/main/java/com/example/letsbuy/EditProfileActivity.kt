package com.example.letsbuy

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityEditProfileBinding
import com.example.letsbuy.dto.AdvertisementResponse
import com.example.letsbuy.dto.BankAccount
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.dto.UserDtoResponse
import com.example.letsbuy.dto.UserUpdateDto
import com.example.letsbuy.service.ImageService
import com.example.letsbuy.service.UserService
import com.example.letsbuy.ui.perfil.PerfilFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var bankAccount: BankAccount
    private lateinit var imageViewProfile: ImageView
    private var changeImage = false
    private lateinit var imageUri: Uri
    private val PICK_MULTIPLE_IMAGES_FROM_GALLERY_REQUEST_CODE = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val userId = pref?.getString("ID", null)?.toLong()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageViewProfile = binding.titleAd

        binding.progressBarPhoto.visibility = View.VISIBLE
        getUserById(userId!!)
        binding.imageBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        binding.tituloedit.setOnClickListener {
            openGallery()

        }

        binding.btnUpdate.setOnClickListener {
            val name = findViewById<EditText>(R.id.inputName).text.toString()
            val cpf = findViewById<EditText>(R.id.inputCpf).text.toString()
            val dtBirth = findViewById<EditText>(R.id.inputDtBirth).text.toString()
            val neighboring = findViewById<EditText>(R.id.inputBairro).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.inputPhNumber).text.toString()
            val email = findViewById<EditText>(R.id.inputEmail).text.toString()
            val bankNumber = findViewById<EditText>(R.id.inputBank).text.toString()
            val bankAg = findViewById<EditText>(R.id.inputAg).text.toString()
            val number = findViewById<EditText>(R.id.inputNumero).text.toString().toLong()
            val cep = findViewById<EditText>(R.id.inputCep).text.toString()
            val complement = findViewById<EditText>(R.id.inputComplement).text.toString()
            val road = findViewById<EditText>(R.id.inputRua).text.toString()
            val bankCount = findViewById<EditText>(R.id.inputCount).text.toString()
            val state = findViewById<EditText>(R.id.inputState).text.toString()
            val city = findViewById<EditText>(R.id.inputCity).text.toString()
            //val id = idUser.toLong()
            //Log.w("id",id.toString())
            val bankAccount = BankAccount(null, bankNumber, bankAg, bankCount)
            val userUpdateDto = UserUpdateDto(
                name,
                email,
                cpf,
                dtBirth,
                phoneNumber,
                cep,
                road,
                number,
                neighboring,
                complement,
                state,
                city,
                bankAccount
            )
            val isValid = validInputs(name,email,cpf,dtBirth,phoneNumber,cep,road,
                number,
                neighboring,
                complement,
                state,
                city,
                bankAccount)
            if(isValid) {
                binding.progressBar.visibility = View.VISIBLE
                updateProfile(userUpdateDto)
            }
        }
    }

//    private fun pickImageGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        startActivityForResult(
//            Intent.createChooser(intent, "Selecione uma imagem para foto de perfil"),
//            PICK_MULTIPLE_IMAGES_FROM_GALLERY_REQUEST_CODE
//        )
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(requestCode == PICK_MULTIPLE_IMAGES_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            if(data.clipData != null) {
//                val count = data.clipData!!.itemCount
//                for(i in 0 until count) {
//                    val uri: Uri = data.clipData!!.getItemAt(i).uri
//                    val file = uri.path?.let { File(it) }
//                    file?.name?.let { Log.d("TAG_URI: ", it) }
//                    imageViewProfile.setImageURI(data.clipData!!.getItemAt(0).uri)
//                    imageUri = data.clipData!!.getItemAt(0).uri
//                }
//            } else {
//                val uri: Uri? = data.data
//                val file = uri?.path?.let { File(it) }
//                file?.name?.let { Log.d("TAG_PATH: ", it) }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            //imageViewProfile.setImageURI(selectedImageUri)
            imageUri = selectedImageUri!!
            changeImage = true
            setImage(selectedImageUri)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun getUserById(id: Long) {
        val api = Rest.getInstance().create(UserService::class.java)
        api.getAdversimentsByUser(id, null).enqueue(object : Callback<UserAdversimentsDtoResponse> {
            override fun onResponse(
                call: Call<UserAdversimentsDtoResponse>,
                response: Response<UserAdversimentsDtoResponse>
            ) {
                binding.progressBarPhoto.visibility = View.GONE
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        //var idUser = id
                        //idUser?.toLong()
                        //idUser = user.bankAccount?.id.toString()
                        binding.inputName.setText(user.name)
                        binding.inputCpf.setText(user.cpf)
                        binding.inputDtBirth.setText(user.birthDate)
                        binding.inputState.setText(user.state)
                        binding.inputCity.setText(user.city)
                        binding.inputPhNumber.setText(user.phoneNumber)
                        binding.inputBairro.setText(user.neighborhood)
                        binding.inputEmail.setText(user.email)
                        binding.inputBank.setText(user?.bankAccount?.bankNumber?.toString() ?: "")
                        binding.inputAg.setText(user.bankAccount?.agencyNumber?.toString() ?: "")
                        binding.inputNumero.setText(user.number.toString())
                        binding.inputCount.setText(user.bankAccount?.accountNumber?.toString() ?: "")
                        binding.inputCep.setText(user.cep)
                        binding.inputComplement.setText(user.complement)
                        binding.inputRua.setText(user.road)

                        Glide.with(this@EditProfileActivity)
                            .load(user.profileImage)
                            .error(
                                Glide.with(this@EditProfileActivity)
                                    .load(R.drawable.broke_image)
                                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            )
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(imageViewProfile)
                    }
                }
            }

            override fun onFailure(call: Call<UserAdversimentsDtoResponse>, t: Throwable) {
                binding.progressBarPhoto.visibility = View.GONE

            }
        })
    }

    private fun updateProfile(userUpdateDto: UserUpdateDto) {
        val api = Rest.getInstance().create(UserService::class.java)
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val userId = pref?.getString("ID", null)?.toLong()
        api.updateUser(userId, userUpdateDto).enqueue(object : Callback<UserDtoResponse> {
            override fun onResponse(
                call: Call<UserDtoResponse>,
                response: Response<UserDtoResponse>
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    if(changeImage) {
                        uploadUserImage(response.body()!!.id)
                    }
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Perfil Atualizado com Sucesso :) !",
                        Toast.LENGTH_SHORT
                    ).show()
                    val black = Intent(this@EditProfileActivity, HomeActivity::class.java)
                    startActivity(black)
                }
            }

            override fun onFailure(call: Call<UserDtoResponse>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(
                    this@EditProfileActivity,
                    "Verifique os campos que não foram preenchidos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun uploadUserImage(id: Long) {
        val paramName = "img"
        val imagePart = uriToMultipartBodyPart(this, imageUri, paramName)

        val api = Rest.getInstance().create(ImageService::class.java)

        api.uploadUserImage(id, imagePart!!).enqueue(object: Callback<UserDtoResponse> {
            override fun onResponse(
                call: Call<UserDtoResponse>,
                response: Response<UserDtoResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("UploadImages", "Response code: ${response.code()}")
                } else {
                    Toast.makeText(this@EditProfileActivity, "Falha ao enviar as imagens!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDtoResponse>, t: Throwable) {
                Log.e("UploadImages", "Erro ao enviar imagens", t)
                Toast.makeText(this@EditProfileActivity, "Ocorreu um erro ao enviar as imagens!", Toast.LENGTH_SHORT).show()
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

    private fun validInputs(name: String,
                            email: String,
                            cpf: String,
                            dtBirth: String,
                            phoneNumber: String,
                            cep: String,
                            road: String,
                            number: Long,
                            neighboring: String,
                            complement: String,
                            state: String,
                            city: String,
                            bankAccount: BankAccount): Boolean {
        var valid = true
        val ivIconErrorName = binding.ivIconErrorName
        val tvMessageErrorName = binding.tvMessageErrorName

        val ivIconErrorCpf = binding.ivIconErrorCpf
        val tvMessageErrorCpf = binding.tvMessageErrorCpf

        val ivIconErrorDtBirth = binding.ivIconErrorDtNasc
        val tvMessageErrorDtBirth = binding.tvMessageErrorDtNasc

        val ivIconErrorPhNumber = binding.ivIconErrorPhNumber
        val tvMessageErrorPhNumber = binding.tvMessageErrorPhNumber

        val ivIconErrorEmail = binding.ivIconErrorEmail
        val tvMessageErrorEmail = binding.tvMessageErrorEmail

        val ivIconErrorBkBank = binding.ivIconErrorBkBank
        val tvMessageErrorBkBank = binding.tvMessageErrorBkBank

        val ivIconErrorBkAgency = binding.ivIconErrorBkAgencia
        val tvMessageErrorBkAgency = binding.tvMessageErrorBkAgencia

        val ivIconErrorBkCount = binding.ivIconErrorBkCount
        val tvMessageErrorBkCount = binding.tvMessageErrorBkCount

        val ivIconErrorCep = binding.ivIconErrorCep
        val tvMessageErrorCep = binding.tvMessageErrorCep

        val ivIconErrorRoad = binding.ivIconErrorRua
        val tvMessageErrorRoad = binding.tvMessageErrorRua

        val ivIconErrorNumber = binding.ivIconErrorNumber
        val tvMessageErrorNumber = binding.tvMessageErrorNumber

        val ivIconErrorComplement = binding.ivIconErrorComp
        val tvMessageErrorComplement = binding.tvMessageErrorComp

        val ivIconErrorNeighborhood = binding.ivIconErrorBairro
        val tvMessageErrorNeighborhood = binding.tvMessageErrorBairro

        val ivIconErrorState = binding.ivIconErrorState
        val tvMessageErrorState = binding.tvMessageErrorState

        val ivIconErrorCity = binding.ivIconErrorCity
        val tvMessageErrorCity = binding.tvMessageErrorCity

        if(name.isEmpty()) {
            valid = false
            ivIconErrorName.visibility = View.VISIBLE
            tvMessageErrorName.visibility = View.VISIBLE
        } else {
            ivIconErrorName.visibility = View.GONE
            tvMessageErrorName.visibility = View.GONE
        }

        if(cpf.isEmpty()) {
            valid = false
            ivIconErrorCpf.visibility = View.VISIBLE
            tvMessageErrorCpf.visibility = View.VISIBLE
        } else {
            ivIconErrorCpf.visibility = View.GONE
            tvMessageErrorCpf.visibility = View.GONE
        }

        if(dtBirth.isEmpty()) {
            valid = false
            ivIconErrorDtBirth.visibility = View.VISIBLE
            tvMessageErrorDtBirth.visibility = View.VISIBLE
        } else {
            ivIconErrorDtBirth.visibility = View.GONE
            tvMessageErrorDtBirth.visibility = View.GONE
        }

        if(email.isEmpty()) {
            valid = false
            ivIconErrorEmail.visibility = View.VISIBLE
            tvMessageErrorEmail.visibility = View.VISIBLE
        } else {
            ivIconErrorEmail.visibility = View.GONE
            tvMessageErrorEmail.visibility = View.GONE
        }

        if(phoneNumber.isEmpty()) {
            valid = false
            ivIconErrorPhNumber.visibility = View.VISIBLE
            tvMessageErrorPhNumber.visibility = View.VISIBLE
        } else {
            ivIconErrorPhNumber.visibility = View.GONE
            tvMessageErrorPhNumber.visibility = View.GONE
        }

        if(bankAccount.accountNumber.isEmpty()) {
            valid = false
            ivIconErrorBkCount.visibility = View.VISIBLE
            tvMessageErrorBkCount.visibility = View.VISIBLE
        } else {
            ivIconErrorBkCount.visibility = View.GONE
            tvMessageErrorBkCount.visibility = View.GONE
        }

        if(bankAccount.bankNumber.isEmpty()) {
            valid = false
            ivIconErrorBkBank.visibility = View.VISIBLE
            tvMessageErrorBkBank.visibility = View.VISIBLE
        } else {
            ivIconErrorBkBank.visibility = View.GONE
            tvMessageErrorBkBank.visibility = View.GONE
        }

        if(bankAccount.agencyNumber.isEmpty()) {
            valid = false
            ivIconErrorBkAgency.visibility = View.VISIBLE
            tvMessageErrorBkAgency.visibility = View.VISIBLE
        } else {
            ivIconErrorBkAgency.visibility = View.GONE
            tvMessageErrorBkAgency.visibility = View.GONE
        }

        if(cep.isEmpty()) {
            valid = false
            ivIconErrorCep.visibility = View.VISIBLE
            tvMessageErrorCep.visibility = View.VISIBLE
        } else {
            ivIconErrorCep.visibility = View.GONE
            tvMessageErrorCep.visibility = View.GONE
        }

        if(road.isEmpty()) {
            valid = false
            ivIconErrorRoad.visibility = View.VISIBLE
            tvMessageErrorRoad.visibility = View.VISIBLE
        } else {
            ivIconErrorRoad.visibility = View.GONE
            tvMessageErrorRoad.visibility = View.GONE
        }

        if(complement.isEmpty()) {
            valid = false
            ivIconErrorComplement.visibility = View.VISIBLE
            tvMessageErrorComplement.visibility = View.VISIBLE
        } else {
            ivIconErrorComplement.visibility = View.GONE
            tvMessageErrorComplement.visibility = View.GONE
        }

        if(neighboring.isEmpty()) {
            valid = false
            ivIconErrorNeighborhood.visibility = View.VISIBLE
            tvMessageErrorNeighborhood.visibility = View.VISIBLE
        } else {
            ivIconErrorNeighborhood.visibility = View.GONE
            tvMessageErrorNeighborhood.visibility = View.GONE
        }

        if(state.isEmpty()) {
            valid = false
            ivIconErrorState.visibility = View.VISIBLE
            tvMessageErrorState.visibility = View.VISIBLE
        } else {
            ivIconErrorState.visibility = View.GONE
            tvMessageErrorState.visibility = View.GONE
        }

        if(city.isEmpty()) {
            valid = false
            ivIconErrorCity.visibility = View.VISIBLE
            tvMessageErrorCity.visibility = View.VISIBLE
        } else {
            ivIconErrorCity.visibility = View.GONE
            tvMessageErrorCity.visibility = View.GONE
        }

        if(number.toString().isEmpty() || number <= 0) {
            valid = false
            ivIconErrorNumber.visibility = View.VISIBLE
            tvMessageErrorNumber.visibility = View.VISIBLE
        } else {
            ivIconErrorNumber.visibility = View.GONE
            tvMessageErrorNumber.visibility = View.GONE
        }

        return valid
    }


    private fun drawCircularImage(imageURI: Uri? = null) {
        var bitmapDrawable: Bitmap? = null
        var picture: Drawable? = null
        if (imageURI == null) {
            picture = ContextCompat.getDrawable(baseContext, R.drawable.circle_profile_image)
            bitmapDrawable = (picture as BitmapDrawable).bitmap
        } else {
            val contentResolver: ContentResolver = applicationContext.contentResolver
            try {
                picture = BitmapFactory
                    .decodeStream(contentResolver.openInputStream(imageURI))
                    .toDrawable(resources)
                bitmapDrawable = picture.bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (picture is BitmapDrawable && bitmapDrawable is Bitmap) {
            val pictureIsPortrait = picture.isPortrait()
            val pictureWidth: Int
            val pictureHeight: Int
            with(baseContext.resources.displayMetrics) {
                val pictureAspectRatio = picture.aspectRatio()
                if (pictureIsPortrait) {
                    pictureWidth = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 200f, this
                    ).toInt()
                    pictureHeight = (pictureWidth / pictureAspectRatio).toInt()
                } else {
                    pictureHeight = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 200f, this
                    ).toInt()
                    pictureWidth = (pictureHeight * pictureAspectRatio).toInt()
                }
            }
            val dimension = if (pictureIsPortrait) pictureWidth else pictureHeight
            val outputBitmap = Bitmap.createBitmap(dimension, dimension, Bitmap.Config.ARGB_8888)

            Canvas(outputBitmap).apply {
                val bitmapDrawableScaled = Bitmap.createScaledBitmap(
                    bitmapDrawable, pictureWidth, pictureHeight, false
                )

                val x: Int
                val y: Int
                if (pictureIsPortrait) {
                    x = 0
                    y = -((bitmapDrawableScaled.height - outputBitmap.height) / 2)
                } else {
                    x = -((bitmapDrawableScaled.width - outputBitmap.width) / 2)
                    y = 0
                }

                val paint = Paint()
                    .apply { isAntiAlias = true }
                    .also {
                        val circleRadius = outputBitmap.width / 2f
                        drawCircle(circleRadius, circleRadius, circleRadius, it)
                    }
                Rect(
                    x, y, bitmapDrawableScaled.width + x, bitmapDrawableScaled.height + y
                ).also { destination ->
                    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                    drawBitmap(bitmapDrawableScaled, null, destination, paint)
                }
            }
            BitmapDrawable(resources, outputBitmap).also {
                findViewById<ImageView>(R.id.titleAd).setImageBitmap(it.bitmap)
            }
        }
    }

    fun BitmapDrawable.isPortrait(): Boolean {
        return this.aspectRatio() < 1
    }

    fun BitmapDrawable.aspectRatio(): Double {
        return this.bitmap.width.toDouble() / this.bitmap.height.toDouble()
    }

    private fun setImage(imageURI: Uri) {
        drawCircularImage(imageURI)
    }
}