package sv.edu.udb.escuela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ActualizarEstudianteActivity : AppCompatActivity() {

    private lateinit var autoNombre: AutoCompleteTextView
    private lateinit var edtNuevoGrado: EditText
    private lateinit var edtNuevaEdad: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private val db = FirebaseFirestore.getInstance()
    private val listaNombres = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_estudiante)

        autoNombre = findViewById(R.id.autoNombre)
        edtNuevoGrado = findViewById(R.id.edtNuevoGrado)
        edtNuevaEdad = findViewById(R.id.edtNuevaEdad)
        btnActualizar = findViewById(R.id.btnActualizarEstudiante)
        btnEliminar = findViewById(R.id.btnEliminarEstudiante)

        cargarNombres()

        btnActualizar.setOnClickListener {
            val nombre = autoNombre.text.toString().trim()
            val nuevoGrado = edtNuevoGrado.text.toString().trim()
            val nuevaEdad = edtNuevaEdad.text.toString().trim().toIntOrNull()

            if (nombre.isEmpty() || nuevoGrado.isEmpty() || nuevaEdad == null) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("estudiantes")
                .whereEqualTo("nombre", nombre)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        Toast.makeText(this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val docId = result.documents[0].id
                    db.collection("estudiantes").document(docId)
                        .update(mapOf("grado" to nuevoGrado, "edad" to nuevaEdad))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                }
        }

        btnEliminar.setOnClickListener {
            val nombre = autoNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa el nombre del estudiante", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("estudiantes")
                .whereEqualTo("nombre", nombre)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        Toast.makeText(this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val docId = result.documents[0].id
                    db.collection("estudiantes").document(docId).delete()

                    // Eliminar sus notas también
                    db.collection("notas")
                        .whereEqualTo("estudiante", nombre)
                        .get()
                        .addOnSuccessListener { notas ->
                            for (notaDoc in notas) {
                                db.collection("notas").document(notaDoc.id).delete()
                            }
                            Toast.makeText(this, "Estudiante y notas eliminadas", Toast.LENGTH_SHORT).show()
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
}