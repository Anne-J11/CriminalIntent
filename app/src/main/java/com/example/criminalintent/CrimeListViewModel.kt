package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {
    private val depotIncidents = CrimeRepository.get()
    private val _listeIncidents: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())

    val listeIncidents: StateFlow<List<Crime>>
        get() = _listeIncidents.asStateFlow()


    init {
        viewModelScope.launch {
            depotIncidents.getIncidents().collect {
                _listeIncidents.value = it
            }

        }
    }

     fun getIncidents() : Flow<List<Crime>> {
        return depotIncidents.getIncidents()
    }

}