package com.example.gofit_10587

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_10587.api.PresensiInstrukturApi
import com.example.gofit_10587.databinding.ActivityPresensiInstrukturBinding
import com.example.gofit_10587.models.InstrukturPresensi
import com.example.gofit_10587.models.PresensiInstruktur
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PresensiInstrukturActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPresensiInstrukturBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresensiInstrukturBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        queue = Volley.newRequestQueue(this)

        binding.srPresensi.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{allData()})
        allData()

        binding.back.setOnClickListener{
            val move = Intent(this@PresensiInstrukturActivity, HomeActivity::class.java)
            startActivity(move)
        }
    }

    private fun allData() {
        binding.srPresensi.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresensiInstrukturApi.GET_ALL_URL, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<PresensiInstruktur>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = PresensiInstruktur(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL_UMUM"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("HARGA_KELAS"),
                    )
                    schedule.add(data)
                }
                var data_array: Array<PresensiInstruktur> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : PresensiInstrukturAdapter = PresensiInstrukturAdapter(schedule,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_presensi)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srPresensi.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@ScheduleInstructorActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()

                }else {

                }

            }, Response.ErrorListener { error ->
                binding.srPresensi.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@PresensiInstrukturActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()

                } catch (e: Exception){
                    Toast.makeText(this@PresensiInstrukturActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun store(id_instruktur: Int, tanggal_mengajar: String){
        val presence = InstrukturPresensi(
            id_instruktur,
            tanggal_mengajar
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, PresensiInstrukturApi.STORE_DATA, Response.Listener { response ->
                val gson = Gson()
                var presence_data = gson.fromJson(response, PresensiInstruktur::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("data")

                if(presence_data!= null) {
                    resJO.getString("message")
                    FancyToast.makeText(this@PresensiInstrukturActivity, "Berhasil Mengupdate Jam Kelas!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    val intent = Intent(this@PresensiInstrukturActivity, PresensiInstrukturActivity::class.java)
                    startActivity(intent)
                }
                else {
                    FancyToast.makeText(this@PresensiInstrukturActivity, "Gagal Update Jam Kelas!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)

                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@PresensiInstrukturActivity, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(presence)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}