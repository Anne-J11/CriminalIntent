package com.example.criminalintent

import android.app.Application
import android.util.Log

class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Vérifier que le fichier asset est bien accessible
        try {
            val fichiers = assets.list("") ?: emptyArray()
            Log.d("CriminalIntent", "Fichiers dans assets : ${fichiers.joinToString()}")
        } catch (e: Exception) {
            Log.e("CriminalIntent", "Erreur lecture assets : ${e.message}")
        }

        CrimeRepository.initialiser(this)
        Log.d("CriminalIntent", "Repository initialisé")
    }
}