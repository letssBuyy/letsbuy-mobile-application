package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding =  ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkbox.setOnClickListener {
            if (binding.checkbox.isChecked()) {
                binding.checkbox.setChecked(true);
            } else {
                binding.checkbox.setChecked(false);
            }
        }

       binding.btnConcluirCadastro.setOnClickListener {
            if (binding.checkbox.isChecked()) {
                val entrar = Intent(this, LoginActivity::class.java);
                startActivity(entrar);
            }
        }
    }
}