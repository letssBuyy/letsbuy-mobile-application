package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityLoginBinding
import com.example.letsbuy.dto.AuthenticationRequestDto
import com.example.letsbuy.dto.TokenDto
import com.example.letsbuy.service.AuthenticationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrar.setOnClickListener {
            val cadastrar = Intent(this, RegisterActivity::class.java)
            startActivity(cadastrar)
        }

        binding.btnEsqueciSenha.setOnClickListener {
            val esqueciSenha = Intent(this, RedefinePasswordActivity::class.java)
            startActivity(esqueciSenha)
        }

        binding.btnEntrar.setOnClickListener {
            val email = findViewById<EditText>(R.id.et_email).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            binding.progressBar.visibility = View.VISIBLE
            authentication(email, password)
        }
    }

    private fun authentication(email: String, password: String) {
        val api = Rest.getInstance().create(AuthenticationService::class.java)
        var login = AuthenticationRequestDto(email, password)

        api.authentication(login).enqueue(object: Callback<TokenDto> {

            override fun onResponse(call: Call<TokenDto>, response: Response<TokenDto>) {
                binding.progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    val prefs = getSharedPreferences("AUTH", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("TOKEN", response.body()?.token)
                    editor.putString("TIPO", response.body()?.tipo)
                    editor.putString("ID", response.body()?.user?.id.toString())
                    editor.putBoolean("LOGADO", true)
                    editor.apply()
                    startActivity(Intent(baseContext, HomeActivity::class.java))
                } else {
                    toast()
                }
            }

            override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                Log.d("ERROLOGIN", t.message.toString())
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun toast() {
        Toast.makeText(
            this,
            "Dados do login incorretos",
            Toast.LENGTH_LONG).show()
    }
}