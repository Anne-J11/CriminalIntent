package com.example.criminalintent

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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

private const val FORMAT_DATE = "EEEE d MMMM yyyy"

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

    private val choixSuspect = registerForActivityResult(ActivityResultContracts.PickContact()) { uri: Uri? ->
        uri?.let { traiterSelectionContact(it) }
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

            suspectIncident.setOnClickListener {
                choixSuspect.launch(null)
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

        // Écouter le résultat de TimePickerFragment
        setFragmentResultListener(TimePickerFragment.TIME_REQUEST_KEY) { _, bundle ->
            val nouvelleDate = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_TIME) as Date
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

            btnDateCrime.text = DateFormat.format(FORMAT_DATE, incident.date).toString()
            btnDateCrime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(incident.date)
                )
            }

            btnTimeCrime.text = DateFormat.format("HH:mm", incident.date).toString()
            btnTimeCrime.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectTime(incident.date)
                )
            }

            cbCrimeResolu.isChecked = incident.estResolu

            rapportIncident.setOnClickListener {
                val rapportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getRapportIncident(incident))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.rapport_incident_sujet))
                }
                val selecteurIntent = Intent.createChooser(rapportIntent, getString(R.string.envoi_rapport))
                startActivity(selecteurIntent)
            }

            suspectIncident.text = incident.suspect.ifEmpty {
                getString(R.string.texte_choix_suspect)
            }

            val intentChoixSuspect = choixSuspect.contract.createIntent(requireContext(), null)
            suspectIncident.isEnabled = peutResoudreIntent(intentChoixSuspect)
        }
    }

    private fun getRapportIncident(incident: Crime): String {
        val texteResolu = if (incident.estResolu) {
            getString(R.string.rapport_incident_resolu)
        } else {
            getString(R.string.rapport_incident_pas_resolu)
        }
        val texteDate = DateFormat.format(FORMAT_DATE, incident.date).toString()
        val texteSuspect = if (incident.suspect.isBlank()) {
            getString(R.string.rapport_incident_pas_suspect)
        } else {
            getString(R.string.rapport_incident_suspect, incident.suspect)
        }
        return getString(
            R.string.rapport_incident,
            incident.titre, texteDate, texteResolu, texteSuspect
        )
    }

    private fun traiterSelectionContact(contactUri: Uri) {
        val champsRequete = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val pointeurRequete = requireActivity().contentResolver
            .query(contactUri, champsRequete, null, null, null)
        pointeurRequete?.use { pointeur ->
            if (pointeur.moveToFirst()) {
                val suspect = pointeur.getString(0)
                crimeDetailViewModel.majIncident { ancienIncident -> ancienIncident.copy(suspect = suspect) }
            }
        }
    }

    private fun peutResoudreIntent(intention: Intent) : Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val activiteResolue: ResolveInfo? = packageManager.resolveActivity(
            intention,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return activiteResolue != null
    }
}