package com.example.letsbuy.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.letsbuy.HomeActivity
import com.example.letsbuy.PerfilActivity
import com.example.letsbuy.PublishAdActivity
import com.example.letsbuy.adapter.AdapterFavorites
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.FragmentFavoritesBinding
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.service.AdversimentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var listFavorites: MutableList<AllAdversimentsAndLikeDtoResponse>

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = context?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()
        binding.progressBar.visibility = View.VISIBLE
        getAdvertisementsLiked(id, view)

        binding.imageView1.setOnClickListener {
            val back = Intent(requireContext(), HomeActivity::class.java)
            startActivity(back)
        }

    }

    private fun initRecyclerView(myList: List<AllAdversimentsAndLikeDtoResponse>) {
        binding.recyclerViewLike.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewLike.setHasFixedSize(true)
        binding.recyclerViewLike.adapter = AdapterFavorites(myList, context!!)
    }

    private fun getAdvertisementsLiked(id: Long?, view: View) {
        val api = Rest.getInstance().create(AdversimentService::class.java)
        api.getAdversimentLike(id)
            .enqueue(object : Callback<List<AllAdversimentsAndLikeDtoResponse>> {
                override fun onResponse(
                    call: retrofit2.Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    response: Response<List<AllAdversimentsAndLikeDtoResponse>>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        val advertisements = response.body()
                        if (advertisements.isNullOrEmpty()) {
                            binding.scrollFav.visibility = View.GONE
                            binding.emptyAdvertisementsLiked.visibility = View.VISIBLE
                        } else {
                            binding.scrollFav.visibility = View.VISIBLE
                            binding.emptyAdvertisementsLiked.visibility = View.GONE
                            initRecyclerView(advertisements)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    t: Throwable
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.scrollFav.visibility = View.GONE
                    binding.emptyAdvertisementsLiked.visibility = View.VISIBLE
                }
            })
    }
}