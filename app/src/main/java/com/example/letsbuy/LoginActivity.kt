package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.letsbuy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnCadastrar = findViewById<TextView>(R.id.btn_cadastrar)
        val btnEsqueciSenha = findViewById<TextView>(R.id.btn_esqueciSenha)
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)

        btnCadastrar.setOnClickListener {
            val cadastrar = Intent(this, CadastroActivity::class.java)
            startActivity(cadastrar)
        }

        btnEsqueciSenha.setOnClickListener {
            val esqueciSenha = Intent(this, EsqueciSenhaActivity::class.java)
            startActivity(esqueciSenha)
        }

        btnEntrar.setOnClickListener {
            val entrar = Intent(this, HomeActivity::class.java)
            startActivity(entrar)
        }

    }
}