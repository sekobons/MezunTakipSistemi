package com.example.mezuntakipsistemi.Adapter

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.example.mezuntakipsistemi.DataClass.OzelMesaj
import com.example.mezuntakipsistemi.MesajlarMesajYazActivity
import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tek_satir_mesajlar.view.*

class OzelMesajRecyclerViewAdapter(context : Context, ozelMesajOdalari:ArrayList<OzelMesaj>) : RecyclerView.Adapter<OzelMesajRecyclerViewAdapter.OzelMesajHolder>() {
    var tumOzelMesajOdalari = ozelMesajOdalari
    var dbRef = FirebaseDatabase.getInstance().reference
    var myActivity = context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OzelMesajRecyclerViewAdapter.OzelMesajHolder {
        var inflater = LayoutInflater.from(p0.context)
        var tekSatirOzelOda = inflater.inflate(R.layout.tek_satir_mesajlar, p0, false)
        return OzelMesajHolder(tekSatirOzelOda)
    }

    override fun getItemCount(): Int {
        return tumOzelMesajOdalari.size
    }

    override fun onBindViewHolder(p0: OzelMesajRecyclerViewAdapter.OzelMesajHolder, p1: Int) {
        var anlikOda = tumOzelMesajOdalari.get(p1)
        p0.setData(anlikOda, p1)

    }

    inner class OzelMesajHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var tumLayout = itemView as ConstraintLayout
        var foto = tumLayout.kisiPP
        var isim = tumLayout.tvKisiAdiMesaj
        var tarih = tumLayout.tvTarihAyrinti
        var mesaj  =tumLayout.tvMesajAyrinti

        fun setData(ozelOda: OzelMesaj, position: Int) {
            tumLayout.setOnClickListener {
                var intent= Intent(myActivity, MesajlarMesajYazActivity::class.java)
                intent.putExtra("konusulankisi_id",ozelOda.alici_ID)
                myActivity.startActivity(intent)
            }

            if(ozelOda.mesajlar!=null) {
                var index = ozelOda.mesajlar!!.size-1
                if(index!=-1)
                {
                    tarih.text=ozelOda.mesajlar!!.get(index).tarih

                    if(ozelOda.mesajlar!!.get(index).mesaj!!.length>20)
                    {
                        mesaj.text=ozelOda.mesajlar!!.get(index).mesaj!!.substring(0,19)+"..."

                    }

                    else
                        mesaj.text =ozelOda.mesajlar!!.get(index).mesaj
                }
                else
                    mesaj.text=ozelOda.mesajlar!!.get(0).mesaj


            }
            if(ozelOda.yazan_ID.equals(FirebaseAuth.getInstance().currentUser?.uid))
            {

                dbRef.child("kullanici").orderByKey().equalTo(ozelOda.alici_ID).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for(user in p0.children)
                            {
                                Log.e("key","key "+user.key)
                                for(kisiselBilgi in user.children)
                                {
                                    if(kisiselBilgi.key.equals("Kisisel_Bilgiler"))
                                    {
                                        Log.e("ozeloda","OZEL MESAJLAR   "+ozelOda.mesajlar.toString())
                                        var nesne=kisiselBilgi.getValue(KisiselBilgilerUser::class.java)
                                        if(!nesne?.profil_resmi.isNullOrEmpty())
                                            Picasso.get().load(nesne?.profil_resmi).into(foto)
                                        isim.text=nesne?.ad_soyad
                                        break
                                    }
                                }



                            }

                        }


                    }
                )
            }

            }
        }
    }
