package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityRegisterBinding
import com.example.letsbuy.dto.UserDto
import com.example.letsbuy.dto.UserDtoResponse
import com.example.letsbuy.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkbox.setOnClickListener {
            checkbox()
        }

        binding.imgBack.setOnClickListener {
            val back = Intent(this, LoginActivity::class.java)
            startActivity(back)
        }

        binding.btnConcluirCadastro.setOnClickListener {
            val name = findViewById<EditText>(R.id.edit_name).text.toString()
            val email = findViewById<EditText>(R.id.edit_email).text.toString()
            val cpf = findViewById<EditText>(R.id.edit_cpf).text.toString()
            val password = findViewById<EditText>(R.id.edit_password).text.toString()
            val birthDate = findViewById<EditText>(R.id.edit_birth_date).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.edit_phone).text.toString()

            val isValid = validInputs(name, email, cpf, password, birthDate, phoneNumber)

            if (isValid) {
                if (checkbox()) {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = dateFormat.parse(birthDate)
                    val formattedDate = outputDateFormat.format(date)
                    val birth = LocalDate.parse(formattedDate).toString()
                    binding.progressBar.visibility = View.VISIBLE
                    registerUser(name, email, cpf, password, birth, phoneNumber)
                } else {
                    toast()
                }
            }
        }
    }

    private fun checkbox(): Boolean {
        if (binding.checkbox.isChecked()) {
            binding.checkbox.setChecked(true)
            return true
        } else {
            binding.checkbox.setChecked(false)
            return false
        }
    }

    private fun registerUser(
        name: String,
        email: String,
        cpf: String,
        password: String,
        birthDate: String,
        phoneNumber: String
    ) {
        val api = Rest.getInstance().create(UserService::class.java)
        var register = UserDto(name, email, cpf, password, birthDate, phoneNumber)

        api.createUser(register).enqueue(object: Callback<UserDtoResponse> {

            override fun onResponse(
                call: Call<UserDtoResponse>,
                response: Response<UserDtoResponse>
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    startActivity(Intent(baseContext, LoginActivity::class.java));
                }
            }

            override fun onFailure(call: Call<UserDtoResponse>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun toast() {
        Toast.makeText(
            this,
            "Concorde com termos de serviços",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun validInputs(
        name: String,
        email: String,
        cpf: String,
        password: String,
        birthDate: String,
        phoneNumber: String
    ): Boolean {
        var valid = true
        val ivIconErrorName = binding.ivIconErrorName
        val tvMessageErrorName = binding.tvMessageErrorName

        val ivIconErrorEmail = binding.ivIconErrorEmail
        val tvMessageErrorEmail = binding.tvMessageErrorEmail

        val ivIconErrorCpf = binding.ivIconErrorCpf
        val tvMessageErrorCpf = binding.tvMessageErrorCpf

        val ivIconErrorPassword = binding.ivIconErrorPassword
        val tvMessageErrorPassword = binding.tvMessageErrorPassword

        val ivIconErrorBirthDate = binding.ivIconErrorBirthDate
        val tvMessageErrorBirthDate = binding.tvMessageErrorBirthDate

        val ivIconErrorPhoneNumber = binding.ivIconErrorPhone
        val tvMessageErrorPhoneNumber = binding.tvMessageErrorPhone

        if (name.isEmpty()) {
            valid = false
            ivIconErrorName.visibility = View.VISIBLE
            tvMessageErrorName.visibility = View.VISIBLE
        } else {
            ivIconErrorName.visibility = View.GONE
            tvMessageErrorName.visibility = View.GONE
        }

        if (email.isEmpty()) {
            valid = false
            ivIconErrorEmail.visibility = View.VISIBLE
            tvMessageErrorEmail.visibility = View.VISIBLE
        } else {
            ivIconErrorEmail.visibility = View.GONE
            tvMessageErrorEmail.visibility = View.GONE
        }

        if (cpf.isEmpty()) {
            valid = false
            ivIconErrorCpf.visibility = View.VISIBLE
            tvMessageErrorCpf.visibility = View.VISIBLE
        } else {
            ivIconErrorCpf.visibility = View.GONE
            tvMessageErrorCpf.visibility = View.GONE
        }

        if (password.isEmpty()) {
            valid = false
            ivIconErrorPassword.visibility = View.VISIBLE
            tvMessageErrorPassword.visibility = View.VISIBLE
        } else {
            ivIconErrorPassword.visibility = View.GONE
            tvMessageErrorPassword.visibility = View.GONE
        }

        if (birthDate.isEmpty()) {
            valid = false
            ivIconErrorBirthDate.visibility = View.VISIBLE
            tvMessageErrorBirthDate.visibility = View.VISIBLE
        } else {
            ivIconErrorBirthDate.visibility = View.GONE
            tvMessageErrorBirthDate.visibility = View.GONE
        }

        if (phoneNumber.isEmpty()) {
            valid = false
            ivIconErrorPhoneNumber.visibility = View.VISIBLE
            tvMessageErrorPhoneNumber.visibility = View.VISIBLE
        } else {
            ivIconErrorPhoneNumber.visibility = View.GONE
            tvMessageErrorPhoneNumber.visibility = View.GONE
        }

        return valid
    }
}