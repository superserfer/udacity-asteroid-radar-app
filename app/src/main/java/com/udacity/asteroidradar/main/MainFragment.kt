package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.AsteroidListener
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(requireActivity().application))[MainViewModel::class.java]
    }

    private lateinit var adapter: AsteroidAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
            it?.let {
                if (it.mediaType == "image") {
                   Picasso.get().load(it.url).into(binding.activityMainImageOfTheDay)
                } else {
                    binding.activityMainImageOfTheDay.setImageResource(R.drawable.placeholder_picture_of_day)
                }
            }
        }

        viewModel.navigateToAsteroid.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                this.viewModel.onAsteroidNavigated()
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onFilterChanged(when(item.itemId) {
            R.id.show_rent_menu -> {
                Filter.TODAY
            }
            R.id.show_buy_menu -> {
                Filter.SAVED
            }
            else -> {
                Filter.WEEK
            }
        })
        viewModel.asteroids.removeObservers(viewLifecycleOwner)
        viewModel.asteroids.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        return true
    }
}
