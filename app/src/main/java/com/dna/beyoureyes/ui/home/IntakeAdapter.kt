package com.dna.beyoureyes.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dna.beyoureyes.model.Nutrition

class IntakeAdapter(private val items: MutableList<Nutrition>,
                           private val age : Int, private val isMan : Boolean)
    : RecyclerView.Adapter<IntakeAdapter.NutritionViewHolder>() {

    init {
        sortItems()
    }

    // 뷰 홀더 생성
    class NutritionViewHolder(val bar: IntakeNutritionBar) : RecyclerView.ViewHolder(bar)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionViewHolder {
        val intakeBar = IntakeNutritionBar(parent.context, null)
        return NutritionViewHolder(intakeBar)
    }

    // 뷰 홀더 데이터 바인딩
    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val nutri = items[position]
        holder.bar.setData(nutri, age, isMan)
    }

    override fun getItemCount() = items.size

    // 데이터 업데이트 시 정렬
    fun updateItems(newItems: List<Nutrition>) {
        sortItems(newItems)
        notifyDataSetChanged()
    }

    // 데이터 정렬
    private fun sortItems(newItems: List<Nutrition> = items) {
        // 경고 대상(상위에 표시)
        val itemsToWarn = newItems
            .filter { it.isInWarningRange(age, isMan) }
            .sortedByDescending {
                // 권장량 대비 섭취량 비율로 내림차순 정렬
                (it.milligram.toFloat() / it.getDailyValue(age, isMan).toFloat()) }
        // 나머지
        val remaining = newItems
            .filter { !it.isInWarningRange(age, isMan) }
            .sortedByDescending {
                // 권장량 대비 섭취량 비율로 내림차순 정렬
                (it.milligram.toFloat() / it.getDailyValue(age, isMan).toFloat()) }
        items.clear()
        items.addAll(itemsToWarn)
        items.addAll(remaining)
    }
}