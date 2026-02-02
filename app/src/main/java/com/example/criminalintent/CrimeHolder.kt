package com.example.criminalintent

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.databinding.ListItemCrimeBinding


class CrimeHolder(

    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(incident: Crime){
        binding.crimeTitre.text = incident.titre
        binding.crimeDate.text = incident.date.toString()
        binding.root.setOnClickListener { Toast.makeText(binding.root.context,"Clic sur ${incident.titre}",Toast.LENGTH_SHORT).show()
        }
        binding.resolu.visibility = if (incident.estResolu){
            View.VISIBLE
        }else{
            View.GONE}
        }
}
