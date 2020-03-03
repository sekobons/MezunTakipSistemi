package com.example.mezuntakipsistemi.Adapter

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.Ilanlar
import com.example.mezuntakipsistemi.IlanlarAyrintiActivity
import com.example.mezuntakipsistemi.IsilanlariActivity
import com.example.mezuntakipsistemi.R
import kotlinx.android.synthetic.main.tek_satir_ilanlar.view.*

class IlanlarRecyclerViewAdapter (Context : AppCompatActivity, ilanlar: ArrayList<Ilanlar>) : RecyclerView.Adapter<IlanlarRecyclerViewAdapter.ilanlarHolder>() {
    var tumIlanlar = ilanlar
    var myContext = Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): IlanlarRecyclerViewAdapter.ilanlarHolder {
        var inflater = LayoutInflater.from(p0.context)
        var tekSatirIlan=inflater.inflate(R.layout.tek_satir_ilanlar,p0,false)
        return ilanlarHolder(tekSatirIlan)

    }

    override fun getItemCount(): Int {
        return tumIlanlar.size

    }

    override fun onBindViewHolder(p0: IlanlarRecyclerViewAdapter.ilanlarHolder, p1: Int) {
        var oAnkiIlan=tumIlanlar.get(p1)
        p0.setData(oAnkiIlan,p1)

    }
    inner class ilanlarHolder(itemView  : View?) : RecyclerView.ViewHolder(itemView!!)
    {
        var layout = itemView as ConstraintLayout
        var firmaAd = layout.tvFirmailan
        var IsPozisyon = layout.tvIsPozisyonu
        var tarih = layout.tvIlanTarihi

        fun setData(anlikIlan : Ilanlar,position: Int)
        {
            firmaAd.text=anlikIlan.firma_adi
            IsPozisyon.text=anlikIlan.is_pozisyon
            tarih.text=anlikIlan.olusturulma_tarihi
            layout.setOnClickListener {
                var intent= Intent(myContext,IlanlarAyrintiActivity::class.java)
                intent.putExtra("ilan_id",anlikIlan.ilan_key)
                myContext.startActivity(intent)

            }
        }

    }
}