package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class CrimeListFragment : Fragment() {

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var _binding: FragmentCrimeListBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Impossible d'accéder au binding car elle est null. Est-ce que la vue est visible ?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.listeIncidents.collect { listeIncidents ->
                    binding.crimeRecyclerView.adapter = CrimeListAdapter(listeIncidents) { incidentID ->
                        findNavController().navigate(
                            CrimeListFragmentDirections.afficheDetailIncident(incidentID)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.nouvel_incident ->{
                afficherNouvelIncident()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun afficherNouvelIncident(){
        viewLifecycleOwner.lifecycleScope.launch {
            val nouvelIncident = Crime (
                id = UUID.randomUUID(),
                titre = "",
                date = Date(),
                estResolu = false
            )
            crimeListViewModel.ajouterIncident(nouvelIncident)
            findNavController().navigate(
                CrimeListFragmentDirections.afficheDetailIncident(nouvelIncident.id)
            )
        }
    }
}