package com.example.mezuntakipsistemi.Adapter

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.KisiProfilleriActivity
import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.tek_satir_calisanlar.view.*
import java.util.ArrayList

class FirmadakiCalisanlarAdapter(Context : AppCompatActivity, firmalarim: ArrayList<Firmalar>) : RecyclerView.Adapter<FirmadakiCalisanlarAdapter.CalisanlarHolder>() {
    var Calisanlar = firmalarim
    var myContext = Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FirmadakiCalisanlarAdapter.CalisanlarHolder {
        var inflater = LayoutInflater.from(p0.context)
        var tekSatirFirma=inflater.inflate(R.layout.tek_satir_calisanlar,p0,false)
        return CalisanlarHolder(tekSatirFirma)
    }

    override fun getItemCount(): Int {
        return Calisanlar.size
    }

    override fun onBindViewHolder(p0: FirmadakiCalisanlarAdapter.CalisanlarHolder, p1: Int) {
        var oAnkiKisi=Calisanlar.get(p1)
        p0.setData(oAnkiKisi,p1)
    }
    inner class CalisanlarHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!)
    {
       var layout = itemView as ConstraintLayout
        var isim = layout.tvCalisanIsım
        var sure = layout.tvCalisanSure
        var yetenek = layout.tvCalisanYetenek
        var calisiyormu =layout.tvCalisanCalisiyormu
        fun setData(oAnkiKisi : Firmalar, position: Int)
        {

            isim.text=oAnkiKisi.firma_telNo
            sure.text=oAnkiKisi.calisma_suresi
            yetenek.text=oAnkiKisi.yetenek
            calisiyormu.text=oAnkiKisi.calisiyor_mu

            layout.setOnClickListener {
                var id = oAnkiKisi.firma_adres // adreste kisi id tutuluyor
                if(!id.equals(FirebaseAuth.getInstance().currentUser?.uid))
                {
                    var intent= Intent(myContext, KisiProfilleriActivity::class.java)
                    intent.putExtra("user_id",id)
                    myContext.startActivity(intent)
                }

            }

        }



    }
}