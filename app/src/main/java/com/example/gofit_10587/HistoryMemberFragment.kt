package com.example.gofit_10587

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.toolbox.Volley
import com.example.gofit_10587.databinding.FragmentHistoryMemberBinding
import com.example.gofit_10587.databinding.FragmentMemberHomeBinding


class HistoryMemberFragment : Fragment() {
    private var _binding:  FragmentHistoryMemberBinding?= null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        binding.btnHistoryBookingGym.setOnClickListener {
            val moveBooking = Intent(activity, BookinGymActivity::class.java)
            startActivity(moveBooking)
        }

        binding.btnHistoryBookingKelas.setOnClickListener {
            val moveBookingKelas = Intent(activity, BookingKelasActivity::class.java)
            startActivity(moveBookingKelas)
        }

    }

}