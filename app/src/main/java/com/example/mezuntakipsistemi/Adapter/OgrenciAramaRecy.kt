package com.example.mezuntakipsistemi.Adapter

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.example.mezuntakipsistemi.KisiProfilleriActivity
import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tek_satir_ogrenci_arama.view.*

class OgrenciAramaRecy(Context : AppCompatActivity, ogrenciler: ArrayList<KisiselBilgilerUser>) : RecyclerView.Adapter<OgrenciAramaRecy.OgrenciAramaHolder>() {
        var myContext = Context
        var arananKisi=ogrenciler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OgrenciAramaRecy.OgrenciAramaHolder {
        var inflater = LayoutInflater.from(p0.context)
        var tekSatirOgr=inflater.inflate(R.layout.tek_satir_ogrenci_arama,p0,false)
        return OgrenciAramaHolder(tekSatirOgr)
    }

    override fun getItemCount(): Int {
        return arananKisi.size
    }

    override fun onBindViewHolder(p0: OgrenciAramaRecy.OgrenciAramaHolder, p1: Int) {
        var oAnkiOgr=arananKisi.get(p1)
        p0.setData(oAnkiOgr,p1)

    }
    inner class OgrenciAramaHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!)
    {
        var layout = itemView as ConstraintLayout
        var ad_soyad =layout.tvadSoyad
        var foto = layout.imgAramaPP
        fun setData(oAnkiOgr : KisiselBilgilerUser,position: Int)
        {
            layout.setOnClickListener {
                if(!oAnkiOgr.user_id.equals(FirebaseAuth.getInstance().currentUser?.uid))
                {
                    var id =oAnkiOgr.user_id
                    var intent= Intent(myContext,KisiProfilleriActivity::class.java)
                    intent.putExtra("user_id",id)
                    myContext.startActivity(intent)
                }


            }
                ad_soyad.text=oAnkiOgr.ad_soyad
                if(oAnkiOgr.profil_resmi!="")
                {
                        Picasso.get().load(oAnkiOgr.profil_resmi).into(foto)
                }
        }


    }
}