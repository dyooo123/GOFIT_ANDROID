package com.example.gofit_10587

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_10587.models.JadwalHarian
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddBookingKelasAdapter(private var historys: List<JadwalHarian>, context: Context):
    RecyclerView.Adapter<AddBookingKelasAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_jadwal_adapter, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]
        val preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)

        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvTanggalKelas.text = data.TANGGAL_JADWAL_HARIAN
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvKeteranganKelas.text = data.STATUS_JADWAL_HARIAN

        holder.tvHari.text = data.HARI_JADWAL_UMUM
        holder.tvTarif.text = "Rp.${data.HARGA_KELAS.toString()}"

        holder.cvKelas.setOnClickListener {
            if(holder.tvKeteranganKelas.text == "Libur") {
                Toast.makeText(context,"Kelas ditiadakan", Toast.LENGTH_SHORT).show()
            }else {
//                val i = Intent(context, BookingClassCreate::class.java)
//                i.putExtra("id_tanggal", data.TANGGAL_JADWAL_HARIAN)
//                i.putExtra("id_kelas",data.ID_KELAS)
//                if(context is BookingClassActivity){
//                    ContextCompat.startActivity(context,i,null)
//                }
                if(!(preferences.getString("booking",null).isNullOrEmpty())){
                    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                    materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin booking kelas ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Iya"){ _, _ ->
                            if (context is AddBookingKelas){
                                context.getSharedPreferences("login",Context.MODE_PRIVATE).getString("id",null)
                                    ?.let { it1 -> context.bookingClass(it1,data.ID_KELAS,data.TANGGAL_JADWAL_HARIAN) }
                            }
                        }
                        .show()
                }else if((preferences.getString("status",null).isNullOrEmpty())){
                    Toast.makeText(context,"Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }else{
//                    Toast.makeText(context,"Silahkan pilih menu booking", Toast.LENGTH_SHORT).show()
                }
            }
//            preferences.edit()
//                .putString("booking",null)
//                .apply()
        }
    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeteranganKelas: TextView
        var tvTanggalKelas: TextView
        var tvHari: TextView
        var tvTarif: TextView
        var cvKelas: CardView

        init {
            tvKelas = view.findViewById(R.id.tv_NamaKelas)
            tvInstruktur = view.findViewById(R.id.tv_NamaInstruktur)
            tvKeteranganKelas = view.findViewById(R.id.tv_KeteranganJadwalHarian)
            tvTanggalKelas = view.findViewById(R.id.tv_tanggalHarian)
            tvHari = view.findViewById(R.id.tv_HariJadwal)
            tvTarif = view.findViewById(R.id.tv_Tarif)
            cvKelas = view.findViewById(R.id.cv_jadwal)
        }
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val data = historys[position]
//
//        holder.tvKodeBooking.text = "${data.KODE_BOOKING_KELAS}  | Kelas :  ${data.NAMA_KELAS}"
//        holder.tvNamaInstruktur.text = "Nama Instruktur :  ${data.NAMA_INSTRUKTUR}"
//        holder.tvTanggalBook.text = "Tanggal Kelas: ${data.TANGGAL_HARIAN}"
//        holder.tvTanggalMelakukan.text = "Tanggal Booking: ${data.TANGGAl_YANG_DIBOOKING_KELAS}"
//        holder.tvStatusBooking.text = "${data.STATUS_PRESENSI_KELAS} - ${data.WAKTU_PRESENSI}"
//        if(holder.tvStatusBooking.text == "null - null"){
//            holder.tvStatusBooking.text = "Status : Belum dikonfirmasi"
//        }
//
//        holder.iconDel.setOnClickListener {
//            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
//            materialAlertDialogBuilder.setTitle("Konfirmasi")
//                .setMessage("Apakah anda yakin ingin membatalkan booking kelas ini?")
//                .setNegativeButton("Batal", null)
//                .setPositiveButton("Iya"){ _, _ ->
//                    if (context is BookingKelasActivity){
//                        context.cancelBooking(data.KODE_BOOKING_KELAS)
//                    }
//                }
//                .show()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return historys.size
//    }
//
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        var tvKodeBooking: TextView
//        var tvNamaInstruktur: TextView
//        var tvTanggalBook: TextView
//        var tvTanggalMelakukan: TextView
//        var tvStatusBooking: TextView
//        var iconDel: ImageButton
//        var cvBook: CardView
//
//        init {
//            tvKodeBooking = view.findViewById(R.id.text_kode)
//            tvNamaInstruktur = view.findViewById(R.id.textNamaInstruktur)
//            tvTanggalBook = view.findViewById(R.id.text_tanggal)
//            tvTanggalMelakukan = view.findViewById(R.id.text_tanggal_melakukan)
//            tvStatusBooking = view.findViewById(R.id.text_status_konfirmasi)
//            iconDel = view.findViewById(R.id.icon_delete)
//            cvBook = view.findViewById(R.id.cv_book)
//        }
//
//    }

}