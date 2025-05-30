package com.example.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val personalButton: Button = findViewById(R.id.btn_personal)
        val administratorButton: Button = findViewById(R.id.btn_administrador)

        personalButton.setOnClickListener {
            // Acción cuando se hace clic en el botón "Personal"
            // Ahora navegamos a la PersonalLoginActivity
            val intent = Intent(this, PersonalLoginActivity::class.java)
            startActivity(intent)
        }

        administratorButton.setOnClickListener {
            // Acción cuando se hace clic en el botón "Administrador"
            // Ahora navegamos a la AdminLoginActivity
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }
}