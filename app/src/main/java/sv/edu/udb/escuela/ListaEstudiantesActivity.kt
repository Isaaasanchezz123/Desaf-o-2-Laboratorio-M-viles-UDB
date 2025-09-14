package sv.edu.udb.escuela

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ListaEstudiantesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_estudiantes)

        recyclerView = findViewById(R.id.recyclerNotas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("notas").get().addOnSuccessListener { result ->
            val listaNotas = result.map { it.toObject(Nota::class.java) }

            // Ordenar grados manualmente
            val ordenGrados = listOf("1°", "2°", "3°", "4°", "5°", "6°")
            val listaOrdenada = listaNotas.sortedBy { ordenGrados.indexOf(it.grado) }

            recyclerView.adapter = NotaAdapter(listaOrdenada)
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
        }
    }
}