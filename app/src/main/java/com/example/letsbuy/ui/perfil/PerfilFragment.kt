package com.example.letsbuy.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import com.example.letsbuy.EditProfileActivity
import com.example.letsbuy.LoginActivity
import com.example.letsbuy.MyAdvertisementsActivity
import com.example.letsbuy.MyShoppingsActivity
import com.example.letsbuy.PublishAdActivity
import com.example.letsbuy.R
import com.example.letsbuy.WalletActivity
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityEditProfileBinding
import com.example.letsbuy.databinding.ActivityPerfilBinding
import com.example.letsbuy.databinding.FragmentPerfilBinding
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.service.UserService
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerfilBinding.inflate(layoutInflater)
        val pref = context?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()
        binding.progressBar.visibility = View.VISIBLE
        getUserById(id)
        binding.tvEdtPerfil.setOnClickListener() {
            val edtPerfil = Intent(context, EditProfileActivity::class.java)
            startActivity(edtPerfil)
        }

        binding.imgEditPerfil.setOnClickListener {
            val edtPerfil = Intent(context, EditProfileActivity::class.java)
            startActivity(edtPerfil)
        }

        binding.tvMyAdvertisements.setOnClickListener {
            val myAdvertisements = Intent(context, MyAdvertisementsActivity::class.java)
            startActivity(myAdvertisements)
        }

        binding.tvPublishAd.setOnClickListener {
            val publishAd = Intent(context, PublishAdActivity::class.java)
            startActivity(publishAd)
        }

        binding.tvMyShoppings.setOnClickListener {
            val myShoppings = Intent(context, MyShoppingsActivity::class.java)
            startActivity(myShoppings)
        }

        binding.tvMyWallet.setOnClickListener {
            val myWallet = Intent(context, WalletActivity::class.java)
            startActivity(myWallet)
        }

        binding.btnExit.setOnClickListener {
            val exit = Intent(context, LoginActivity::class.java)
            startActivity(exit)
        }

        return binding.root
    }

    private fun getUserById(id: Long?) {
        val api = Rest.getInstance().create(UserService::class.java)
        api.getAdversimentsByUser(id!!, null).enqueue(object : Callback<UserAdversimentsDtoResponse> {
            override fun onResponse(
                call: Call<UserAdversimentsDtoResponse>,
                response: Response<UserAdversimentsDtoResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val user = response.body()
                    Glide.with(this@PerfilFragment)
                        .load(user!!.profileImage)
                        .error(R.drawable.broke_image)
                        .into(binding.profileImage)
                    binding.profileName.text = user.name
                }
            }

            override fun onFailure(call: Call<UserAdversimentsDtoResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}