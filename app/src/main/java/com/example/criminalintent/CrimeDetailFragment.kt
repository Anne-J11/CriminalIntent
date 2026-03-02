package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.util.Date

class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Impossible d'accéder au binding car la vue est null."
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

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
            etTitleCrime.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.majIncident { ancienIncident ->
                    ancienIncident.copy(titre = text.toString())
                }
            }

            cbCrimeResolu.setOnCheckedChangeListener { _, estCoche ->
                crimeDetailViewModel.majIncident { ancienIncident ->
                    ancienIncident.copy(estResolu = estCoche)
                }
            }
        }

        // Observer les changements de l'incident pour mettre à jour l'UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.incident.collect { incident ->
                    incident?.let { majUI(it) }
                }
            }
        }

        // Écouter le résultat de DatePickerFragment
        setFragmentResultListener(DatePickerFragment.DATE_REQUEST_KEY) { _, bundle ->
            val nouvelleDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.majIncident { it.copy(date = nouvelleDate) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun majUI(incident: Crime) {
        binding.apply {
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