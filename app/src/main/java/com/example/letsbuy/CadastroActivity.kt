package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityCadastroBinding
import com.example.letsbuy.databinding.ActivityPerfilBinding

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        binding =  ActivityCadastroBinding.inflate(layoutInflater)
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