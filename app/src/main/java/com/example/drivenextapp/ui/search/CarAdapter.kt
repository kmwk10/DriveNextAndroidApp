// ui/home/CarAdapter.kt
package com.example.drivenextapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenextapp.R
import com.example.drivenextapp.data.CarData

class CarAdapter(
    private var items: List<CarData> = listOf(),
    private val onBook: (CarData) -> Unit,
    private val onDetails: (CarData) -> Unit
) : RecyclerView.Adapter<CarAdapter.Holder>() {

    fun submitList(list: List<CarData>) {
        this.items = list
        notifyDataSetChanged()
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvModel: TextView = view.findViewById(R.id.tvModel)
        val tvBrand: TextView = view.findViewById(R.id.tvBrand)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvGearbox: TextView = view.findViewById(R.id.tvGearbox)
        val tvFuel: TextView = view.findViewById(R.id.tvFuel)
        val imgCar: ImageView = view.findViewById(R.id.imgCar)
        val btnBook: Button = view.findViewById(R.id.btnBook)
        val btnDetails: Button = view.findViewById(R.id.btnDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val car = items[position]
        holder.tvModel.text = car.model
        holder.tvBrand.text = car.brand

        holder.tvPrice.text = "${car.pricePerDay}₽"

        holder.tvGearbox.text = car.gearbox
        holder.tvFuel.text = car.fuel

        // Заглушка картинки: если есть то imageResId, иначе placeholder
        if (car.imageResId != null) {
            holder.imgCar.setImageResource(car.imageResId)
        } else {
            holder.imgCar.setImageResource(R.drawable.ic_car_photo)
        }

        holder.btnBook.setOnClickListener { onBook(car) }
        holder.btnDetails.setOnClickListener { onDetails(car) }
    }

    override fun getItemCount(): Int = items.size
}
