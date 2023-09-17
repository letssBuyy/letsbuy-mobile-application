package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnCadastrar = findViewById<TextView>(R.id.btn_cadastrar)
        val btnEsqueciSenha = findViewById<TextView>(R.id.btn_esqueciSenha)
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)

        btnCadastrar.setOnClickListener {
            val cadastrar = Intent(this, RegisterActivity::class.java)
            startActivity(cadastrar)
        }

        btnEsqueciSenha.setOnClickListener {
            val esqueciSenha = Intent(this, RedefinePasswordActivity::class.java)
            startActivity(esqueciSenha)
        }

        btnEntrar.setOnClickListener {
            val entrar = Intent(this, HomeActivity::class.java)
            startActivity(entrar)
        }

    }
}