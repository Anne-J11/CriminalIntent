package com.example.criminalintent

import android.os.Bundle
import kotlinx.coroutines.flow.Flow
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch

class CrimeListFragment : Fragment() {
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    //private var tache: Job? = null
    private var _binding: FragmentCrimeListBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Impossible d'accéder au binding car elle est null. Est-ce que la vue est visible ?"
        }

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.listeIncidents.size}")

    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        /*val incidents = crimeListViewModel.listeIncidents
        val adapteur = CrimeListAdapter(incidents)
        binding.crimeRecyclerView.adapter = adapteur*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                crimeListViewModel.listeIncidents.collect{
                listeIncidents -> binding.crimeRecyclerView.adapter = CrimeListAdapter(listeIncidents)
            }}
        }
    }

    /*override fun onStart() {
        super.onStart()
        tache = viewLifecycleOwner.lifecycleScope.launch {
            val incidents = crimeListViewModel.chargerIncidents()
            binding.crimeRecyclerView.adapter = CrimeListAdapter(incidents)
        }
    }

    override fun onStop() {
        super.onStop()
        tache?.cancel()
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}