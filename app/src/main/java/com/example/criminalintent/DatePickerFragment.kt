package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class DatePickerFragment : DialogFragment() {
    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ecouterDate = DatePickerDialog.OnDateSetListener {
            _: DatePicker, annee: Int, mois: Int, jour: Int ->
            val dateResult = GregorianCalendar(annee, mois, jour).time
            setFragmentResult(DATE_REQUEST_KEY, bundleOf(DATE_BUNDLE_KEY to dateResult)
        }
        val calendar = Calendar.getInstance()
        calendar.time = args.dateIncident
        val annee = calendar.get(Calendar.YEAR)
        val  mois = calendar.get(Calendar.MONTH)
        val jour = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            ecouterDate,
            null,
            annee,
            mois,
            jour
        )
    }

    companion object{
        const val DATE_REQUEST_KEY = "DATE_REQUEST_KEY"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"

    }
}