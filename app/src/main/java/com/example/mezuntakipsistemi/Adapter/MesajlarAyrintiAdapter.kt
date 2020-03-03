package com.example.mezuntakipsistemi.Adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.Mesajlar
import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.tek_satir_ozel_mesaj2.view.*

class MesajlarAyrintiAdapter(context : Context, MesajHepsi:ArrayList<Mesajlar>) : RecyclerView.Adapter<MesajlarAyrintiAdapter.AyrintiHolder>() {
    var myContext=context
    var tumMesajlar=MesajHepsi
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MesajlarAyrintiAdapter.AyrintiHolder {
        var inflater= LayoutInflater.from(myContext)
        var view : View? = null
        if(p1==1)
        {
            view=inflater.inflate(R.layout.tek_satir_ozel_mesaj,p0,false)
        }
        else
            view=inflater.inflate(R.layout.tek_satir_ozel_mesaj2,p0,false)
        return AyrintiHolder(view,p1)


    }
    override fun getItemViewType(position: Int): Int {
        if(tumMesajlar.get(position).kullanici_id.equals(FirebaseAuth.getInstance().currentUser?.uid))
        {
            return 1
        }
        else
            return 2
    }

    override fun getItemCount(): Int {
        return tumMesajlar.size
    }

    override fun onBindViewHolder(p0: MesajlarAyrintiAdapter.AyrintiHolder, p1: Int) {
        var oankiMesaj=tumMesajlar.get(p1)
        p0.setData(oankiMesaj,p1)
    }
    inner class AyrintiHolder(itemView : View, p1 : Int) : RecyclerView.ViewHolder(itemView)
    {
        var  tumLayout=(itemView as ConstraintLayout)
        var mesaj =tumLayout.tvMesajAyrinti
        var tarih = tumLayout.tvTarihAyrinti

        fun setData(anlikMesaj : Mesajlar,p1 : Int)
        {
            mesaj.text=anlikMesaj.mesaj
            tarih.text=anlikMesaj.tarih

        }


    }
}