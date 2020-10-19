package com.thetechannel.android.planit

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDayPieChart()
        setUpPerformanceLineChart()
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