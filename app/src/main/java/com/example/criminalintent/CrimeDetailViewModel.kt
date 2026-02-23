package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailViewModel(incidentID: UUID) : ViewModel() {
    private val depotIncidents = CrimeRepository.get()

    private val _incident: MutableStateFlow<Crime?> = MutableStateFlow(null)
    val incident: StateFlow<Crime?> = _incident.asStateFlow()

    init {
        viewModelScope.launch {
            _incident.value = depotIncidents.getIncident(incidentID)
        }
    }

    fun majIncident(onUpdate: (Crime) -> Crime) {
        _incident.update { ancienIncident ->
            ancienIncident?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        // Sauvegarder l'incident dans la BD avant la destruction du ViewModel
        incident.value?.let { incidentActuel ->
            depotIncidents.majIncident(incidentActuel)
        }
    }
}

class CrimeDetailViewModelFactory(
    private val incidentID: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CrimeDetailViewModel(incidentID) as T
    }
}