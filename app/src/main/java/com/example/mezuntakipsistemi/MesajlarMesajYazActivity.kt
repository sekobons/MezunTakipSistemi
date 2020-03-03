package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.mezuntakipsistemi.Adapter.MesajlarAyrintiAdapter
import com.example.mezuntakipsistemi.DataClass.Mesajlar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_mesajlar_mesaj_yaz.*
import java.text.SimpleDateFormat
import java.util.*

class MesajlarMesajYazActivity : AppCompatActivity() {
        var konusulanID : String ? =null
        var dbRef : DatabaseReference ? =null
        var mesajID : HashSet<String>  ?=null
        var tumMesajlar : ArrayList<Mesajlar >? =null
        var myAdapter : MesajlarAyrintiAdapter ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesajlar_mesaj_yaz)
        odayiOgren()
        Finit()
    }

    private fun Finit() {
                imgMesajGonder.setOnClickListener {
                    if(!etMesajYaz.text.isNullOrEmpty())
                    {


                        var nesne= Mesajlar()
                        nesne.mesaj=etMesajYaz.text.toString()
                        nesne.kullanici_id=FirebaseAuth.getInstance().currentUser?.uid
                        nesne.tarih=getMesajTarihi()
                        var mesajKey=dbRef!!.push().key
                        var ref=FirebaseDatabase.getInstance().reference
                        ref.child("özel_mesaj")?.child(FirebaseAuth.getInstance().currentUser?.uid!!)!!
                            .child(konusulanID!!).child("sohbet_mesajlari").child(mesajKey!!).setValue(nesne)
                        ref.child("özel_mesaj").child(konusulanID!!).child(FirebaseAuth.getInstance().currentUser?.uid!!)
                            .child("sohbet_mesajlari").child(mesajKey!!).setValue(nesne)


                        etMesajYaz.setText("")
                    }
                }
        etMesajYaz.setOnClickListener {
            if(myAdapter!=null)
                mesajlarAyrintiRecy.smoothScrollToPosition(myAdapter!!.itemCount-1) // mesaj yazına tıklayınca yavaşca kayması
        }

    }

    private fun odayiOgren() {
        konusulanID=intent.getStringExtra("konusulankisi_id")

        baslatListener()
    }
    var mValueEventListener: ValueEventListener =object : ValueEventListener
    {
        override fun onCancelled(p0: DatabaseError) {
            Log.e("asdsad","hataaa")
        }

        override fun onDataChange(p0: DataSnapshot) {
            Log.e("asdsad","tetik")
            // myAdapter?.notifyDataSetChanged()

            OzelOdaMesajlariniGetir()

        }


    }

    private fun OzelOdaMesajlariniGetir() {
        if(tumMesajlar==null)
        {
            tumMesajlar=ArrayList<Mesajlar>()
            mesajID=HashSet<String>()
        }
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("özel_mesaj").child(FirebaseAuth.getInstance().currentUser?.uid!!).child(konusulanID!!)
            .child("sohbet_mesajlari").addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(tekMesaj in p0.children)
                    {
                        if(!mesajID!!.contains(tekMesaj.key))
                        {
                            mesajID!!.add(tekMesaj.key!!)
                            var mesajlar = Mesajlar()
                            mesajlar.kullanici_id=tekMesaj.getValue(Mesajlar::class.java)?.kullanici_id
                            mesajlar.tarih=tekMesaj.getValue(Mesajlar::class.java)?.tarih
                            mesajlar.mesaj=tekMesaj.getValue(Mesajlar::class.java)?.mesaj
                            //   Log.e("DATABAS","DB  "+mesajlar.mesaj)
                            tumMesajlar!!.add(mesajlar)
                            myAdapter?.notifyDataSetChanged()

                            if(myAdapter!=null)
                                mesajlarAyrintiRecy.scrollToPosition(myAdapter?.itemCount!!-1)
                            if(myAdapter==null)
                            {
                                MesajAdapterSet()
                            }
                        }


                    }




                }


            })


    }

    private fun baslatListener(){
        dbRef= FirebaseDatabase.getInstance().getReference().child("özel_mesaj").child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .child(konusulanID!!).child("sohbet_mesajlari")

        dbRef!!.addValueEventListener(mValueEventListener)
    }
    private fun getMesajTarihi(): String? {
        var sdf= SimpleDateFormat("HH:mm:ss", Locale("tr"))
        return sdf.format(Date())
    }
    private fun MesajAdapterSet() {
        //  tumMesajlar!!.forEach { Log.e("BU MESAJLAR","MESAJ "+it.mesaj) }
        myAdapter= MesajlarAyrintiAdapter(this,tumMesajlar!!)
        mesajlarAyrintiRecy.adapter=myAdapter
        mesajlarAyrintiRecy.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        //mesela 100 tane mesaj varsa en baştan başlamasın en son mesajdan itiraben görsün
        mesajlarAyrintiRecy.scrollToPosition(myAdapter?.itemCount!!-1)
    }
}
