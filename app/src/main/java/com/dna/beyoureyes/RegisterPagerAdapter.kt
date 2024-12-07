package com.dna.beyoureyes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class RegisterPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> fragment_onboarding.newInstance(1)
            1 -> fragment_onboarding.newInstance(2)
            else -> fragment_onboarding.newInstance(3)
        }
    }

    override fun getCount() =  3
}