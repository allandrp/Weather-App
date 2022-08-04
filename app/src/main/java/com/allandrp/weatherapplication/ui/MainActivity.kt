package com.allandrp.weatherapplication.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allandrp.weatherapplication.R
import com.allandrp.weatherapplication.databinding.ActivityMainBinding
import com.allandrp.weatherapplication.repository.Result
import com.allandrp.weatherapplication.room.Weather
import com.allandrp.weatherapplication.ui.list.AdapterWeather
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: AdapterWeather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getListWeather().observe(this) {
            if (it.isNotEmpty()) {
                initRecyclerView(it)
            }
        }

        binding.btnAdd.setOnClickListener {
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.activity_add, null)
            val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
                .setTitle("Add Location")

            val alertDialog = dialogBuilder.show()

            val addButton = dialogView.findViewById<Button>(R.id.AddButton)
            val locationInput = dialogView.findViewById<EditText>(R.id.et_location)
            val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)

            addButton.setOnClickListener {
                var location = locationInput.text.toString()

                if (location.isEmpty()) {
                    locationInput.error = "Empty"
                } else {
                    Executors.newSingleThreadExecutor().execute {
                        if (viewModel.isLocationExist(location)) {
                            this@MainActivity.runOnUiThread {
                                Toast.makeText(this, "Location Already Exist !", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            this@MainActivity.runOnUiThread {
                                viewModel.getWeatherData(location).observe(this) {
                                    when (it) {
                                        is Result.Loading -> {
                                            isLoading(progressBar, true)
                                        }

                                        is Result.Error -> {
                                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT)
                                                .show()
                                            isLoading(progressBar, false)
                                        }

                                        is Result.Success -> {
                                            isLoading(progressBar, false)
                                            val weather = Weather(
                                                location = it.data.name,
                                                lat = it.data.coord.lat.toFloat(),
                                                lon = it.data.coord.lon.toFloat(),
                                                iconCode = it.data.weather[0].icon,
                                                temperature = it.data.main.temp.toFloat(),
                                                desc = it.data.weather[0].description
                                            )

                                            viewModel.insertWeather(weather)
                                            alertDialog.dismiss()
                                        }

                                        else -> isLoading(progressBar, false)
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
    }

    private fun initRecyclerView(listLocation: List<Weather>) {
        binding.rvWeather.layoutManager = LinearLayoutManager(this)
        adapter = AdapterWeather(listLocation)
        binding.rvWeather.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val location = (viewHolder as AdapterWeather.ViewHolder).getData
                viewModel.deleteLocation(location)
            }

        })

        itemTouchHelper.attachToRecyclerView(binding.rvWeather)
    }

    private fun isLoading(progressBar: ProgressBar, loading: Boolean) {
        if (loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}