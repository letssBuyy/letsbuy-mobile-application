package com.example.letsbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RedefinePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefine_password)

        val btnEnviar = findViewById<Button>(R.id.btn_enviar)

        btnEnviar.setOnClickListener {
            val enviar = Intent(this, LoginActivity::class.java)
            startActivity(enviar)
        }
    }
}