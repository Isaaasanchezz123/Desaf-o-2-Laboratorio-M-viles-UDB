package sv.edu.udb.escuela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class VerNotasActivity : AppCompatActivity() {

    private lateinit var autoNombre: AutoCompleteTextView
    private lateinit var buttonBuscar: Button
    private lateinit var textGrado: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotaAdapter
    private lateinit var db: FirebaseFirestore
    private val listaNotas = mutableListOf<Nota>()
    private val listaNombres = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_notas)

        autoNombre = findViewById(R.id.autoNombre)
        buttonBuscar = findViewById(R.id.buttonBuscar)
        textGrado = findViewById(R.id.textGrado)
        recyclerView = findViewById(R.id.recyclerNotas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotaAdapter(listaNotas)
        recyclerView.adapter = adapter

        db = FirebaseFirestore.getInstance()

        cargarNombres()

        buttonBuscar.setOnClickListener {
            val nombre = autoNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa un nombre válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("notas")
                .whereEqualTo("estudiante", nombre)
                .get()
                .addOnSuccessListener { result ->
                    listaNotas.clear()
                    for (document in result) {
                        val nota = document.toObject(Nota::class.java)
                        listaNotas.add(nota)
                    }

                    if (listaNotas.isNotEmpty()) {
                        textGrado.text = "Grado: ${listaNotas[0].grado}"
                    } else {
                        textGrado.text = "Grado: No encontrado"
                        Toast.makeText(this, "No se encontraron notas", Toast.LENGTH_SHORT).show()
                    }

                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar notas", Toast.LENGTH_SHORT).show()
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