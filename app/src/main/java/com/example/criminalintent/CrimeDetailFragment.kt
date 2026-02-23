package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch

class CrimeDetailFragment : Fragment() {

    // Propriété nullable pour gérer la destruction de la vue
    private var _binding: FragmentCrimeDetailBinding? = null

    // Propriété non-nullable pour un accès facile dans le code
    private val binding
        get() = checkNotNull(_binding) {
            "Impossible d'accéder au binding car la vue est null. Est-ce que la vue a été créée ?"
        }

    // Récupération des arguments de navigation (l'ID de l'incident)
    private val args: CrimeDetailFragmentArgs by navArgs()

    // Création du ViewModel avec la fabrique (factory)
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.incidentID)
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
            // Écouteur pour le titre de l'incident
            etTitleCrime.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.majIncident { ancienIncident ->
                    ancienIncident.copy(titre = text.toString())
                }
            }

            // Le bouton de date est désactivé pour l'instant
            btnDateCrime.apply {}

            btnTimeCrime.apply {}

            // Écouteur pour la case à cocher
            cbCrimeResolu.setOnCheckedChangeListener { _, estCoche ->
                crimeDetailViewModel.majIncident { ancienIncident ->
                    ancienIncident.copy(estResolu = estCoche)
                }
            }
        }

        // Observer les changements de l'incident
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.incident.collect { incident ->
                    incident?.let { majUI(it) }
                }
            }
        }
        setFragmentResultListener(DatePickerFragment.DATE_REQUEST_KEY) { _, bundle ->
            val nouvelleDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.majIncident {
                it.copy(date = nouvelleDate)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libération de la référence au binding pour éviter les fuites mémoire
        _binding = null
    }

    // Fonction privée pour mettre à jour l'interface
    private fun majUI(incident: Crime) {
        binding.apply {
            // Vérifier que le texte est différent avant de le mettre à jour
            // pour éviter une boucle infinie
            if (etTitleCrime.text.toString() != incident.titre) {
                etTitleCrime.setText(incident.titre)
            }
            btnDateCrime.text = incident.date.toString()
            btnDateCrime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(incident.date)
                )
            }
            cbCrimeResolu.isChecked = incident.estResolu
        }
    }
}