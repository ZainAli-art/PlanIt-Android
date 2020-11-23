package com.thetechannel.android.planit.newtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.thetechannel.android.planit.EventObserver
import com.thetechannel.android.planit.MyApplication
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.databinding.FragmentNewTaskBinding
import java.sql.Time
import java.util.*

class NewTaskFragment : Fragment(), View.OnClickListener {

    private val viewModel by viewModels<NewTaskViewModel> {
        NewTaskViewModelFactory((requireActivity().applicationContext as MyApplication).repository)
    }

    private lateinit var viewDataBinding: FragmentNewTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentNewTaskBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        setUpScheduling()
        setUpNavigation()
        setUpSnackBar()
    }

    private fun setUpNavigation() {
        viewModel.taskAddedEvent.observe(viewLifecycleOwner, EventObserver { added ->
            if (added) findNavController().navigateUp()
        })
    }

    private fun setUpSnackBar() {
        viewModel.snackBarText.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(requireView(), resources.getString(it), Snackbar.LENGTH_SHORT)
        })
    }

    private fun setUpScheduling() {
        viewModel.scheduleTaskEvent.observe(viewLifecycleOwner, EventObserver { schedule ->
            if (schedule) showDatePicker()
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scheduleButton -> {
                showDatePicker()
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()

        val picker = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->

                cal.set(year, month, day)
                viewModel.selectDay(cal.time)
                showTimePicker()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        picker.show()
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()

        val picker = TimePickerDialog(
            requireContext(),
            { _: TimePicker, hour: Int, minute: Int ->

                cal.set(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute
                )
                val time = Time(cal.timeInMillis)
                viewModel.selectTime(time)
                viewModel.saveNewTask()
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(requireContext())
        )

        picker.show()
    }
}