package com.example.mezuntakipsistemi.Adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.R
import kotlinx.android.synthetic.main.tek_satir_deneyimler.view.*
import java.util.ArrayList

class DeneyimlerRecyAdapter(Context : AppCompatActivity, firmalarim: ArrayList<Firmalar>) : RecyclerView.Adapter<DeneyimlerRecyAdapter.DeneyimlerHolder>() {
    var myDeneyim = firmalarim
    var myContext = Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DeneyimlerRecyAdapter.DeneyimlerHolder {
        var inflater = LayoutInflater.from(p0.context)
        var tekSatirFirma=inflater.inflate(R.layout.tek_satir_deneyimler,p0,false)
        return DeneyimlerHolder(tekSatirFirma)
    }

    override fun getItemCount(): Int {
        return myDeneyim.size
    }

    override fun onBindViewHolder(p0: DeneyimlerRecyAdapter.DeneyimlerHolder, p1: Int) {
        var oAnkiFirma=myDeneyim.get(p1)
        p0.setData(oAnkiFirma,p1)
    }
    inner class DeneyimlerHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!)
    {
        var tekSatirLayout=itemView as ConstraintLayout
        var firmaAdi = tekSatirLayout.tvFirmaAdiDeneyim
        var sure = tekSatirLayout.tvSureDeneyim
        var pozisyon = tekSatirLayout.tvPozisyonDeneyim
        var yetenek = tekSatirLayout.tvYetenekDeneyim

        fun setData(oAnkiFirma : Firmalar,position: Int)
        {
            firmaAdi.text=oAnkiFirma.firma_adi

            sure.text=oAnkiFirma.baslangicTarih + " " +oAnkiFirma.bitisTarih + " "+oAnkiFirma.calisma_suresi
            pozisyon.text=oAnkiFirma.pozisyon
            yetenek.text=oAnkiFirma.yetenek

        }



    }
}