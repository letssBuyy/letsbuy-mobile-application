package com.example.letsbuy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.models.SlideModel
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityAdDetailBinding
import com.example.letsbuy.dto.ChatRequest
import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.dto.ImageDtoResponse
import com.example.letsbuy.model.DetailAdvertisementResponse
import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.service.AdDetailService
import com.example.letsbuy.service.ChatService
import com.example.letsbuy.service.LikeService
import com.example.letsbuy.ui.chat.ChatFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAdDetailBinding

    private var idAdversiment: Long = 0
    private var idUser: Long = 0
    private var isLiked: Boolean = false
    private var likeId: Int? = 0
    private var sellerId: Long = -1

    private var sellerName: String = ""
    private var sellerImageProfile: String = ""
    private var adversimentImage: String = ""
    private var adversimentTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityAdDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idAdversiment = intent.getLongExtra("ID_AD", -1)
        val prefs = getSharedPreferences("AUTH", MODE_PRIVATE)
        idUser = prefs.getString("ID", "")?.toLong() ?: -1

        binding.imageView.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
        getDetailData()
        bindLayoutEvents()
    }

    fun bindLayoutEvents() {
        binding.likeImageView.setOnClickListener {
            if (isLiked) {
                unLike()
            } else {
                like()
            }
        }

        binding.userChatButton.setOnClickListener {
            createChat()
        }

        binding.Buybutton.setOnClickListener {
            val comprar = Intent(this, CheckoutPaymentActivity::class.java)
            comprar.putExtra("ID_AD", idAdversiment)
            startActivity(comprar)
        }

        binding.userNameTextView.setOnClickListener {
            val back = Intent(this, ProfileViewActivity::class.java)
            back.putExtra("sellerId", sellerId)
            startActivity(back)
        }
    }

    fun sendChatMessage(chatId: Long) {
        val chatMessage = Intent(this, ChatMessageActivity::class.java)
        chatMessage.putExtra("CHAT_ID", chatId)
        chatMessage.putExtra("PARTNER_ID", sellerId)
        chatMessage.putExtra("PARTNER_NAME", sellerName)
        chatMessage.putExtra("PARTNER_IMAGE", sellerImageProfile)
        chatMessage.putExtra("ADVERSIMENT_IMAGE", adversimentImage)
        chatMessage.putExtra("ADVERSIMENT_TITLE", adversimentTitle)
        startActivity(chatMessage)
    }

    private fun createChat() {
        val api = Rest.getInstance().create(ChatService::class.java)
        val chatRequest = ChatRequest(sellerId, idUser, idAdversiment)
        api.createChat(chatRequest).enqueue(object : Callback<ChatResponseDto> {
            override fun onResponse(
                call: Call<ChatResponseDto>,
                response: Response<ChatResponseDto>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if (data?.id != null) {
                        sendChatMessage(data.id)
                    }
                } else {
                    showToast("Não foi possivel ir para o chat")
                }
            }

            override fun onFailure(call: Call<ChatResponseDto>, t: Throwable) {
                showToast("Não foi possivel ir para o chat")
            }
        })
    }

    fun like() {
        Log.w("respostaApi", "aqui fora no like")

        val api = Rest.getInstance().create(LikeService::class.java)

        api.like(idAdversiment, idUser).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("respostaApi", "cai no sucesso")
                    isLiked = true
                    binding.likeImageView.setImageResource(R.drawable.icon_heart_selected)
                } else {
                    Log.e("respostaApi", "Erro na requisição: ${response}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("respostaApi", "Ocorreu um erro ao curtir, tente novamente")
            }
        })
    }

    fun unLike() {
        Log.w("respostaApi", "aqui fora no unlike")

        val api = Rest.getInstance().create(LikeService::class.java)

        likeId?.toLong()?.let { id ->
            api.unLike(id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        isLiked = false
                        binding.likeImageView.setImageResource(R.drawable.heart)
                        Log.w("respostaApi", "aqui no sucesso")
                    } else {
                        Log.e("respostaApi", "Erro na requisição: $response")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("respostaApi", "Ocorreu um erro ao descurtir, tente novamente")
                }
            })
        } ?: Log.d("respostaApi", "likeId is null or not a valid Long")
    }

    fun converterData(data: String?): String {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatoSaida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return try {
            val dataObjeto: Date = formatoEntrada.parse(data)
            formatoSaida.format(dataObjeto)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    private fun loadImagesIntoSlider(images: List<ImageDtoResponse>?) {
        val imageList = ArrayList<SlideModel>()

        images?.forEach { image ->
            imageList.add(SlideModel(image.url))
        }

        val imageSlider = binding.cardMainImageView
        imageSlider.setImageList(imageList)
    }

    private fun loadUserProfileImage(image: ImageDtoResponse?) {
        image?.let { userProfileImage ->
            Picasso.get()
                .load(userProfileImage.url)
                .transform(CircleTransform()) // Aplica a transformação para tornar a imagem redonda
                .into(binding.userProfileImageView)
        }
    }

    class CircleTransform : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            paint.shader = shader
            paint.isAntiAlias = true

            val radius = size / 2f
            canvas.drawCircle(radius, radius, radius, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }

    fun getDetailData() {
        val api = Rest.getInstance().create(AdDetailService::class.java)

        api.getDetail(idAdversiment, idUser).enqueue(object: Callback<List<DetailAdvertisementResponse>> {
            override fun onResponse(
                call: Call<List<DetailAdvertisementResponse>>,
                response: Response<List<DetailAdvertisementResponse>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.first()

                    isLiked = data?.isLike ?: false
                    sellerId = data?.adversiments?.userSellerLikeDto!!.id

                    sellerName = data.adversiments.userSellerLikeDto.name
                    sellerImageProfile = data.adversiments.userSellerLikeDto.profileImage
                    adversimentImage = data.adversiments.images?.first()?.url ?: ""
                    adversimentTitle = data.adversiments.title

                    if (isLiked) {
                        likeId = data?.likeId
                        binding.likeImageView.setImageResource(R.drawable.icon_heart_selected)
                    } else {
                        binding.likeImageView.setImageResource(R.drawable.heart)
                    }

                    binding.titleTextView.text = data?.adversiments?.title
                    binding.dateTextView.text = converterData(data?.adversiments?.postDate)
                    binding.priceTextView.text = "R$ " + data?.adversiments?.price.toString()
                    binding.descriptionTextView.text = data?.adversiments?.description

                    binding.categoryTextView.text = CategoryEnum.enumCategoryToDescription(data?.adversiments.category)

                    binding.colorTextView.text = AdversimentColorEnum.enumColorToDescription(data?.adversiments.color)

                    binding.userNameTextView.text = data?.adversiments?.userSellerLikeDto?.name
                    binding.userCityTextView.text = data?.adversiments?.userSellerLikeDto?.city

                    data?.adversiments?.images?.let { images ->
                        loadImagesIntoSlider(images)
                    }

                    val userProfileImage: ImageDtoResponse? = data?.adversiments?.userSellerLikeDto?.profileImage?.let {
                        ImageDtoResponse(url = it)
                    }

                    loadUserProfileImage(userProfileImage)
                }
            }

            override fun onFailure(call: Call<List<DetailAdvertisementResponse>>, t: Throwable) {
                Log.e("getDetailData", "Falha na requisição: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG).show()
    }
}