package com.example.gofit_10587

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PresensiMemberKelasAdapter(private var historys: List<com.example.gofit_10587.models.HistoryBookingKelas>, context: Context): RecyclerView.Adapter<PresensiMemberKelasAdapter.ViewHolder>() {
    private val context: Context

    companion object{
        private val STATUS = arrayOf(
            "Hadir",
            "Tidak Hadir",
        )
    }

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresensiMemberKelasAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_presensi_member_kelas_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresensiMemberKelasAdapter.ViewHolder, position: Int) {
        val data = historys[position]
        val preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)

        setExposedDropDownMenu(holder)

        holder.tvKodeBooking.text = "${data.KODE_BOOKING} - ${data.NAMA_KELAS}"
        holder.tvTanggalBook.text = "ID Member: ${data.TANGGAL_JADWAL_HARIAN}"
        holder.tvTanggalMelakukan.text = "Nama Member: ${data.TANGGAL_MELAKUKAN_BOOKING}"
        holder.tvStatusBooking.text = "${data.STATUS_PRESENSI_KELAS} - ${data.WAKTU_PRESENSI_KELAS}"
        if(holder.tvStatusBooking.text == "null - null"){
            holder.tvStatusBooking.text = "Belum dikonfirmasi/Tidak Hadir"
        }

        holder.iconCheck.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin konfirmasi presensi member ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya"){ _, _ ->
                    if (context is PresensiMemberKelas){
                        context.update(data.KODE_BOOKING,holder.edStatus.text.toString())
                    }
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKodeBooking: TextView
        var tvTanggalBook: TextView
        var tvTanggalMelakukan: TextView
        var tvStatusBooking: TextView
        var iconCheck: ImageButton
        var cvBook: CardView
        var edStatus: AutoCompleteTextView

        init {
            tvKodeBooking = view.findViewById(R.id.text_kode_presensi)
            tvTanggalBook = view.findViewById(R.id.text_tanggal_presensi)
            tvTanggalMelakukan = view.findViewById(R.id.text_tanggal_melakukan_presensi)
            tvStatusBooking = view.findViewById(R.id.text_status_konfirmasi_presensi)
            iconCheck = view.findViewById(R.id.icon_check)
            cvBook = view.findViewById(R.id.cv_book_presensi)
            edStatus = view.findViewById(R.id.ed_status)
        }

    }

    fun setExposedDropDownMenu(holder: PresensiMemberKelasAdapter.ViewHolder){
        val adapterslot: ArrayAdapter<String> = ArrayAdapter<String>(context as PresensiMemberKelas,
            R.layout.itemliststatusdropdown, PresensiMemberKelasAdapter.STATUS
        )
        holder.edStatus.setAdapter(adapterslot)
    }
}