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

class PersonalLoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper // Declara una instancia de tu DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_login)

        // Inicializar tu DBHelper
        dbHelper = DBHelper(this) // 'this' es el Contexto de la actividad

        val etUsuario: EditText = findViewById(R.id.et_usuario)
        val etContrasena: EditText = findViewById(R.id.et_contrasena)
        val btnIniciarSesion: Button = findViewById(R.id.btn_iniciar_sesion_login)



        btnIniciarSesion.setOnClickListener {
            val username = etUsuario.text.toString().trim()
            val password = etContrasena.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar al método de tu DBHelper para verificar usuario y contraseña
            val checkUserPass = dbHelper.checkusernamepassword(username, password)

            if (checkUserPass == true) { // Si el login es exitoso
                Toast.makeText(this, "Inicio de sesión de Personal exitoso para: $username", Toast.LENGTH_LONG).show()
                val intent = Intent(this, PersonalDashboardActivity::class.java) // Asegúrate de que esta actividad exista
                intent.putExtra("USERNAME", username)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}