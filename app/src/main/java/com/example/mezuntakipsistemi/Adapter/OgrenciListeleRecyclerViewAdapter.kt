package com.example.mezuntakipsistemi.Adapter

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.KisiProfilleriActivity
import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profilim.view.*
import kotlinx.android.synthetic.main.tek_satir_ogrenci.view.*

class OgrenciListeleRecyclerViewAdapter (Context : AppCompatActivity, ogrenciler: ArrayList<EgitimBilgileriUser>) : RecyclerView.Adapter<OgrenciListeleRecyclerViewAdapter.OgrencilerHolder>() {

    var tumOgrenciler = ogrenciler
    var myContext = Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OgrenciListeleRecyclerViewAdapter.OgrencilerHolder {
        var inflater = LayoutInflater.from(p0.context)
        var tekSatirOgr=inflater.inflate(R.layout.tek_satir_ogrenci,p0,false)
        return OgrencilerHolder(tekSatirOgr)
    }

    override fun getItemCount(): Int {
        return tumOgrenciler.size
    }

    override fun onBindViewHolder(p0: OgrenciListeleRecyclerViewAdapter.OgrencilerHolder, p1: Int) {
        var oAnkiOgr=tumOgrenciler.get(p1)
        p0.setData(oAnkiOgr,p1)
    }
    inner class OgrencilerHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!)
    {
            var layout = itemView as ConstraintLayout
            var isim = layout.tvOgrAdi
            var ogretimTuru = layout.tvOgretimTuruOgr
            var girisYili = layout.tvGirisYiliOGr
            var mezunYili = layout.tvMznYili

            //diploma notunu ogr id gibi kullandık. ismi de aynı şekilde calısma durumuyla
        fun setData(oAnkiOgr : EgitimBilgileriUser,position: Int)
        {
            layout.setOnClickListener {
                if(!oAnkiOgr.diplomaNotu.equals(FirebaseAuth.getInstance().currentUser?.uid))
                {
                    var intent= Intent(myContext,KisiProfilleriActivity::class.java)
                    intent.putExtra("user_id",oAnkiOgr.diplomaNotu)
                    myContext.startActivity(intent)
                }

            }
            Log.e("tag","tag "+getItemCount())
           isim.text=oAnkiOgr.calismaDurumu
            ogretimTuru.text=oAnkiOgr.ogretimTuru
            girisYili.text=oAnkiOgr.girisYili
            mezunYili.text=oAnkiOgr.mezuniyetYili
            }


        }



    }


