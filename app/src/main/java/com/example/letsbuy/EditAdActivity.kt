package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityEditAdBinding
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.service.AdversimentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAdActivity: AppCompatActivity() {

    private lateinit var binding: ActivityEditAdBinding
    private lateinit var selectedColor: String
    private lateinit var selectedCategory: String
    private lateinit var selectedQuality: String
    private lateinit var adversiment: AllAdversimentsAndLikeDtoResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ad)
        binding = ActivityEditAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idAdversiment = intent.getLongExtra("idAdvertisement", 0)
        getAdversimentById(idAdversiment)

        binding.ivImageBack.setOnClickListener {
            val back = Intent(this, MyAdvertisementsActivity::class.java)
            startActivity(back)
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
                        val editTitle = findViewById<EditText>(R.id.et_title)
                        val editDescription = findViewById<EditText>(R.id.et_description)
                        val editPrice = findViewById<EditText>(R.id.et_price)

                        editTitle.setText(adversiment.title)
                        editDescription.setText(adversiment.description)
                        editPrice.setText(adversiment.price.toString())
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

}