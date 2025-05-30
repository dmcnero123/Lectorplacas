package com.example.login

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Base64
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class PersonalDashboardActivity : AppCompatActivity() {

    // Declara las variables para los elementos de UI
    private lateinit var tvNombrePersonal: TextView
    private lateinit var ivVehiculo: ImageView
    private lateinit var etPlaca: EditText
    private lateinit var btnCapturar: Button
    private lateinit var btnConfirmar: Button
    private lateinit var btnNuevoVehiculo: Button
    private lateinit var btnListaIngresantes: Button

    private var capturedImageBitmap: Bitmap? = null // Para almacenar la imagen capturada

    // Launcher para solicitar permisos de la cámara
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera() // Si el permiso es concedido, abre la cámara
            } else {
                Toast.makeText(this, "Permiso de cámara denegado. No se puede tomar foto.", Toast.LENGTH_SHORT).show()
            }
        }

    // Launcher para iniciar la actividad de la cámara y obtener el resultado
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap?
                imageBitmap?.let {
                    ivVehiculo.setImageBitmap(it)
                    capturedImageBitmap = it // Guarda la imagen capturada
                    Toast.makeText(this, "Imagen capturada", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(this, "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show()
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Captura de cámara cancelada", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_dashboard)

        tvNombrePersonal = findViewById(R.id.tv_nombre_personal_dashboard)
        ivVehiculo = findViewById(R.id.iv_vehiculo)
        etPlaca = findViewById(R.id.et_placa)
        btnCapturar = findViewById(R.id.btn_capturar)
        btnConfirmar = findViewById(R.id.btn_confirmar)
        btnNuevoVehiculo = findViewById(R.id.btn_nuevo_vehiculo)
        btnListaIngresantes = findViewById(R.id.btn_lista_ingresantes_personal)

        // RECIBIR EL NOMBRE DE USUARIO Y MOSTRARLO
        val username = intent.getStringExtra("USERNAME") // Obtiene el valor con la clave "USERNAME"
        username?.let {
            tvNombrePersonal.text = "Personal: $it" // Muestra el nombre aquí
        } ?: run {
            tvNombrePersonal.text = "Personal: Desconocido" // Fallback si no se pasa el nombre
        }

        // --- Lógica del botón CAPTURAR ---
        btnCapturar.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        // --- Lógica del botón CONFIRMAR ---
        btnConfirmar.setOnClickListener {
            val placaIngresada = etPlaca.text.toString().trim()
            if (placaIngresada.isNotEmpty()) {
                // Aquí deberías verificar la placa contra tu base de datos de vehículos registrados
                // Por ahora, solo un Toast
                Toast.makeText(this, "Placa Confirmada: $placaIngresada. Verificando acceso...", Toast.LENGTH_LONG).show()

                // Simular la verificación de la base de datos (aquí usarías tu DBHelper)
                // val isAuthorized = dbHelper.checkVehicleByPlate(placaIngresada)
                // if (isAuthorized) { ... acceso permitido ... } else { ... acceso denegado ... }

                // Ejemplo de cómo navegar a una pantalla de resultado o procesar el acceso
                // Puedes crear una nueva actividad para mostrar "Acceso Permitido" o "Acceso Denegado"
                // y pasar la placa y el estado de la verificación.
            } else {
                Toast.makeText(this, "La placa no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Lógica del botón NUEVO VEHÍCULO ---
        btnNuevoVehiculo.setOnClickListener {
            Toast.makeText(this, "Funcionalidad para Nuevo Vehículo (Registro de Placa Manual)", Toast.LENGTH_SHORT).show()
            // Esta funcionalidad generalmente la haría el ADMINISTRADOR, no el personal de entrada.
            // Si el personal de entrada puede registrar nuevos vehículos, necesitarías una actividad para eso.
            // Actualmente, el registro de vehículos lo hace el administrador.
            // Considera si esta función realmente debe estar aquí o si es un error de diseño.
        }

        // --- Lógica del botón LISTA DE INGRESANTES ---
        btnListaIngresantes.setOnClickListener {
            Toast.makeText(this, "Abriendo Lista de Ingresantes del Personal", Toast.LENGTH_SHORT).show()
            // Aquí iría el Intent para abrir la actividad de "Lista de Ingresantes"
            // Por ejemplo, si tienes una actividad para esto:
            // val intent = Intent(this, IngresantesListActivity::class.java)
            // startActivity(intent)
        }
    }

    // --- Métodos para la Cámara y API ---

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            // Si ya tenemos el permiso
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            // Si debemos mostrar una explicación al usuario
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Mostrar un diálogo explicativo antes de solicitar el permiso
                Toast.makeText(this, "Necesitamos acceso a la cámara para capturar la placa del vehículo.", Toast.LENGTH_LONG).show()
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            // Solicitar el permiso
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(this, "No se encontró aplicación de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    // Este método simula el envío de la imagen a un API y la recepción de la placa
    private fun sendImageToApiAndGetPlate(imageBitmap: Bitmap) {
        Toast.makeText(this, "Enviando imagen a la API...", Toast.LENGTH_SHORT).show()
        etPlaca.setText("Procesando...")
        etPlaca.isEnabled = false // Deshabilitar mientras se procesa

        CoroutineScope(Dispatchers.IO).launch {
            // 1. Convertir Bitmap a String Base64 (formato común para APIs)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            // Aquí iría tu código para hacer la petición HTTP real a la API
            // Ejemplo con Retrofit/OkHttp:
            // val response = YourApiService.uploadImage(base64Image)
            // if (response.isSuccessful) {
            //     val placaObtenida = response.body()?.plate // Suponiendo que la API devuelve un objeto con un campo 'plate'
            // } else {
            //     val errorBody = response.errorBody()?.string()
            //     // Manejar error
            // }

            // 2. Simular un retraso de red y la respuesta de la API
            delay(3000) // Simula 3 segundos de procesamiento de la API

            // 3. Simular la placa recibida de la API
            val simulatedPlate = "ABC-123" // Esta sería la placa real de tu API

            withContext(Dispatchers.Main) {
                etPlaca.setText(simulatedPlate)
                etPlaca.isEnabled = true // Habilitar de nuevo
                Toast.makeText(this@PersonalDashboardActivity, "Placa recibida: $simulatedPlate", Toast.LENGTH_LONG).show()
                // Aquí, después de obtener la placa, podrías automáticamente llamar a btnConfirmar.performClick()
                // o mostrar un diálogo al usuario para que la confirme.
            }
        }
    }
}

