package com.dna.beyoureyes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.dna.beyoureyes.databinding.FragmentResultNutriBarChartBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarEntry

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultNutriBarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultNutriBarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding : FragmentResultNutriBarChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultNutriBarChartBinding.inflate(inflater, container, false)
        val naBarChart : HorizontalBarChart = binding.naBarchart
        val na : TextView = binding.nutriBarResultNa
        val testNaBarChart = NutriIntakeBarDisplay(naBarChart, na)
        val testcarbBarChart = NutriIntakeBarDisplay(binding.carbBarchart, binding.nutriBarResultCarb)

        val testBarChart = NaIntakeBarDisplay(testNaBarChart)
        val intakeRangeMap = mapOf(
            IntakeRange.LACK to IntProgression.fromClosedRange(0, 500, 1),
            IntakeRange.LESS to IntProgression.fromClosedRange(501, 1000, 1),
            IntakeRange.ENOUGH to IntProgression.fromClosedRange(1001, 2000, 1),
            IntakeRange.OVER to IntProgression.fromClosedRange(2001, 5000, 1)
        )
        val testDailyValue = DailyValueImpl(
            unit = UnitOfMass.MILLIGRAM,
            dailyValue = 2300, // 원하는 값을 여기에 설정합니다.
            intakeRange = intakeRangeMap
        )
        testNaBarChart.setBarValue(requireContext(), Nutrition(600, UnitOfMass.MILLIGRAM), testDailyValue)
        testcarbBarChart.setBarValue(requireContext(), Nutrition(1200, UnitOfMass.GRAM), testDailyValue)
        Log.d("BAR CHART : ", "now processing")
        val energyIntake = 0
        val userDVs = NutrientDailyValues()
        var totalIntake = NutritionFacts()
        // 2.4. 총 섭취량 화면 표시 - 성분별 섭취량 바
        //testBarChart.setAll(requireContext(), totalIntake, userDVs)
        //Log.d("BAR: ", "setAllFinished")

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultNutriBarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultNutriBarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class DailyValueImpl(
    override val unit: UnitOfMass,
    override var dailyValue: Int,
    override var intakeRange: Map<IntakeRange, IntProgression>?
) : DailyValue {
}