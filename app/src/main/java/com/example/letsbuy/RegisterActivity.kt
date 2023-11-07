package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
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
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val cpf = binding.editCpf.unMasked
            val password = binding.editPassword.text.toString()
            val birthDate = binding.editBirthDate.text.toString()
            val phoneNumber = binding.editPhone.unMasked

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

        api.createUser(register).enqueue(object : Callback<UserDtoResponse> {

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

        when {
            name.isEmpty() -> {
                valid = false
                ivIconErrorName.visibility = View.VISIBLE
                tvMessageErrorName.visibility = View.VISIBLE
            }

            name.length > 50 -> {
                valid = false
                ivIconErrorName.visibility = View.VISIBLE
                tvMessageErrorName.visibility = View.VISIBLE
                tvMessageErrorName.text = "Deve ser menor que 51 caracteres"
            }

            name.length < 3 -> {
                valid = false
                ivIconErrorName.visibility = View.VISIBLE
                tvMessageErrorName.visibility = View.VISIBLE
                tvMessageErrorName.text = "Deve ser maior que 2 caracteres"
            }

            else -> {
                ivIconErrorName.visibility = View.GONE
                tvMessageErrorName.visibility = View.GONE
            }
        }

        when {
            email.isEmpty() -> {
                valid = false
                ivIconErrorEmail.visibility = View.VISIBLE
                tvMessageErrorEmail.visibility = View.VISIBLE
            }

            email.indexOf("@", 3, false) == -1 -> {
                valid = false
                ivIconErrorEmail.visibility = View.VISIBLE
                tvMessageErrorEmail.visibility = View.VISIBLE
                tvMessageErrorEmail.text = "Deve possuir @"
            }

            email.indexOf(".com", 5, false) == -1 -> {
                valid = false
                ivIconErrorEmail.visibility = View.VISIBLE
                tvMessageErrorEmail.visibility = View.VISIBLE
                tvMessageErrorEmail.text = "Email invalido"
            }

            else -> {
                ivIconErrorEmail.visibility = View.GONE
                tvMessageErrorEmail.visibility = View.GONE
            }
        }

        when {
            cpf.isEmpty() -> {
                valid = false
                ivIconErrorCpf.visibility = View.VISIBLE
                tvMessageErrorCpf.visibility = View.VISIBLE
            }

            cpf.length != 11 -> {
                valid = false
                ivIconErrorCpf.visibility = View.VISIBLE
                tvMessageErrorCpf.visibility = View.VISIBLE
                tvMessageErrorCpf.text = "Deve possuir 11 digito"
            }

            !validateCPF(cpf) -> {
                valid = false
                ivIconErrorCpf.visibility = View.VISIBLE
                tvMessageErrorCpf.visibility = View.VISIBLE
                tvMessageErrorCpf.text = "CPF invalido"
            }

            else -> {
                ivIconErrorCpf.visibility = View.GONE
                tvMessageErrorCpf.visibility = View.GONE
            }
        }

        when {
            password.isEmpty() -> {
                valid = false
                ivIconErrorPassword.visibility = View.VISIBLE
                tvMessageErrorPassword.visibility = View.VISIBLE
            }

            password.length < 6 -> {
                valid = false
                ivIconErrorPassword.visibility = View.VISIBLE
                tvMessageErrorPassword.visibility = View.VISIBLE
                tvMessageErrorPassword.text = "Senha deve conter no minimo 6 caracteres"
            }

            password.length > 12 -> {
                valid = false
                ivIconErrorPassword.visibility = View.VISIBLE
                tvMessageErrorPassword.visibility = View.VISIBLE
                tvMessageErrorPassword.text = "Senha deve conter no maximo 12"
            }

            else -> {
                ivIconErrorPassword.visibility = View.GONE
                tvMessageErrorPassword.visibility = View.GONE
            }
        }

        when {
            birthDate.isEmpty() -> {
                valid = false
                ivIconErrorBirthDate.visibility = View.VISIBLE
                tvMessageErrorBirthDate.visibility = View.VISIBLE
            }

            calculateAge(birthDate) < 18 -> {
                valid = false
                ivIconErrorBirthDate.visibility = View.VISIBLE
                tvMessageErrorBirthDate.visibility = View.VISIBLE
                tvMessageErrorBirthDate.text = "Precisa ser maior de idade"
            }

            else -> {
                ivIconErrorBirthDate.visibility = View.GONE
                tvMessageErrorBirthDate.visibility = View.GONE
            }
        }

        when {
            phoneNumber.isEmpty() -> {
                valid = false
                ivIconErrorPhoneNumber.visibility = View.VISIBLE
                tvMessageErrorPhoneNumber.visibility = View.VISIBLE
            }

            phoneNumber.length != 11 -> {
                valid = false
                ivIconErrorPhoneNumber.visibility = android.view.View.VISIBLE
                tvMessageErrorPhoneNumber.visibility = android.view.View.VISIBLE
                tvMessageErrorPhoneNumber.text = "Numero de telefone invalido"
            }

            else -> {
                ivIconErrorPhoneNumber.visibility = View.GONE
                tvMessageErrorPhoneNumber.visibility = View.GONE
            }
        }
        return valid
    }

    private fun validateCPF(cpf: String): Boolean {

        if (cpf.length != 11 || cpf.toSet().size == 1) {
            return false
        }

        val digits = cpf.map { it.toString().toInt() }

        val firstVerifierDigit = calculateVerifierDigit(digits.take(9))
        val secondVerifierDigit =
            calculateVerifierDigit(digits.take(9) + listOf(firstVerifierDigit))

        return cpf.substring(9, 11) == "$firstVerifierDigit$secondVerifierDigit"
    }

    private fun calculateVerifierDigit(digits: List<Int>): Int {
        var weight = digits.size + 1
        val sum = digits.fold(0) { acc, digit ->
            acc + digit * weight--
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    private fun calculateAge(birthDate: String): Int {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(birthDate, formatter)
        val period = Period.between(date, today)
        return period.years
    }
}