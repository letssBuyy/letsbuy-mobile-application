package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityEditProfileBinding
import com.example.letsbuy.dto.BankAccount
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.dto.UserDtoResponse
import com.example.letsbuy.dto.UserUpdateDto
import com.example.letsbuy.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var bankAccount: BankAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val pref = getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val userId = pref?.getString("ID", null)?.toLong()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserById(userId!!)
        binding.imageBack.setOnClickListener() {
            val back = Intent(this, PerfilActivity::class.java)
            startActivity(back)
        }
        binding.btnUpdate.setOnClickListener {
            val name = findViewById<EditText>(R.id.inputName).text.toString()
            val cpf = findViewById<EditText>(R.id.inputCpf).text.toString()
            val dtBirth = findViewById<EditText>(R.id.inputDtBirth).text.toString()
            val neighboring = findViewById<EditText>(R.id.inputBairro).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.inputPhNumber).text.toString()
            val email = findViewById<EditText>(R.id.inputEmail).text.toString()
            // val bankNumber = findViewById<EditText>(R.id.inputBank).text.toString()
            // val bankAg = findViewById<EditText>(R.id.inputAg).text.toString()
            val number = findViewById<EditText>(R.id.inputNumero).text.toString().toLong()
            val cep = findViewById<EditText>(R.id.inputCep).text.toString()
            val complement = findViewById<EditText>(R.id.inputComplement).text.toString()
            val road = findViewById<EditText>(R.id.inputRua).text.toString()
            // val bankCount = findViewById<EditText>(R.id.inputCount).text.toString()
            val state = findViewById<EditText>(R.id.inputState).text.toString()
            val city = findViewById<EditText>(R.id.inputCity).text.toString()
            //val id = idUser.toLong()
            //Log.w("id",id.toString())
            val bankAccount = null
            val userUpdateDto = UserUpdateDto(
                name,
                email,
                cpf,
                dtBirth,
                phoneNumber,
                cep,
                road,
                number,
                neighboring,
                complement,
                state,
                city,
                bankAccount
            )
            val isValid = validInputs(name,email,cpf,dtBirth,phoneNumber,cep,road,
                number,
                neighboring,
                complement,
                state,
                city,
                bankAccount)
            if(isValid) {
                updateProfile(userUpdateDto)
            }
        }
    }

    private fun getUserById(id: Long) {
        val api = Rest.getInstance().create(UserService::class.java)
        api.getAdversimentsByUser(id, null).enqueue(object : Callback<UserAdversimentsDtoResponse> {
            override fun onResponse(
                call: Call<UserAdversimentsDtoResponse>,
                response: Response<UserAdversimentsDtoResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        //var idUser = id
                        //idUser?.toLong()
                        //idUser = user.bankAccount?.id.toString()
                        binding.inputName.setText(user.name)
                        binding.inputCpf.setText(user.cpf)
                        binding.inputDtBirth.setText(user.birthDate)
                        binding.inputState.setText(user.state)
                        binding.inputCity.setText(user.city)
                        binding.inputPhNumber.setText(user.phoneNumber)
                        binding.inputBairro.setText(user.neighborhood)
                        binding.inputEmail.setText(user.email)
                        binding.inputBank.setText(user.bankAccount?.bankNumber.toString())
                        binding.inputAg.setText(user.bankAccount?.agencyNumber.toString())
                        binding.inputNumero.setText(user.number.toString())
                        binding.inputCount.setText(user.bankAccount?.accountNumber.toString())
                        binding.inputCep.setText(user.cep)
                        binding.inputComplement.setText(user.complement)
                        binding.inputRua.setText(user.road)
                    }
                }
            }

            override fun onFailure(call: Call<UserAdversimentsDtoResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun updateProfile(userUpdateDto: UserUpdateDto) {
        val api = Rest.getInstance().create(UserService::class.java)
        val pref = getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val userId = pref?.getString("ID", null)?.toLong()
        api.updateUser(userId, userUpdateDto).enqueue(object : Callback<UserDtoResponse> {
            override fun onResponse(
                call: Call<UserDtoResponse>,
                response: Response<UserDtoResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Perfil Atualizado com Sucesso :) !",
                        Toast.LENGTH_SHORT
                    ).show()
                    val black = Intent(this@EditProfileActivity, PerfilActivity::class.java)
                    startActivity(black)
                }
            }

            override fun onFailure(call: Call<UserDtoResponse>, t: Throwable) {
                Toast.makeText(
                    this@EditProfileActivity,
                    "Verifique os campos que não foram preenchidos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun validInputs(name: String,
                            email: String,
                            cpf: String,
                            dtBirth: String,
                            phoneNumber: String,
                            cep: String,
                            road: String,
                            number: Long,
                            neighboring: String,
                            complement: String,
                            state: String,
                            city: String,
                            bankAccount: BankAccount?): Boolean {
        var valid = true
        val ivIconErrorName = binding.ivIconErrorName
        val tvMessageErrorName = binding.tvMessageErrorName

        val ivIconErrorCpf = binding.ivIconErrorCpf
        val tvMessageErrorCpf = binding.tvMessageErrorCpf

        val ivIconErrorDtBirth = binding.ivIconErrorDtNasc
        val tvMessageErrorDtBirth = binding.tvMessageErrorDtNasc

        val ivIconErrorPhNumber = binding.ivIconErrorPhNumber
        val tvMessageErrorPhNumber = binding.tvMessageErrorPhNumber

        val ivIconErrorEmail = binding.ivIconErrorEmail
        val tvMessageErrorEmail = binding.tvMessageErrorEmail

        val ivIconErrorBkBank = binding.ivIconErrorBkBank
        val tvMessageErrorBkBank = binding.tvMessageErrorBkBank

        val ivIconErrorBkAgency = binding.ivIconErrorBkAgencia
        val tvMessageErrorBkAgency = binding.tvMessageErrorBkAgencia

        val ivIconErrorBkCount = binding.ivIconErrorBkCount
        val tvMessageErrorBkCount = binding.tvMessageErrorBkCount

        val ivIconErrorCep = binding.ivIconErrorCep
        val tvMessageErrorCep = binding.tvMessageErrorCep

        val ivIconErrorRoad = binding.ivIconErrorRua
        val tvMessageErrorRoad = binding.tvMessageErrorRua

        val ivIconErrorNumber = binding.ivIconErrorNumber
        val tvMessageErrorNumber = binding.tvMessageErrorNumber

        val ivIconErrorComplement = binding.ivIconErrorComp
        val tvMessageErrorComplement = binding.tvMessageErrorComp

        val ivIconErrorNeighborhood = binding.ivIconErrorBairro
        val tvMessageErrorNeighborhood = binding.tvMessageErrorBairro

        val ivIconErrorState = binding.ivIconErrorState
        val tvMessageErrorState = binding.tvMessageErrorState

        val ivIconErrorCity = binding.ivIconErrorCity
        val tvMessageErrorCity = binding.tvMessageErrorCity

        if(name.isEmpty()) {
            valid = false
            ivIconErrorName.visibility = View.VISIBLE
            tvMessageErrorName.visibility = View.VISIBLE
        } else {
            ivIconErrorName.visibility = View.GONE
            tvMessageErrorName.visibility = View.GONE
        }

        if(cpf.isEmpty()) {
            valid = false
            ivIconErrorCpf.visibility = View.VISIBLE
            tvMessageErrorCpf.visibility = View.VISIBLE
        } else {
            ivIconErrorCpf.visibility = View.GONE
            tvMessageErrorCpf.visibility = View.GONE
        }

        if(dtBirth.isEmpty()) {
            valid = false
            ivIconErrorDtBirth.visibility = View.VISIBLE
            tvMessageErrorDtBirth.visibility = View.VISIBLE
        } else {
            ivIconErrorDtBirth.visibility = View.GONE
            tvMessageErrorDtBirth.visibility = View.GONE
        }

        if(email.isEmpty()) {
            valid = false
            ivIconErrorEmail.visibility = View.VISIBLE
            tvMessageErrorEmail.visibility = View.VISIBLE
        } else {
            ivIconErrorEmail.visibility = View.GONE
            tvMessageErrorEmail.visibility = View.GONE
        }

        if(phoneNumber.isEmpty()) {
            valid = false
            ivIconErrorPhNumber.visibility = View.VISIBLE
            tvMessageErrorPhNumber.visibility = View.VISIBLE
        } else {
            ivIconErrorPhNumber.visibility = View.GONE
            tvMessageErrorPhNumber.visibility = View.GONE
        }

//        if(bankAccount.accountNumber.isEmpty()) {
//            valid = false
//            ivIconErrorBkCount.visibility = View.VISIBLE
//            tvMessageErrorBkCount.visibility = View.VISIBLE
//        } else {
//            ivIconErrorBkCount.visibility = View.GONE
//            tvMessageErrorBkCount.visibility = View.GONE
//        }
//
//        if(bankAccount.bankNumber.isEmpty()) {
//            valid = false
//            ivIconErrorBkBank.visibility = View.VISIBLE
//            tvMessageErrorBkBank.visibility = View.VISIBLE
//        } else {
//            ivIconErrorBkBank.visibility = View.GONE
//            tvMessageErrorBkBank.visibility = View.GONE
//        }
//
//        if(bankAccount.agencyNumber.isEmpty()) {
//            valid = false
//            ivIconErrorBkAgency.visibility = View.VISIBLE
//            tvMessageErrorBkAgency.visibility = View.VISIBLE
//        } else {
//            ivIconErrorBkAgency.visibility = View.GONE
//            tvMessageErrorBkAgency.visibility = View.GONE
//        }

        if(cep.isEmpty()) {
            valid = false
            ivIconErrorCep.visibility = View.VISIBLE
            tvMessageErrorCep.visibility = View.VISIBLE
        } else {
            ivIconErrorCep.visibility = View.GONE
            tvMessageErrorCep.visibility = View.GONE
        }

        if(road.isEmpty()) {
            valid = false
            ivIconErrorRoad.visibility = View.VISIBLE
            tvMessageErrorRoad.visibility = View.VISIBLE
        } else {
            ivIconErrorRoad.visibility = View.GONE
            tvMessageErrorRoad.visibility = View.GONE
        }

        if(complement.isEmpty()) {
            valid = false
            ivIconErrorComplement.visibility = View.VISIBLE
            tvMessageErrorComplement.visibility = View.VISIBLE
        } else {
            ivIconErrorComplement.visibility = View.GONE
            tvMessageErrorComplement.visibility = View.GONE
        }

        if(neighboring.isEmpty()) {
            valid = false
            ivIconErrorNeighborhood.visibility = View.VISIBLE
            tvMessageErrorNeighborhood.visibility = View.VISIBLE
        } else {
            ivIconErrorNeighborhood.visibility = View.GONE
            tvMessageErrorNeighborhood.visibility = View.GONE
        }

        if(state.isEmpty()) {
            valid = false
            ivIconErrorState.visibility = View.VISIBLE
            tvMessageErrorState.visibility = View.VISIBLE
        } else {
            ivIconErrorState.visibility = View.GONE
            tvMessageErrorState.visibility = View.GONE
        }

        if(city.isEmpty()) {
            valid = false
            ivIconErrorCity.visibility = View.VISIBLE
            tvMessageErrorCity.visibility = View.VISIBLE
        } else {
            ivIconErrorCity.visibility = View.GONE
            tvMessageErrorCity.visibility = View.GONE
        }

        if(number.toString().isEmpty() || number <= 0) {
            valid = false
            ivIconErrorNumber.visibility = View.VISIBLE
            tvMessageErrorNumber.visibility = View.VISIBLE
        } else {
            ivIconErrorNumber.visibility = View.GONE
            tvMessageErrorNumber.visibility = View.GONE
        }

        return valid
    }
}