package sv.edu.udb.escuela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ActualizarNotasActivity : AppCompatActivity() {

    private lateinit var autoNombre: AutoCompleteTextView
    private lateinit var textGrado: TextView
    private lateinit var spinnerMateria: Spinner
    private lateinit var edtNuevaNota: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button

    private val db = FirebaseFirestore.getInstance()
    private val listaNombres = mutableListOf<String>()
    private val listaMaterias = listOf("Matemática", "Lenguaje", "Ciencias", "Sociales", "Inglés")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_notas)

        autoNombre = findViewById(R.id.autoNombre)
        textGrado = findViewById(R.id.textGrado)
        spinnerMateria = findViewById(R.id.spinnerMateria)
        edtNuevaNota = findViewById(R.id.edtNuevaNota)
        btnActualizar = findViewById(R.id.btnActualizarNota)
        btnEliminar = findViewById(R.id.btnEliminarNota)

        cargarNombres()
        cargarMaterias()

        autoNombre.setOnItemClickListener { _, _, _, _ ->
            val nombre = autoNombre.text.toString().trim()
            obtenerGrado(nombre)
        }

        btnActualizar.setOnClickListener {
            val nombre = autoNombre.text.toString().trim()
            val materia = spinnerMateria.selectedItem.toString()
            val notaTexto = edtNuevaNota.text.toString().trim()
            val notaValor = notaTexto.toDoubleOrNull()

            if (nombre.isEmpty() || notaValor == null || notaValor !in 0.0..10.0) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("notas")
                .whereEqualTo("estudiante", nombre)
                .whereEqualTo("materia", materia)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        Toast.makeText(this, "Nota no encontrada", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val docId = result.documents[0].id
                    db.collection("notas").document(docId)
                        .update("nota", notaValor)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
                            edtNuevaNota.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                }
        }

        btnEliminar.setOnClickListener {
            val nombre = autoNombre.text.toString().trim()
            val materia = spinnerMateria.selectedItem.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa el nombre del estudiante", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("notas")
                .whereEqualTo("estudiante", nombre)
                .whereEqualTo("materia", materia)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        Toast.makeText(this, "Nota no encontrada", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val docId = result.documents[0].id
                    db.collection("notas").document(docId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
                            edtNuevaNota.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }

    private fun cargarNombres() {
        db.collection("estudiantes").get().addOnSuccessListener { result ->
            listaNombres.clear()
            for (doc in result) {
                doc.getString("nombre")?.let { listaNombres.add(it) }
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaNombres)
            autoNombre.setAdapter(adapter)
        }
    }

    private fun cargarMaterias() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaMaterias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMateria.adapter = adapter
    }

    private fun obtenerGrado(nombre: String) {
        db.collection("estudiantes")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val grado = result.documents[0].getString("grado") ?: "No registrado"
                    textGrado.text = "Grado: $grado"
                } else {
                    textGrado.text = "Grado: No encontrado"
                }
            }
    }
}