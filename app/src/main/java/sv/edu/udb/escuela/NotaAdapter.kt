package sv.edu.udb.escuela

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotaAdapter(private val listaNotas: List<Nota>) :
    RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val estudiante: TextView = itemView.findViewById(R.id.textEstudiante)
        val grado: TextView = itemView.findViewById(R.id.textGrado)
        val materia: TextView = itemView.findViewById(R.id.textMateria)
        val nota: TextView = itemView.findViewById(R.id.textNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = listaNotas[position]
        holder.estudiante.text = nota.estudiante
        holder.grado.text = "Grado: ${nota.grado}"
        holder.materia.text = "Materia: ${nota.materia}"
        holder.nota.text = "Nota: ${nota.nota}"
    }

    override fun getItemCount(): Int = listaNotas.size
}