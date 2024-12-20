package com.dna.beyoureyes.ui.assign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dna.beyoureyes.AppUser
import com.dna.beyoureyes.MainActivity
import com.dna.beyoureyes.R
import com.dna.beyoureyes.model.Allergen
import com.dna.beyoureyes.model.Disease
import com.dna.beyoureyes.model.FirebaseHelper
import com.dna.beyoureyes.model.UserInfo
import com.dna.beyoureyes.ui.FragmentNavigationListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FieldValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignActivity : AppCompatActivity(), FragmentNavigationListener {
    private var name : String? = null
    private var gender : Int? = null
    private var birth : String? = null
    private var currentStep = 0
    private var disease : MutableSet<Disease> = mutableSetOf()
    // private var allergy : ArrayList<String> = ArrayList<String>()
    private var allergens : MutableSet<Allergen> = mutableSetOf()
    private var profile : String = ""

    override fun onNavigateToFragment(fragment: Fragment) {
        replaceFragment(fragment)
    }

    override fun onNameInputRecieved(name: String) {
        this.name = name
    }

    override fun onGenderInputRecieved(gender: Int) {
        this.gender = gender
    }

    override fun onBirthInputRecieved(birth: String) {
        this.birth = birth
    }

    override fun onDiseaseInputRecieved(userDiseaseSet: MutableSet<Disease>) {
        this.disease = userDiseaseSet
    }

    override fun onAllergyInputRecieved(userAllergySet: MutableSet<Allergen>) {
        this.allergens = userAllergySet
    }

    override fun onBackPressed() {
        currentStep--
        super.onBackPressed()
    }

    override fun onBtnClick(currentFragment: Fragment, isNxt : Boolean) {
        if (isNxt) {
            currentStep++
        }
        else {
            currentStep--
        }

        when(currentStep) {
            0 -> replaceFragment(AssignNameFragment())
            1 -> replaceFragment(AssignGenderFragment())
            2 -> replaceFragment(AssignBirthFragment())
            3 -> replaceFragment(AssignDiseaseFragment())
            4 -> replaceFragment(AssignAllergyFragment())
            5 -> {
                // currentStep이 5 이상일 경우 MainActivity로 전환
                registInfo()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()  // 현재 Activity 종료
            }
            else -> {
                finish()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign)

        replaceFragment(AssignNameFragment())
    }

    fun registInfo() {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val birthDate = dateFormat.parse(birth ?: "") ?: Date()
        val birthTimeStamp = Timestamp(birthDate)
        AppUser.info =
            UserInfo(name ?: "", gender ?: 0, birthTimeStamp, disease.ifEmpty { null }, allergens.ifEmpty { null })
        val userInfo = hashMapOf(
            "userId" to Firebase.auth.currentUser?.uid,
            "userName" to name!!,
            "userGender" to gender,
            "userBirth" to birthTimeStamp,
            "userProfile" to profile,
            "lastActivationDate" to FieldValue.serverTimestamp()
        )
        if (disease.isNotEmpty()) { // 질환 정보 전달 - enum명으로 DB 저장
            userInfo["userDisease"] = disease.map{ it.name }
        }
        if (allergens.isNotEmpty()) { // 알레르기 정보 전달 - enum명으로 DB 저장
            userInfo["userAllergens"] = allergens.map{ it.name }
        }
        FirebaseHelper.sendData(userInfo, "userInfo")
    }
}