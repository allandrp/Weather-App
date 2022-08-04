package com.allandrp.weatherapplication.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.allandrp.weatherapplication.R
import com.allandrp.weatherapplication.databinding.ItemWeatherBinding
import com.allandrp.weatherapplication.room.Weather
import com.bumptech.glide.Glide
import okhttp3.internal.concurrent.Task
import kotlin.math.roundToInt

class AdapterWeather(private val listWeather: List<Weather>): RecyclerView.Adapter<AdapterWeather.ViewHolder>() {


    class ViewHolder(val binding: ItemWeatherBinding): RecyclerView.ViewHolder(binding.root){
        lateinit var getData: Weather

        fun bind(data: Weather){
            getData = data
            binding.apply {
                tvTemperature.text = "${data.temperature.roundToInt()}\u2103"
                tvLocation.text = data.location

                Glide.with(root)
                    .load("http://openweathermap.org/img/w/${data.iconCode}.png")
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listWeather[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listWeather.size
}