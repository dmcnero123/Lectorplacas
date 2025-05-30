package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin_login)

        val etUsuarioAdmin: EditText = findViewById(R.id.et_usuario)
        val etContrasenaAdmin: EditText = findViewById(R.id.et_contrasena)
        val btnIniciarSesionAdmin: Button = findViewById(R.id.btn_iniciar_sesion_admin_login)

        btnIniciarSesionAdmin.setOnClickListener {
            val usuario = etUsuarioAdmin.text.toString()
            val contrasena = etContrasenaAdmin.text.toString()

            // Administrator authentication logic
            if (usuario == "tupapi" && contrasena == "1234") { // Example credentials
                Toast.makeText(this, "Inicio de sesión de Administrador exitoso!", Toast.LENGTH_SHORT).show()

                // *** NAVIGATE TO ADMIN DASHBOARD ACTIVITY ***
                val intent = Intent(this, AdminDashboardActivity::class.java)
                // Optional: Pass the username to the next activity
                // intent.putExtra("USERNAME", usuario)
                startActivity(intent)
                finish() // Optional: Close the login activity so the user can't go back with the back button
            } else {
                Toast.makeText(this, "Usuario o contraseña de Administrador incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}