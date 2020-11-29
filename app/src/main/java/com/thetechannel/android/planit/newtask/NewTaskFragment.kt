package com.thetechannel.android.planit.newtask

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.thetechannel.android.planit.*
import com.thetechannel.android.planit.broadcast.AppBroadcastReceiver
import com.thetechannel.android.planit.databinding.FragmentNewTaskBinding
import java.sql.Time
import java.util.*

private const val TAG = "NewTaskFragment"

class NewTaskFragment : Fragment(), View.OnClickListener {

    private val viewModel by viewModels<NewTaskViewModel> {
        NewTaskViewModelFactory((requireContext().applicationContext as MyApplication).repository)
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
        setUpReminder()
    }

    private fun setUpNavigation() {
        viewModel.taskAddedEvent.observe(viewLifecycleOwner, EventObserver { added ->
            if (added) {
                findNavController().navigateUp()
            }
        })
    }

    private fun setUpReminder() {
        viewModel.reminderTimeMillis.observe(viewLifecycleOwner, EventObserver {
            setUpReminderBroadcast(it)
        })
    }

    private fun setUpReminderBroadcast(millis: Long) {
        Log.d(TAG, "broadcast sent")
        val intent = Intent(requireContext(), AppBroadcastReceiver::class.java)
        intent.putExtra(
            AppBroadcastReceiver.BROADCAST_NAME_KEY,
            AppBroadcastReceiver.REMINDER_BROADCAST_EXTRA
        )
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)

        val alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            millis,
            pendingIntent
        )
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