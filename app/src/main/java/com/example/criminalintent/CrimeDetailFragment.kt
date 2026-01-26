package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import java.util.Date
import java.util.UUID

class CrimeDetailFragment : Fragment() {

    // Propriété nullable pour gérer la destruction de la vue
    private var _binding: FragmentCrimeDetailBinding? = null

    // Propriété non-nullable pour un accès facile dans le code
    private val binding
        get() = checkNotNull(_binding) {
            "Impossible d'accéder au binding car la vue est null. Est-ce que la vue a été créée ?"
        }

    private lateinit var incident: Crime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        incident = Crime(
            id = UUID.randomUUID(),
            titre = "",
            date = Date(),
            estResolu = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etTitleCrime.doOnTextChanged { text, _, _, _ ->
                incident = incident.copy(titre = text.toString())
            }

            btnDateCrime.apply {
                text = incident.date.toString()
                isEnabled = false
            }

            cbCrimeResolu.setOnCheckedChangeListener { _, estCoche ->
                incident = incident.copy(estResolu = estCoche)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libération de la référence au binding pour éviter les fuites mémoire
        _binding = null
    }
}