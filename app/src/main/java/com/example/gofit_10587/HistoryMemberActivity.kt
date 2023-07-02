package com.example.gofit_10587

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gofit_10587.databinding.ActivityHistoryMemberBinding
import com.example.gofit_10587.databinding.ActivityMainBinding
import com.example.gofit_10587.models.umumActivity

class HistoryMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryMemberBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        super.onCreate(savedInstanceState)

//        binding.btnHistoryBookingGym.setOnClickListener(View.OnClickListener {
//            val moveBooking = Intent(this@HistoryMemberActivity, BookinGymActivity::class.java)
//            startActivity(moveBooking)
//        })
//
//        binding.btnHistoryBookingKelas.setOnClickListener(View.OnClickListener  {
//            val moveDaftar = Intent(this@HistoryMemberActivity, BookingKelasActivity::class.java)
//            startActivity(moveDaftar)
//        })

        binding.btnHistoryBookingGym.setOnClickListener {
            val moveBooking = Intent(this@HistoryMemberActivity, HistoryBookingGym::class.java)
            startActivity(moveBooking)
        }

        binding.btnHistoryBookingKelas.setOnClickListener {
            val moveBookingKelas = Intent(this@HistoryMemberActivity, HistoryBookingKelas::class.java)
            startActivity(moveBookingKelas)
        }
    }


}