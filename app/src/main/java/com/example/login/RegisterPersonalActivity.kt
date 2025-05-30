package com.example.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterPersonalActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_personal)

        // Inicializar tu DBHelper
        dbHelper = DBHelper(this) // 'this' es el Contexto de la actividad

        // Obtener referencias a los elementos de UI
        val etNombre: EditText = findViewById(R.id.et_nombre_registro)
        val etContrasena: EditText = findViewById(R.id.et_contrasena_registro)
        val etRepetirContrasena: EditText = findViewById(R.id.et_repetir_contrasena_registro)
        val btnRegistrar: Button = findViewById(R.id.btn_registrar_usuario)

        btnRegistrar.setOnClickListener {
            val username = etNombre.text.toString().trim()
            val password = etContrasena.text.toString().trim()
            val repeatPassword = etRepetirContrasena.text.toString().trim()

            // 1. Validar campos vacíos
            if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Validar que las contraseñas coincidan
            if (password != repeatPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Usar DBHelper para verificar y registrar
            // NOTA: Estas operaciones se ejecutan en el hilo principal (UI thread).
            // Para una aplicación real, considera usar Coroutines o AsyncTasks.
            val checkUser = dbHelper.checkusername(username) // Verifica si el usuario ya existe

            if (checkUser == true) {
                Toast.makeText(this, "El usuario '$username' ya existe", Toast.LENGTH_SHORT).show()
            } else {
                val insertSuccess = dbHelper.insertData(username, password) // Intenta insertar el nuevo usuario
                if (insertSuccess == true) {
                    Toast.makeText(this, "Usuario '$username' registrado exitosamente!", Toast.LENGTH_LONG).show()
                    // Opcional: Limpiar los campos después de un registro exitoso
                    etNombre.text.clear()
                    etContrasena.text.clear()
                    etRepetirContrasena.text.clear()
                } else {
                    Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}