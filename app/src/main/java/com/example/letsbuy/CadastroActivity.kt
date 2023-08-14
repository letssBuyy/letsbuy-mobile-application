package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val btnProximo = findViewById<Button>(R.id.btn_proximo)

        btnProximo.setOnClickListener {
            val cadastrar = Intent(this, Cadastro2Activity::class.java)
            startActivity(cadastrar)
        }
    }
}