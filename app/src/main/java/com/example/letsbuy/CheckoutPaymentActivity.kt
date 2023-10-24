package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityCheckoutPaymentBinding
import com.example.letsbuy.dto.ImageDtoResponse
import com.example.letsbuy.dto.PaymentRequest
import com.example.letsbuy.model.DetailAdvertisementResponse
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.service.AdDetailService
import com.example.letsbuy.service.PaymentService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutPaymentActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutPaymentBinding
    var idAdversiment: Long = -1
    private var idUser: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityCheckoutPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idAdversiment = intent.getLongExtra("ID_AD", -1)
        val prefs = getSharedPreferences("AUTH", MODE_PRIVATE)
        idUser = prefs.getString("ID", "")?.toLong() ?: -1

        getDetailData()
        bindLayoutEvents()
    }

    private fun bindLayoutEvents() {
        binding.finalizedPaymentButton.setOnClickListener {
            payment()
        }

        binding.adressCustomView.setOnClickListener {
            val editProfile = Intent(this, EditProfileActivity::class.java)
            startActivity(editProfile)
        }

        binding.withdrawRadioButton.setOnClickListener {
            binding.DeliveryRadioButton.isChecked = false
            binding.adressCustomView.visibility = View.INVISIBLE
            binding.homeCircleImageView.visibility = View.INVISIBLE
            binding.adressRoadTextView.visibility = View.INVISIBLE

            binding.tipsOfWithdraw.visibility = View.VISIBLE
            binding.tipsTextView.visibility = View.VISIBLE
            binding.tipsImageView.visibility = View.VISIBLE
        }

        binding.DeliveryRadioButton.setOnClickListener {
            binding.withdrawRadioButton.isChecked = false

            binding.adressCustomView.visibility = View.VISIBLE
            binding.homeCircleImageView.visibility = View.VISIBLE
            binding.adressRoadTextView.visibility = View.VISIBLE

            binding.tipsOfWithdraw.visibility = View.INVISIBLE
            binding.tipsTextView.visibility = View.INVISIBLE
            binding.tipsImageView.visibility = View.INVISIBLE
        }
    }

    fun payment() {
        val isShipment: Boolean = when {
            binding.withdrawRadioButton.isChecked -> false
            binding.DeliveryRadioButton.isChecked -> true
            else -> false
        }
        val cardName = binding.inputName.text
        val cardNumber = binding.inputCardNumber
        val expirationDate = binding.inputDateExpiration.unMasked
        val month = expirationDate.substring(0, 2)
        val year = expirationDate.substring(2)
        val cvc = binding.inputCvC

        if (cardName.isNotEmpty() &&
            cardNumber.isDone &&
            month.isNotEmpty() &&
            year.isNotEmpty() &&
            cvc.isDone) {

            val paymentRequest = PaymentRequest(
                isShipment = isShipment,
                idAdvertisement = idAdversiment,
                idUser = idUser,
                cardNumber = cardNumber.unMasked,
                expMonth = month,
                expYear = year,
                securityCode = cvc.unMasked,
                holderName = cardName.toString()
            )

            Log.d("respostaApi", paymentRequest.toString())

            val api = Rest.getInstance().create(PaymentService::class.java)

            api.makePayment(paymentRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    if (response.isSuccessful) {
                        Log.d("respostaApi", "deu tudo certo"+response.toString())
                    } else {
                        Log.d("respostaApi", "deu tudo certo mas caiu no else"+response.toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("respostaApi", "caiu no catch")
                }
            })
        } else {
            Toast.makeText(this, "Preencha os campos corretamente", Toast.LENGTH_LONG)
        }
    }

    private fun loadProductImage(image: ImageDtoResponse?) {
        image?.let { userProfileImage ->
            Picasso.get().load(userProfileImage.url).into(binding.productImageView)
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
                    val road = data?.adversiments?.userSellerLikeDto?.road
                    val number = data?.adversiments?.userSellerLikeDto?.number
                    val neighborhood = data?.adversiments?.userSellerLikeDto?.neighborhood
                    val city = data?.adversiments?.userSellerLikeDto?.city
                    val state = data?.adversiments?.userSellerLikeDto?.state
                    val name = data?.adversiments?.userSellerLikeDto?.name
                    val cpf = data?.adversiments?.userSellerLikeDto?.cpf
                    val price = data?.adversiments?.price.toString()

                    loadProductImage(data?.adversiments?.images?.get(0))
                    binding.adressRoadTextView.text = "$road, $number \n$neighborhood, $city - $state"
                    binding.productNameTextView.text = data?.adversiments?.title
                    binding.CategoryNameTextView.text = CategoryEnum.enumCategoryToDescription(
                        CategoryEnum.categoryToEnum(data?.adversiments?.category ?: "")
                    )
                    binding.ownerNameTextView.text = "Vendido por: $name \nCPF: ${cpf?.take(3) + "-***-***-**"}"
                    binding.PriceTextView.text = "R$ $price"
                }
            }

            override fun onFailure(call: Call<List<DetailAdvertisementResponse>>, t: Throwable) {
                Log.e("getDetailData", "Falha na requisição: ${t.message}")
            }
        })
    }
}