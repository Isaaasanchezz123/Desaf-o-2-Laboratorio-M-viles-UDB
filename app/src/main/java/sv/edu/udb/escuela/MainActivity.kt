package sv.edu.udb.escuela

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var btnRegistrarEstudiante: Button
    private lateinit var btnRegistrarNota: Button
    private lateinit var btnVerEstudiantesFinal: Button
    private lateinit var btnActualizarEstudiante: Button
    private lateinit var btnActualizarNota: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        try {
            btnRegistrarEstudiante = findViewById(R.id.btnRegistrarEstudiante)
            btnRegistrarNota = findViewById(R.id.btnRegistrarNota)
            btnVerEstudiantesFinal = findViewById(R.id.btnVerEstudiantesFinal)
            btnActualizarEstudiante = findViewById(R.id.btnActualizarEstudiante)
            btnActualizarNota = findViewById(R.id.btnActualizarNota)
            btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar botones: ${e.message}", Toast.LENGTH_LONG).show()
            return
        }

        btnRegistrarEstudiante.setOnClickListener {
            startActivity(Intent(this, RegistrarEstudianteActivity::class.java))
        }

        btnRegistrarNota.setOnClickListener {
            startActivity(Intent(this, RegistrarNotaActivity::class.java))
        }

        btnVerEstudiantesFinal.setOnClickListener {
            startActivity(Intent(this, ListaEstudiantesActivity::class.java))
        }

        btnActualizarEstudiante.setOnClickListener {
            startActivity(Intent(this, ActualizarEstudianteActivity::class.java))
        }

        btnActualizarNota.setOnClickListener {
            startActivity(Intent(this, ActualizarNotasActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}