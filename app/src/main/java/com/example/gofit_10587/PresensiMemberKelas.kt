package com.example.gofit_10587

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_10587.api.PresensiBookingKelasApi
import com.example.gofit_10587.databinding.ActivityPresensiMemberKelasBinding
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class PresensiMemberKelas : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPresensiMemberKelasBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresensiMemberKelasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        val tanggal = sharedPreferences.getString("tanggal_jadwal_harian",null)
        queue = Volley.newRequestQueue(this)

        binding.srPresensiBooking.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
            if (tanggal != null) {
                allData(tanggal)
            }
        })
        if (tanggal != null) {
            allData(tanggal)
        }
    }
    private fun allData(id: String) {
        binding.srPresensiBooking.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresensiBookingKelasApi.GETDATAMEMBER + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var history = arrayListOf<com.example.gofit_10587.models.HistoryBookingKelas>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = com.example.gofit_10587.models.HistoryBookingKelas(
                        jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("WAKTU_PRESENSI_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_PRESENSI_KELAS")
                    )
                    history.add(data)
                }
                var data_array: Array<com.example.gofit_10587.models.HistoryBookingKelas> = history.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : PresensiMemberKelasAdapter = PresensiMemberKelasAdapter(history,this)
                val rvPermission : RecyclerView = findViewById(R.id.rvPresensiBooking)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srPresensiBooking.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this, "Notification Display!",
                        "Succesfully get data",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }else {
                    MotionToast.darkToast(
                        this, "Notification Display!",
                        "Data not found",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }

            }, Response.ErrorListener { error ->
                binding.srPresensiBooking.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srPresensiBooking.isRefreshing = false
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srPresensiBooking.isRefreshing = false
                }

            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }



    fun update(kode_booking: String, status: String){
        val booking = com.example.gofit_10587.models.HistoryBookingKelas(
            kode_booking,
            null,
            null,
            null,
            null,
            status,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, PresensiBookingKelasApi.UPDATEPRESENSI, Response.Listener { response ->
                val gson = Gson()
                var booking_data = gson.fromJson(response, com.example.gofit_10587.models.HistoryBookingKelas::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("data")

                if(booking_data!= null) {
                    MotionToast.darkColorToast(this,"Notification Booking!",
                        resJO.getString("message"),
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@PresensiMemberKelas, JadwalPresensiInstrukturActivity::class.java)
                    sharedPreferences.edit()
                        .putString("tanggal_jadwal_harian",null)
                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Booking!",
                        "Failed Confirm Booking Class",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,"Notification Booking!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@PresensiMemberKelas, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(booking)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}