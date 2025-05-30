package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.DBHelper

class VehicleListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VehicleAdapter
    private val vehicleList = mutableListOf<Vehicle>() // Lista mutable de objetos Vehicle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vehicle_list)

        dbHelper = DBHelper(this)

        recyclerView = findViewById(R.id.rv_vehicle_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = VehicleAdapter(vehicleList) { vehicle ->
            Toast.makeText(this, "Seleccionado: ${vehicle.plate}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val btnNuevo: Button = findViewById(R.id.btn_nuevo_vehicle)
        val btnEditar: Button = findViewById(R.id.btn_editar_vehicle)
        val btnEliminar: Button = findViewById(R.id.btn_eliminar_vehicle)

        // Botón "Nuevo" - Navegar a la pantalla de registro de nuevo vehículo
        btnNuevo.setOnClickListener {
            val intent = Intent(this, RegisterVehicleActivity::class.java) // Crearemos esta actividad
            startActivity(intent)
        }

        // Botón "Editar" (funcionalidad pendiente)
        btnEditar.setOnClickListener {
            val selectedVehicle = adapter.getSelectedVehicle()
            if (selectedVehicle != null) {
                Toast.makeText(this, "Editar vehículo: ${selectedVehicle.plate}", Toast.LENGTH_SHORT).show()
                // Aquí iría el Intent para una actividad de edición, pasando el 'selectedVehicle.plate'
                val intent = Intent(this, RegisterVehicleActivity::class.java) // Reutilizamos la de registro, pero la adaptaremos
                intent.putExtra("EDIT_MODE", true)
                intent.putExtra("VEHICLE_PLATE_TO_EDIT", selectedVehicle.plate)
                intent.putExtra("VEHICLE_NAME_TO_EDIT", selectedVehicle.name)
                intent.putExtra("VEHICLE_LASTNAME_TO_EDIT", selectedVehicle.lastname)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, selecciona un vehículo para editar", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón "Eliminar" - Con diálogo de confirmación
        btnEliminar.setOnClickListener {
            val selectedVehicle = adapter.getSelectedVehicle()
            if (selectedVehicle != null) {
                AlertDialog.Builder(this)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estás seguro de que quieres eliminar la placa '${selectedVehicle.plate}'?")
                    .setPositiveButton("Sí") { dialog, _ ->
                        val isDeleted = dbHelper.deleteVehicle(selectedVehicle.plate) // Llama al método de tu DBHelper
                        if (isDeleted) {
                            adapter.removeSelectedVehicle() // Elimina de la lista visible
                            Toast.makeText(this, "Placa '${selectedVehicle.plate}' eliminada exitosamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al eliminar la placa '${selectedVehicle.plate}'", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                Toast.makeText(this, "Por favor, selecciona un vehículo para eliminar", Toast.LENGTH_SHORT).show()
            }
        }

        loadVehicleList() // Carga la lista inicial
    }

    override fun onResume() {
        super.onResume()
        // Recarga la lista cada vez que la actividad vuelve a estar en primer plano
        // (por ejemplo, después de registrar/editar un nuevo vehículo)
        loadVehicleList()
    }

    private fun loadVehicleList() {
        val cursor = dbHelper.getAllVehicles()
        val vehicles = mutableListOf<Vehicle>()
        if (cursor != null && cursor.moveToFirst()) {
            val plateColumnIndex = cursor.getColumnIndex("plate")
            val nameColumnIndex = cursor.getColumnIndex("name")
            val lastnameColumnIndex = cursor.getColumnIndex("lastname")

            if (plateColumnIndex != -1 && nameColumnIndex != -1 && lastnameColumnIndex != -1) {
                do {
                    val plate = cursor.getString(plateColumnIndex)
                    val name = cursor.getString(nameColumnIndex)
                    val lastname = cursor.getString(lastnameColumnIndex)
                    vehicles.add(Vehicle(plate, name, lastname))
                } while (cursor.moveToNext())
            } else {
                Toast.makeText(this, "Columnas de vehículo no encontradas en la base de datos", Toast.LENGTH_LONG).show()
            }
        }
        cursor?.close()

        adapter.updateList(vehicles)
    }
}