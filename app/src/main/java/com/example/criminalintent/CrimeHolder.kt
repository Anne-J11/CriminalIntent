package com.example.criminalintent

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.databinding.ListItemCrimeBinding
import java.util.UUID

class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(incident: Crime, onCrimeClicked: (incidentID: UUID) -> Unit) {
        binding.crimeTitre.text = incident.titre
        binding.crimeDate.text = incident.date.toString()

        // Définir le listener de clic sur toute la vue
        binding.root.setOnClickListener {
            onCrimeClicked(incident.id)
        }

        // Afficher ou masquer l'icône "résolu"
        binding.resolu.visibility = if (incident.estResolu) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}