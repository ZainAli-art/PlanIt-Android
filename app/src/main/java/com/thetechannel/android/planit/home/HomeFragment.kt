package com.thetechannel.android.planit.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.thetechannel.android.planit.EventObserver
import com.thetechannel.android.planit.MyApplication
import com.thetechannel.android.planit.TaskFilterType
import com.thetechannel.android.planit.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory((requireActivity().applicationContext as MyApplication).repository)
    }

    private lateinit var viewDataBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDayPieChart()
        setUpPerformanceLineChart()
        setUpNavigation()
    }

    private fun setUpNavigation() {
        viewModel.openTasksEvent.observe(viewLifecycleOwner, EventObserver {
            openTasks(it)
        })
        viewModel.addNewTaskEvent.observe(viewLifecycleOwner, EventObserver { newTask ->
            if (newTask) {
                val action = HomeFragmentDirections.actionHomeFragmentToNewTaskFragment()
                findNavController().navigate(action)
            }
        })
    }

    private fun openTasks(filterType: TaskFilterType) {
        val action = HomeFragmentDirections.actionHomeFragmentToTasksFragment(filterType)
        findNavController().navigate(action)
    }

    private fun setUpDayPieChart() {
        val visitors = listOf(
            PieEntry(3f, "Business"),
            PieEntry(2f, "Study"),
            PieEntry(1f, "Sport")
        )

        val dataSet = PieDataSet(visitors, "Tasks")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 16f

        val pieData = PieData(dataSet)

        dayPieChart.data = pieData
        dayPieChart.description.isEnabled = false
        dayPieChart.centerText = "Tasks"
        dayPieChart.animateY(2000)
    }

    private fun setUpPerformanceLineChart() {
        val entries = listOf(
            Entry(0f, 60f),
            Entry(1f, 50f),
            Entry(2f, 70f),
            Entry(3f, 30f),
            Entry(4f, 50f),
            Entry(5f, 60f),
            Entry(6f, 65f)
        )

        val dataSet = LineDataSet(entries, "Progress").apply {
            color = Color.BLACK
            valueTextColor = Color.GRAY
            lineWidth = 1.5f
        }

        performanceLineChart.apply {
            description.isEnabled = false
            axisLeft.isEnabled = false
            data = LineData(dataSet)
            invalidate()
        }
    }
}