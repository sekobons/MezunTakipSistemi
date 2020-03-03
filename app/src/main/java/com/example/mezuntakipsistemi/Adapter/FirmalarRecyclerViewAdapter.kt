package com.example.mezuntakipsistemi.Adapter

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.FirmaAyrintiActivity
import com.example.mezuntakipsistemi.R
import kotlinx.android.synthetic.main.tek_satir_firmalar.view.*

class FirmalarRecyclerViewAdapter(Context : AppCompatActivity, firmalar: ArrayList<Firmalar>) : RecyclerView.Adapter<FirmalarRecyclerViewAdapter.FirmalarHolder>() {

    var Firma = firmalar
    var myContext = Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FirmalarRecyclerViewAdapter.FirmalarHolder {
        var inflater = LayoutInflater.from(p0.context)
       var tekSatirFirma=inflater.inflate(R.layout.tek_satir_firmalar,p0,false)
       return FirmalarHolder(tekSatirFirma)
    }
    override fun onBindViewHolder(p0: FirmalarHolder, p1: Int) {
        var oAnkiFirma=Firma.get(p1)

        p0.setData(oAnkiFirma,p1)
    }

    override fun getItemCount(): Int {
        return Firma.size
    }


    inner class FirmalarHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!)
    {
            var layout = itemView as ConstraintLayout
            var firmaIsmi = layout.tvFirmaADi

        fun setData(oAnkiFirma : Firmalar,position: Int)
        {

                firmaIsmi.text=oAnkiFirma.firma_adi
            layout.setOnClickListener {

                var intent= Intent(myContext, FirmaAyrintiActivity::class.java)
                intent.putExtra("firma_id",oAnkiFirma.firma_id)
                myContext.startActivity(intent)


            }
             //   Log.e("tag","firma : "+firmaIsmi.text)


            }


        }



    }

