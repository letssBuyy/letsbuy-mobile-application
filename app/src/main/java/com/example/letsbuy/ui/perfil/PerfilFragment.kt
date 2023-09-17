package com.example.letsbuy.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.letsbuy.EditProfileActivity
import com.example.letsbuy.LoginActivity
import com.example.letsbuy.MyAdvertisementsActivity
import com.example.letsbuy.MyShoppingsActivity
import com.example.letsbuy.PublishAdActivity
import com.example.letsbuy.R
import com.example.letsbuy.WalletActivity
import com.example.letsbuy.databinding.ActivityEditProfileBinding
import com.example.letsbuy.databinding.ActivityPerfilBinding
import com.example.letsbuy.databinding.FragmentPerfilBinding
import org.w3c.dom.Text

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
}