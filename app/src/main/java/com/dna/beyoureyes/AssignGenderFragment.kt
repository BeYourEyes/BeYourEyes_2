package com.dna.beyoureyes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dna.beyoureyes.databinding.FragmentAssignGenderBinding
import com.dna.beyoureyes.databinding.FragmentAssignNameBinding
import com.dna.beyoureyes.ui.CustomToolbar
import com.dna.beyoureyes.ui.FragmentNavigationListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AssignGenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssignGenderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAssignGenderBinding

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
        binding = FragmentAssignGenderBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val listener = activity as? FragmentNavigationListener
        binding.nextBtn.setOnClickListener {
            val gender = getUserGender()
            if (gender != -1) {
                listener?.onGenderInputRecieved(gender)
                listener?.onBtnClick(this, true)
            }
            else {
                Toast.makeText(requireContext(), "성별을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
        }

        binding.toolbar.backButtonClickListener = object : CustomToolbar.ButtonClickListener {
            override fun onClicked() {
                listener?.onBtnClick(this@AssignGenderFragment, false)
            }
        }

        binding.chipFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.chipMale.isChecked = false
            }
        }
        binding.chipMale.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                binding.chipFemale.isChecked = false
            }

        }

        return binding.root
    }

    private fun getUserGender(): Int {
        if (binding.chipFemale.isChecked) return 0
        else if(binding.chipMale.isChecked) return 1
        else return -1
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AssignGenderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssignGenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}