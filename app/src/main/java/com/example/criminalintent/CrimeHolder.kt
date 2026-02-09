package com.example.criminalintent

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.databinding.ListItemCrimeBinding
import java.util.UUID


class CrimeHolder(

    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(incident: Crime, onCrimeClicked: (incident: UUID) -> Unit){
        binding.crimeTitre.text = incident.titre
        binding.crimeDate.text = incident.date.toString()
        binding.root.setOnClickListener { onCrimeClicked(incident.id)
        }
        binding.resolu.visibility = if (incident.estResolu){
            View.VISIBLE
        }else{
            View.GONE}
        }
}
