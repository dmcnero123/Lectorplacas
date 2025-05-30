package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonnelAdapter(
    private val personnelList: MutableList<String>, // Lista de nombres de usuario
    private val onItemClick: (String) -> Unit // Callback para cuando se hace clic en un elemento
) : RecyclerView.Adapter<PersonnelAdapter.PersonnelViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION // Para manejar la selección

    inner class PersonnelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.tv_personnel_username)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val previouslySelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previouslySelectedPosition) // Desmarcar el anterior
                    notifyItemChanged(selectedPosition) // Marcar el nuevo
                    onItemClick(personnelList[position]) // Notificar el clic
                }
            }
        }

        fun bind(username: String, isSelected: Boolean) {
            usernameTextView.text = username
            if (isSelected) {
                itemView.setBackgroundResource(android.R.color.holo_blue_light) // Fondo azul para seleccionado
            } else {
                itemView.setBackgroundResource(android.R.color.transparent) // Fondo transparente
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonnelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_personnel, parent, false)
        return PersonnelViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonnelViewHolder, position: Int) {
        val username = personnelList[position]
        holder.bind(username, position == selectedPosition)
    }

    override fun getItemCount(): Int = personnelList.size

    // Métodos para actualizar la lista
    fun updateList(newList: List<String>) {
        personnelList.clear()
        personnelList.addAll(newList)
        selectedPosition = RecyclerView.NO_POSITION // Reiniciar selección al actualizar
        notifyDataSetChanged()
    }

    // Método para obtener el usuario seleccionado
    fun getSelectedUser(): String? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            personnelList[selectedPosition]
        } else {
            null
        }
    }

    // Método para eliminar un elemento y actualizar la lista
    fun removeSelectedUser() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            val usernameToRemove = personnelList[selectedPosition]
            personnelList.removeAt(selectedPosition)
            notifyItemRemoved(selectedPosition)
            // Asegurarse de que selectedPosition no apunte a un índice inválido
            if (selectedPosition >= personnelList.size && personnelList.isNotEmpty()) {
                selectedPosition = personnelList.size - 1
            } else if (personnelList.isEmpty()) {
                selectedPosition = RecyclerView.NO_POSITION
            }
            notifyDataSetChanged() // Para recalcular las vistas
        }
    }
}