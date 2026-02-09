package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailViewModel(incidentID: UUID) {
    private val depotIncidents = CrimeRepository.get()
    private val _incident: MutableStateFlow<Crime?> = MutableStateFlow(null)
    val incident: StateFlow<Crime?> = _incident.asSharedFlow()

    init {
        viewModelScope.launch{
            _incident.value = depotIncidents.getIncident(incidentID)
        }
    }

    fun majIncident(onUpdate: (Crime) -> Crime){
        _incident.update {

        ancienIncident -> ancienIncident?.let { onUpdate(it) }
        }
    }
}

class CrimeListViewModelFactory(private val incidentID: UUID) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(incidentID) as T
    }
}