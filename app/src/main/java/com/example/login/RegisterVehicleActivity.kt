package com.example.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.login.DBHelper

class RegisterVehicleActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var tvTitle: TextView
    private lateinit var etPlate: EditText
    private lateinit var etName: EditText
    private lateinit var etLastname: EditText
    private lateinit var btnSave: Button

    private var isEditMode: Boolean = false
    private var originalPlate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_vehicle)

        dbHelper = DBHelper(this)

        tvTitle = findViewById(R.id.tv_register_vehicle_title)
        etPlate = findViewById(R.id.et_plate_register)
        etName = findViewById(R.id.et_name_register)
        etLastname = findViewById(R.id.et_lastname_register)
        btnSave = findViewById(R.id.btn_save_vehicle)

        // Verificar si estamos en modo edición
        isEditMode = intent.getBooleanExtra("EDIT_MODE", false)
        if (isEditMode) {
            tvTitle.text = "Editar Vehículo"
            btnSave.text = "Guardar Cambios"
            originalPlate = intent.getStringExtra("VEHICLE_PLATE_TO_EDIT")
            etPlate.setText(originalPlate)
            etName.setText(intent.getStringExtra("VEHICLE_NAME_TO_EDIT"))
            etLastname.setText(intent.getStringExtra("VEHICLE_LASTNAME_TO_EDIT"))

            // Opcional: Deshabilitar la edición de la placa si la placa es la clave primaria y no debe cambiar
            // etPlate.isEnabled = false
        } else {
            tvTitle.text = "Nuevo Vehículo"
            btnSave.text = "Registrar"
        }

        btnSave.setOnClickListener {
            val plate = etPlate.text.toString().trim().uppercase() // Asegúrate de guardar la placa en mayúsculas
            val name = etName.text.toString().trim()
            val lastname = etLastname.text.toString().trim()

            if (plate.isEmpty() || name.isEmpty() || lastname.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                // Modo edición
                if (originalPlate != null) {
                    val isUpdated = dbHelper.updateVehicle(originalPlate!!, plate, name, lastname)
                    if (isUpdated) {
                        Toast.makeText(this, "Vehículo actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        finish() // Vuelve a la lista de vehículos
                    } else {
                        Toast.makeText(this, "Error al actualizar el vehículo. ¿La nueva placa ya existe?", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                // Modo registro (nuevo vehículo)
                val checkPlate = dbHelper.checkVehicleByPlate(plate)
                if (checkPlate == true) {
                    Toast.makeText(this, "La placa '$plate' ya está registrada", Toast.LENGTH_SHORT).show()
                } else {
                    val insertSuccess = dbHelper.insertVehicle(plate, name, lastname)
                    if (insertSuccess == true) {
                        Toast.makeText(this, "Vehículo con placa '$plate' registrado exitosamente!", Toast.LENGTH_LONG).show()
                        // Limpiar campos o cerrar actividad
                        etPlate.text.clear()
                        etName.text.clear()
                        etLastname.text.clear()
                        // finish() // Puedes cerrar la actividad para volver a la lista automáticamente
                    } else {
                        Toast.makeText(this, "Error al registrar el vehículo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}