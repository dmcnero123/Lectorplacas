package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.DBHelper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PersonnelListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonnelAdapter
    private val personnelList = mutableListOf<String>() // Lista mutable para los nombres de usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personnel_list)

        dbHelper = DBHelper(this)

        recyclerView = findViewById(R.id.rv_personnel_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador y pasarlo al RecyclerView
        adapter = PersonnelAdapter(personnelList) { username ->
            // Aquí manejas el clic en un elemento de la lista (opcional)
            Toast.makeText(this, "Seleccionado: $username", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val btnNuevo: Button = findViewById(R.id.btn_nuevo_personnel)
        val btnEditar: Button = findViewById(R.id.btn_editar_personnel)
        val btnEliminar: Button = findViewById(R.id.btn_eliminar_personnel)

        // Botón "Nuevo" - Navegar a la pantalla de registro de personal
        btnNuevo.setOnClickListener {
            val intent = Intent(this, RegisterPersonalActivity::class.java)
            startActivity(intent)
        }

        // Botón "Editar" (funcionalidad pendiente, por ahora solo un Toast)
        btnEditar.setOnClickListener {
            val selectedUser = adapter.getSelectedUser()
            if (selectedUser != null) {
                Toast.makeText(this, "Editar usuario: $selectedUser", Toast.LENGTH_SHORT).show()
                // Aquí iría el Intent para una actividad de edición, pasando el 'selectedUser'
            } else {
                Toast.makeText(this, "Por favor, selecciona un usuario para editar", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón "Eliminar" - Con diálogo de confirmación
        btnEliminar.setOnClickListener {
            val selectedUser = adapter.getSelectedUser()
            if (selectedUser != null) {
                AlertDialog.Builder(this)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estás seguro de que quieres eliminar a '$selectedUser'?")
                    .setPositiveButton("Sí") { dialog, _ ->
                        val isDeleted = dbHelper.deleteUser(selectedUser) // Llama al método de tu DBHelper
                        if (isDeleted) {
                            adapter.removeSelectedUser() // Elimina de la lista visible
                            Toast.makeText(this, "'$selectedUser' eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al eliminar a '$selectedUser'", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                Toast.makeText(this, "Por favor, selecciona un usuario para eliminar", Toast.LENGTH_SHORT).show()
            }
        }

        loadPersonnelList() // Carga la lista inicial al crear la actividad
    }

    override fun onResume() {
        super.onResume()
        // Recarga la lista cada vez que la actividad vuelve a estar en primer plano
        // (por ejemplo, después de registrar un nuevo usuario en RegisterPersonalActivity)
        loadPersonnelList()
    }

    private fun loadPersonnelList() {
        val cursor = dbHelper.getAllUsers()
        val users = mutableListOf<String>()
        if (cursor != null && cursor.moveToFirst()) {
            val usernameColumnIndex = cursor.getColumnIndex("username")
            if (usernameColumnIndex != -1) { // Asegurarse de que la columna existe
                do {
                    users.add(cursor.getString(usernameColumnIndex))
                } while (cursor.moveToNext())
            } else {
                Toast.makeText(this, "Columna 'username' no encontrada en la base de datos", Toast.LENGTH_LONG).show()
            }
        }
        cursor?.close() // Cierra el cursor para evitar fugas de memoria

        adapter.updateList(users) // Actualiza la lista en el adaptador
    }
}