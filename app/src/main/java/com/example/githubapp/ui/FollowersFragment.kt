package com.example.githubapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.data.response.FollowersResponseItem
import com.example.githubapp.database.FavouriteRoomDatabase
import com.example.githubapp.database.SettingPreferences
import com.example.githubapp.database.dataStore
import com.example.githubapp.databinding.FragmentFollowersBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var _binding: FragmentFollowersBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>{
        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        ViewModelFactory.getInstance(requireActivity().application,pref)
    }
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFollowers)

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        userDetailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        userDetailViewModel.getFollowers(UserDetailActivity.Username)
        userDetailViewModel.followers.observe(viewLifecycleOwner, Observer { followersResponse ->
            if (followersResponse != null) {
                showUsers(followersResponse)
            }
            val followersSize = followersResponse?.size
            Log.d(tag,"Tania {$followersSize}")
        })

    }

    private fun showUsers(listUser: List<FollowersResponseItem>) {
        val favouriteDao = FavouriteRoomDatabase.getDatabase(requireContext()).favouriteDao()
        val adapter = FollowersAdapter(favouriteDao)
        adapter.submitList(listUser)
        binding.rvFollowers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvFollowers.visibility = if (isLoading) View.GONE else View.VISIBLE
        Log.d("ok","halo")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}