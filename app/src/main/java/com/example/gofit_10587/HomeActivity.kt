package com.example.gofit_10587

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.gofit_10587.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setContentView(R.layout.activity_home)
//
//        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
//
//        var role = sharedPreferences.getString("role",null)

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("role", null)

//      var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)

//        val homeFragment = HomeMemberFragment()
//


        if(role == "member"){
            setThatFragments(MemberHomeFragment())
            binding.navView.setOnItemSelectedListener {
                when(it){
                    R.id.nav_books ->{
                        setThatFragments(MemberHomeFragment())
                    }
                    R.id.nav_profile ->{
                        setThatFragments(ProfileMemberFragment())
                    }
                }
                true
            }
        }

        if(role == "Manajer Operasional"){
            setThatFragments(ManajerOperasionalHomeFragment())
            binding.navView.setOnItemSelectedListener {
                when(it){
                    R.id.nav_books ->{
                        setThatFragments(ManajerOperasionalHomeFragment())
                    }
                }
                true
            }
        }

        if(role == "instruktur"){
            setThatFragments(InstrukturHomeFragment())
            binding.navView.setOnItemSelectedListener {
                when(it){
                    R.id.nav_books ->{
                        setThatFragments(InstrukturHomeFragment())
                    }
                    R.id.nav_profile ->{
                        setThatFragments(ProfileInstrukturFragment())
                    }

                }
                true
            }
        }



    }



    private fun setThatFragments(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layout_fragments,fragment)
            commit()
        }
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}