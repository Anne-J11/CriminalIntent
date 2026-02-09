package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
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

    private val args: CrimeDetailFragmentArgs by navArgs()
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeListViewModelFactory(args.incidentID)
    }



   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        incident = Crime(
            id = UUID.randomUUID(),
            titre = "",
            date = Date(),
            estResolu = false
        )
    }*/

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
                crimeDetailViewModel.majIncident {
                ancienIncident -> ancienIncident.copy(titre = text.toString())}
            }

            btnDateCrime.apply {
                isEnabled = false
            }

            cbCrimeResolu.setOnCheckedChangeListener { _, estCoche ->
                crimeDetailViewModel.majIncident {
                ancienIncident -> ancienIncident.copy(estResolu = estCoche)}
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            crimeDetailViewModel.incident.collect {
                incident -> incident?.let { majUI(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libération de la référence au binding pour éviter les fuites mémoire
        _binding = null
    }
}