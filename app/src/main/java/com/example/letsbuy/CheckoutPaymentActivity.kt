package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
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

    private fun validation(): Boolean {
        val cardName = binding.inputName.text?.toString()
        val cardNumber = binding.inputCardNumber.unMasked
        val expirationDate = binding.inputDateExpiration.unMasked
        val cvc = binding.inputCvC.unMasked

        if (cardName.isNullOrEmpty()) {
            showToast("Preencha o nome do cartão")
            return false
        }

        if (cardNumber.isEmpty() || cardNumber.length != 16 || !cardNumber.isDigitsOnly()) {
            showToast("Preencha o número do cartão corretamente (deve conter 16 dígitos)")
            return false
        }

        if (expirationDate.isEmpty() || expirationDate.length < 6 || !expirationDate.isDigitsOnly()) {
            showToast("Preencha uma data válida (deve conter 6 dígitos)")
            return false
        }

        if (cvc.isEmpty() || cvc.length < 3 || !cvc.isDigitsOnly()) {
            showToast("Preencha um CVC válido (deve conter pelo menos 3 dígitos)")
            return false
        }

        return true
    }

    fun payment() {
        Log.d("respostaApi", validation().toString())

        if (validation()) {
            val isShipment: Boolean = when {
                binding.withdrawRadioButton.isChecked -> false
                binding.DeliveryRadioButton.isChecked -> true
                else -> false
            }

            val cardName = binding.inputName.text
            val cardNumber = binding.inputCardNumber.unMasked
            val expirationDate = binding.inputDateExpiration.unMasked
            val month = expirationDate.substring(0, 2)
            val year = expirationDate.substring(2)
            val cvc = binding.inputCvC.unMasked

            val paymentRequest = PaymentRequest(
                isShipment = isShipment,
                idAdvertisement = idAdversiment.toString(),
                idUser = idUser.toString(),
                cardNumber = cardNumber,
                expMonth = month,
                expYear = year,
                securityCode = cvc,
                holderName = cardName.toString()
            )

            Log.d("respostaApi", paymentRequest.toString())

            val api = Rest.getInstance().create(PaymentService::class.java)
            api.makePayment(paymentRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showToast("Pagamento realizado com sucesso!!")
                    } else {
                        val errorResponse = response.toString()
                        showToast("Erro ao realizar o pagamento $errorResponse")
                        Log.d("respostaApi", errorResponse)
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showToast("Ocorreu um erro ao realizar o pagamento")
                }
            })
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
                    binding.CategoryNameTextView.text = CategoryEnum.enumCategoryToDescription(data?.adversiments!!.category)
                    binding.ownerNameTextView.text = "Vendido por: $name \nCPF: ${cpf?.take(3) + "-***-***-**"}"
                    binding.PriceTextView.text = "R$ $price"
                }
            }

            override fun onFailure(call: Call<List<DetailAdvertisementResponse>>, t: Throwable) {
                showToast("Erro ao carregar anúncio")
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