package com.example.mezuntakipsistemi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.CheckBox
import com.example.mezuntakipsistemi.Adapter.FirmadakiCalisanlarAdapter
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_firma_ayrinti.*

class FirmaAyrintiActivity : AppCompatActivity() , View.OnClickListener{
    override fun onClick(v: View?) {
        v as CheckBox
        when(v.id)
        {
            R.id.calisiyorCheck -> if(calisiyorCheck.isChecked)
            {

                if(calismisCheck.isChecked)
                {
                    flag=0
                    tumCalisanlar!!.clear()
                    myAdapter?.notifyDataSetChanged()
             //       set!!.clear()
                Finit()
                }
                else
                {
                    flag =1
                    Log.e("flag","flag "+flag)
                    tumCalisanlar!!.clear()
                    myAdapter?.notifyDataSetChanged()
              //      set!!.clear()
                    Finit()
                }
            }

            R.id.calismisCheck -> if(calismisCheck.isChecked)
            {
                if(calisiyorCheck.isChecked)
                {
                    flag=0
                    tumCalisanlar!!.clear()
                    myAdapter?.notifyDataSetChanged()
            //       set!!.clear()
                    Finit()
                }

                else
                {
                 flag =2
                    Log.e("flag","flag "+flag)
                    tumCalisanlar!!.clear()
                    myAdapter?.notifyDataSetChanged()
              //      set!!.clear()
                    Finit()
                }

            }
        }
    }
    var tumCalisanlar : ArrayList<Firmalar> ? =null
    var dbRef : DatabaseReference  ?=null
    var set : HashSet<String> ? =null
    var firma_id : String  ?=null
    var firma_adi : String ?=null
    var firma_adres : String ? =null
    var flag =0
    var myAdapter  : FirmadakiCalisanlarAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firma_ayrinti)
        calisiyorCheck.setOnClickListener(this)
        calismisCheck.setOnClickListener(this)
        calisiyorCheck.isChecked=true
        calismisCheck.isChecked=true
        Finit()
    }


    private fun Finit() {
        btnYerGoster.setOnClickListener {
            var intent= Intent(this,HaritaActivity::class.java)
            intent.putExtra("firma_adres",firma_adres)
            intent.putExtra("firma_adi",firma_adi)
            startActivity(intent)
        }
        if(tumCalisanlar==null)
        {
            tumCalisanlar=ArrayList()
            set= HashSet()
        }

       firma_id = intent.getStringExtra("firma_id")
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("kullanici").addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(user in p0.children)
                {
                    var userkey=user.key
                    for(firma in user.children)
                    {
                  //      if(!set!!.contains(userkey))
                 //   {
                    //    set!!.add(userkey!!)

                        if(firma.key.equals("Firmalar"))
                        {
                            for(firmalar in firma.children)
                            {
                                var firmaNesne = firmalar.getValue(Firmalar::class.java)
                                var myFirma =Firmalar()
                                if(firmaNesne?.firma_id.equals(firma_id))
                                {
                                    tvFirmaAdiA.text=firmaNesne?.firma_adi
                                    firma_adres = firmaNesne?.firma_adres
                                    firma_adi=firmaNesne?.firma_adi
                                    tvSektorA.text=firmaNesne?.firma_sektoru
                                    tvSehirA.text=firmaNesne?.firma_il
                                    tvWebSiteA.text=firmaNesne?.firma_website
                                    tvTelNoA.text=firmaNesne?.firma_telNo
                                    myFirma.calisiyor_mu=firmaNesne?.calisiyor_mu

                                    myFirma.yetenek=firmaNesne?.yetenek
                                    myFirma.calisma_suresi=firmaNesne?.calisma_suresi
                                    myFirma.firma_adres=userkey
                                    var isim  =""
                                    var dbRef2 = FirebaseDatabase.getInstance().reference
                                    dbRef2.child("kullanici").child(userkey!!).child("Kisisel_Bilgiler")

                                        .addValueEventListener(object : ValueEventListener{
                                            override fun onCancelled(p0: DatabaseError) {
                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                for(userx in p0.children)
                                                {
                                                    isim= userx.getValue().toString()
                                                    break

                                                }
                                                myFirma.firma_telNo=isim
                                                if(flag==1 && myFirma.calisiyor_mu.equals("Calisiyor"))
                                                {
                                                    tumCalisanlar?.add(myFirma)
                                                }
                                                else if(flag==2 && myFirma.calisiyor_mu.equals("Calismiyor")) {
                                                    tumCalisanlar?.add(myFirma)
                                                }
                                                else if (flag==0)
                                                    tumCalisanlar?.add(myFirma)

                                                adapterHazirla()
                                            }

                                        })




                                }
                            }
                        }
                   // }

                    }
                }

            }


        })

    }

    private fun adapterHazirla() {
        myAdapter=FirmadakiCalisanlarAdapter(this,tumCalisanlar!!)
        calismislarRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        calismislarRecy.layoutManager=linearLayoutManager

    }
}
