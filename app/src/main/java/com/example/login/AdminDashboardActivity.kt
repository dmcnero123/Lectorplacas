package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var btnRegistrarNuevoPersonal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        // Enlazar los elementos de UI existentes
        val tvNombreAdministrador: TextView = findViewById(R.id.tv_nombre_administrador)
        val btnListaIngresantes: Button = findViewById(R.id.btn_lista_ingresantes)
        val btnPersonalDashboard: Button = findViewById(R.id.btn_personal_dashboard)
        val btnVehiculosRegistrados: Button = findViewById(R.id.btn_vehiculos_registrados)

        // Nuevo: Enlazar el botón "Registrar Personal"
        btnRegistrarNuevoPersonal = findViewById(R.id.btn_registrar_nuevo_personal_admin)


        // Configurar OnClickListeners para los botones
        btnListaIngresantes.setOnClickListener {
            Toast.makeText(this, "Abriendo Lista de Ingresantes", Toast.LENGTH_SHORT).show()
            // Aquí iría el Intent para abrir la actividad de "Lista de Ingresantes"
        }

        btnPersonalDashboard.setOnClickListener {
            val intent = Intent(this, PersonnelListActivity::class.java) // <-- ¡CAMBIO AQUÍ!
            startActivity(intent)
        }

        btnVehiculosRegistrados.setOnClickListener {
            val intent = Intent(this, VehicleListActivity::class.java) // <-- NAVEGA A VehicleListActivity
            startActivity(intent)
        }

        // Lógica para el nuevo botón "Registrar Personal"
        btnRegistrarNuevoPersonal.setOnClickListener {
            val intent = Intent(this, RegisterPersonalActivity::class.java) // <-- ¡CAMBIO AQUÍ!
            startActivity(intent)

        }
    }
}