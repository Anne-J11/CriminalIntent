package com.example.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.GregorianCalendar

class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val ecouterHeure = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, heure: Int, minute: Int ->
            val calendar = GregorianCalendar()
            // Conserver la date existante et seulement changer l'heure/minute
            calendar.time = args.dateIncident
            calendar.set(Calendar.HOUR_OF_DAY, heure)
            calendar.set(Calendar.MINUTE, minute)
            val dateResult = calendar.time
            setFragmentResult(
                TIME_REQUEST_KEY,
                Bundle().apply { putSerializable(BUNDLE_KEY_TIME, dateResult) }
            )
        }

        val calendar = Calendar.getInstance()
        calendar.time = args.dateIncident
        val heure  = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            ecouterHeure,
            heure,
            minute,
            true   // format 24h
        )
    }

    companion object {
        const val TIME_REQUEST_KEY = "TIME_REQUEST_KEY"
        const val BUNDLE_KEY_TIME  = "BUNDLE_KEY_TIME"
    }
}