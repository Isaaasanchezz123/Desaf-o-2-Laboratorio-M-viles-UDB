package sv.edu.udb.escuela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarNotaActivity : AppCompatActivity() {

    private lateinit var spinnerEstudiante: Spinner
    private lateinit var spinnerGrado: Spinner
    private lateinit var spinnerMateria: Spinner
    private lateinit var notaField: EditText
    private lateinit var guardarButton: Button
    private lateinit var db: FirebaseFirestore

    private val listaEstudiantes = mutableListOf<String>()
    private val grados = listOf("1°", "2°", "3°", "4°", "5°", "6°")
    private val materias = listOf("Matemática", "Lenguaje", "Ciencias", "Sociales", "Inglés")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_nota)

        spinnerEstudiante = findViewById(R.id.spinnerEstudiante)
        spinnerGrado = findViewById(R.id.spinnerGrado)
        spinnerMateria = findViewById(R.id.spinnerMateria)
        notaField = findViewById(R.id.editTextNota)
        guardarButton = findViewById(R.id.buttonGuardarNota)

        db = FirebaseFirestore.getInstance()

        cargarEstudiantes()
        cargarGrados()
        cargarMaterias()

        guardarButton.setOnClickListener {
            guardarNota()
        }
    }

    private fun cargarEstudiantes() {
        db.collection("estudiantes").get().addOnSuccessListener { result ->
            listaEstudiantes.clear()
            for (document in result) {
                val nombre = document.getString("nombre")
                if (!nombre.isNullOrBlank()) {
                    listaEstudiantes.add(nombre)
                }
            }

            if (listaEstudiantes.isEmpty()) {
                listaEstudiantes.add("Sin estudiantes registrados")
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEstudiantes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstudiante.adapter = adapter
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarGrados() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrado.adapter = adapter
    }

    private fun cargarMaterias() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, materias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMateria.adapter = adapter
    }

    private fun guardarNota() {
        val estudiante = spinnerEstudiante.selectedItem?.toString() ?: ""
        val grado = spinnerGrado.selectedItem?.toString() ?: ""
        val materia = spinnerMateria.selectedItem?.toString() ?: ""
        val notaTexto = notaField.text.toString().trim()

        val notaValor = notaTexto.toDoubleOrNull()
        if (estudiante.isBlank() || estudiante == "Sin estudiantes registrados" ||
            grado.isBlank() || materia.isBlank() || notaValor == null || notaValor !in 0.0..10.0
        ) {
            Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val registro = hashMapOf(
            "estudiante" to estudiante,
            "grado" to grado,
            "materia" to materia,
            "nota" to notaValor
        )

        db.collection("notas").add(registro)
            .addOnSuccessListener {
                Toast.makeText(this, "Nota registrada correctamente", Toast.LENGTH_SHORT).show()
                notaField.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar nota", Toast.LENGTH_SHORT).show()
            }
    }
}