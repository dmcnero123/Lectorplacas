package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleAdapter(
    private val vehicleList: MutableList<Vehicle>,
    private val onItemClick: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plateTextView: TextView = itemView.findViewById(R.id.tv_vehicle_plate)
        val nameLastnameTextView: TextView = itemView.findViewById(R.id.tv_vehicle_name_lastname)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val previouslySelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previouslySelectedPosition)
                    notifyItemChanged(selectedPosition)
                    onItemClick(vehicleList[position])
                }
            }
        }

        fun bind(vehicle: Vehicle, isSelected: Boolean) {
            plateTextView.text = vehicle.plate
            nameLastnameTextView.text = "${vehicle.name} ${vehicle.lastname}"
            if (isSelected) {
                itemView.setBackgroundResource(android.R.color.holo_blue_light)
            } else {
                itemView.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]
        holder.bind(vehicle, position == selectedPosition)
    }

    override fun getItemCount(): Int = vehicleList.size

    fun updateList(newList: List<Vehicle>) {
        vehicleList.clear()
        vehicleList.addAll(newList)
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    fun getSelectedVehicle(): Vehicle? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            vehicleList[selectedPosition]
        } else {
            null
        }
    }

    fun removeSelectedVehicle() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            vehicleList.removeAt(selectedPosition)
            notifyItemRemoved(selectedPosition)
            // Asegurarse de que selectedPosition no apunte a un índice inválido
            if (selectedPosition >= vehicleList.size && vehicleList.isNotEmpty()) {
                selectedPosition = vehicleList.size - 1
            } else if (vehicleList.isEmpty()) {
                selectedPosition = RecyclerView.NO_POSITION
            }
            notifyDataSetChanged()
        }
    }
}