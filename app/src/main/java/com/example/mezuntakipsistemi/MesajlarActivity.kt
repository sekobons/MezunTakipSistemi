package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.mezuntakipsistemi.Adapter.OzelMesajRecyclerViewAdapter
import com.example.mezuntakipsistemi.DataClass.Mesajlar
import com.example.mezuntakipsistemi.DataClass.OzelMesaj
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_mesajlar.*


class MesajlarActivity : AppCompatActivity() {
            var dbRef : DatabaseReference ? =null
            var tumMesajOdalari : ArrayList<OzelMesaj> ? =null
            var set : HashSet<String> ? =null
            var mesajSet : HashSet<String> ? =null
            var myAdapter : OzelMesajRecyclerViewAdapter ? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesajlar)
        MesajListeleriniGetir()
    }

    private fun MesajListeleriniGetir() {
                if(tumMesajOdalari==null)
                {
                    tumMesajOdalari=ArrayList()
                    set= HashSet()
                    mesajSet= HashSet()
                }
        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("özel_mesaj").orderByKey().equalTo(FirebaseAuth.getInstance().currentUser?.uid!!).addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                            for(ozelMesaj in p0.children)
                            {
                                var key=ozelMesaj.key
                                if(!set!!.contains(key))
                                {
                                    set!!.add(key!!)
                                    ozelMesaj.children.forEach {
                                        var x =it.child("sohbet_mesajlari").children
                                        var ozelOda =OzelMesaj()
                                        ozelOda.yazan_ID=FirebaseAuth.getInstance().currentUser?.uid
                                        ozelOda.alici_ID=it.key

                                        var mesajlar=ArrayList<Mesajlar>()
                                        for(Mesaj in x)
                                        {
                                        var mesajlarNesne=Mesajlar()
                                                mesajlarNesne.kullanici_id=Mesaj.getValue(Mesajlar::class.java)?.kullanici_id
                                                 mesajlarNesne.mesaj=Mesaj.getValue(Mesajlar::class.java)?.mesaj
                                                 mesajlarNesne.tarih=Mesaj.getValue(Mesajlar::class.java)?.tarih
                                                 mesajlar.add(mesajlarNesne)
                                            Log.e("yazan - alici "," "+mesajlarNesne.mesaj  + " " )

                                        }
                                        ozelOda.mesajlar=mesajlar
                                        tumMesajOdalari?.add(ozelOda)
                                        ozelMesajlariListele()
                                    }


                                }


                            }
                }


            }
        )

    }

    private fun ozelMesajlariListele() {
        for(i in 0..tumMesajOdalari!!.size-1)
        {
            Log.e("eleman","el : "+tumMesajOdalari?.get(i)?.yazan_ID)
        }
        myAdapter= OzelMesajRecyclerViewAdapter(this@MesajlarActivity,tumMesajOdalari!!)
        mesajlarRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mesajlarRecy.layoutManager=linearLayoutManager
    }
}
