package com.dna.beyoureyes.ui.assign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dna.beyoureyes.R
import com.dna.beyoureyes.databinding.FragmentAssignBirthBinding
import java.util.Calendar

class AssignBirthFragment: Fragment() {
    private var _binding: FragmentAssignBirthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentAssignBirthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // number picker 바인딩
        val year : NumberPicker = binding.yearPicker
        val month : NumberPicker = binding.monthPicker
        val day : NumberPicker = binding.dayPicker

        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH) + 1

        // 스크롤로 순환할 수 있는 기능 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false
        day.wrapSelectorWheel = false

        // 년도 범위 설정: 100년 전 ~ 현재
        year.minValue = 0
        year.maxValue = 99

        // 월 범위 설정: 1월 ~ 12월
        month.minValue = 1
        month.maxValue = 12

        // 년도를 역순으로 표시
        val yearsDesc = ((currentYear-100)..currentYear).map { it.toString() }.reversed().toTypedArray()
        year.displayedValues = yearsDesc

        // number picker에 생월을 표시할 때 'x월'이라는 형식으로 표시
        month.displayedValues = getDisplayValues(1, 12, getString(R.string.assign_birth_month))

        // 일 범위 설정: 1일 부터 시작하며, 년도와 월마다 최대 일수가 다름
        day.minValue = 1
        day.maxValue = getDaysInMonth(currentYear, currentMonth)
        // 입력 년도가 넘어가며 월이 바뀔 때 최대 일수 변경되어야 함
        year.setOnValueChangedListener { _, _, input ->
            val maxDayValue = getDaysInMonth(input, month.value)
            day.maxValue = maxDayValue
        }
        // 입력 생월이 바뀔 때 최대 일수 변경되어야 함
        month.setOnValueChangedListener { _, _, input ->
            val maxDayValue = getDaysInMonth(year.value, input)
            day.maxValue = maxDayValue
        }

        // 초기값 설정
        year.value = 0
        month.value = 1
        day.value = 1

        // 다음으로 버튼
        binding.nextBtn.setOnClickListener {
            Toast.makeText(context,
                "입력값은 ${yearsDesc[year.value].toInt()}년 ${month.value}월 ${day.value}일",
                Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 특정 년도, 특정 월의 날짜 수 구하기
    private fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun getDisplayValues(start: Int, end: Int, suffix: String): Array<String> {
        val displayValues = mutableListOf<String>()

        for (value in start..end) {
            displayValues.add("${value}${suffix}")
        }
        return displayValues.toTypedArray()
    }

}