package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val ecouterDate = DatePickerDialog.OnDateSetListener {
                _: DatePicker, annee: Int, mois: Int, jour: Int ->
            val dateResult = GregorianCalendar(annee, mois, jour).time
            setFragmentResult(
                DATE_REQUEST_KEY,
                Bundle().apply { putSerializable(BUNDLE_KEY_DATE, dateResult) }
            )
        }

        val calendar = Calendar.getInstance()
        calendar.time = args.dateIncident
        val annee = calendar.get(Calendar.YEAR)
        val mois  = calendar.get(Calendar.MONTH)
        val jour  = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            ecouterDate,
            annee,
            mois,
            jour
        )
    }

    companion object {
        const val DATE_REQUEST_KEY = "DATE_REQUEST_KEY"
        const val BUNDLE_KEY_DATE  = "BUNDLE_KEY_DATE"
    }
}