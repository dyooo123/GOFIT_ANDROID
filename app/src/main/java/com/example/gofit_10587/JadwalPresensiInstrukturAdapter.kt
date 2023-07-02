package com.example.gofit_10587

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_10587.models.JadwalInstruktur

class JadwalPresensiInstrukturAdapter(private var instructors: List<JadwalInstruktur>, context: Context): RecyclerView.Adapter<JadwalPresensiInstrukturAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalPresensiInstrukturAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_jadwal_presensi_instruktur_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalPresensiInstrukturAdapter.ViewHolder, position: Int) {
        val data = instructors[position]
        val preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)
        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvTanggal.text = data.TANGGAL_JADWAL_HARIAN
        holder.tvHari.text = data.HARI_JADWAL_UMUM
        holder.tvKeterangan.text = data.STATUS_JADWAL_HARIAN

        holder.cvSchedule.setOnClickListener {
            if (context is JadwalPresensiInstrukturActivity){
                val intent = Intent(context, PresensiMemberKelas::class.java)
                preferences.edit()
                    .putString("tanggal_jadwal_harian",data.TANGGAL_JADWAL_HARIAN)
                    .apply()
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return instructors.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeterangan: TextView
        var tvHari: TextView
        var tvTanggal: TextView
        var cvSchedule: CardView


        init {
            tvKelas = view.findViewById(R.id.text_kelas_instructor_presensi)
            tvInstruktur = view.findViewById(R.id.text_instruktur_instructor_presensi)
            tvKeterangan = view.findViewById(R.id.text_keterangan_kelas_instructor_presensi)
            tvHari = view.findViewById(R.id.tv_hari_instructor_presensi)
            tvTanggal = view.findViewById(R.id.text_tanggal_kelas_instructor_presensi)
            cvSchedule = view.findViewById(R.id.cv_schedule_instructor_presensi)
        }

    }
}