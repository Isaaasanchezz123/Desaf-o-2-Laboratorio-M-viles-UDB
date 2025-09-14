package sv.edu.udb.escuela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarEstudianteActivity : AppCompatActivity() {

    private lateinit var nombreField: EditText
    private lateinit var edadField: EditText
    private lateinit var direccionField: EditText
    private lateinit var telefonoField: EditText
    private lateinit var guardarButton: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_estudiante)

        nombreField = findViewById(R.id.editTextNombre)
        edadField = findViewById(R.id.editTextEdad)
        direccionField = findViewById(R.id.editTextDireccion)
        telefonoField = findViewById(R.id.editTextTelefono)
        guardarButton = findViewById(R.id.buttonGuardarEstudiante)

        db = FirebaseFirestore.getInstance()

        guardarButton.setOnClickListener {
            val nombre = nombreField.text.toString().trim()
            val edad = edadField.text.toString().trim()
            val direccion = direccionField.text.toString().trim()
            val telefono = telefonoField.text.toString().trim()

            if (nombre.isEmpty() || edad.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val estudiante = hashMapOf(
                "nombre" to nombre,
                "edad" to edad,
                "direccion" to direccion,
                "telefono" to telefono
            )

            db.collection("estudiantes")
                .add(estudiante)
                .addOnSuccessListener {
                    Toast.makeText(this, "Estudiante registrado", Toast.LENGTH_SHORT).show()
                    nombreField.text.clear()
                    edadField.text.clear()
                    direccionField.text.clear()
                    telefonoField.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}