package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class Cadastro2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro2)

        val checkBox = findViewById<CheckBox>(R.id.checkbox);
        val btnConcluirCadastro = findViewById<Button>(R.id.btn_concluirCadastro);

        checkBox.setOnClickListener {
            if (checkBox.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }

        btnConcluirCadastro.setOnClickListener {
            if (checkBox.isChecked()) {
                val entrar = Intent(this, MainActivity::class.java);
                startActivity(entrar);
            }
        }
    }
}