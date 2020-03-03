package com.example.mezuntakipsistemi

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.mezuntakipsistemi.Adapter.OgrenciListeleRecyclerViewAdapter
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ogrenci.*
import org.jsoup.Jsoup

class OgrenciActivity : AppCompatActivity() {

    var myAdapter : OgrenciListeleRecyclerViewAdapter ? =null
    var tumKisiler  : ArrayList<EgitimBilgileriUser> ? =null
    var dbRef : DatabaseReference ? =null
    var BolumlerArrayList  = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ogrenci)
        //init().execute()
        Finit()
        spinnerYetenekOgrenci.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tumKisiler?.clear()
                myAdapter?.notifyDataSetChanged()
                btnOgrenciListele.isEnabled=true
                TextleriGosterme()
                tvBulunamadi.text=""

            }

        }

        spinnerBolumSec.onItemSelectedListener = object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tumKisiler?.clear()
                myAdapter?.notifyDataSetChanged()
                btnOgrenciListele.isEnabled=true
                TextleriGosterme()
                tvBulunamadi.text=""
            }


        }
        spinnerGirisYili.onItemSelectedListener = object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tumKisiler?.clear()
                myAdapter?.notifyDataSetChanged()
                btnOgrenciListele.isEnabled=true
                TextleriGosterme()
                tvBulunamadi.text=""
            }


        }


    }

    private fun Finit() {
        // Bölümleri databaseden çek
        var myAdapter2= ArrayAdapter<String>(this@OgrenciActivity,R.layout.support_simple_spinner_dropdown_item,BolumlerArrayList)
        myAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        var myRef = FirebaseDatabase.getInstance().reference
        myRef.child("bolumler").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(bolum in p0.children)
                {

                    BolumlerArrayList.add(bolum.child("bolum_adi").getValue().toString())
                }
                spinnerBolumSec.adapter=myAdapter2


            }


        })


        var knt=0
        var knt2=0
        if(tumKisiler==null)
        {
            tumKisiler=ArrayList()
        }

        btnOgrenciListele.setOnClickListener {

            btnOgrenciListele.isEnabled=false
            dbRef=FirebaseDatabase.getInstance().getReference()
            dbRef!!.child("kullanici").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(user in p0.children)
                    {
                        knt =0
                        knt2 =0
                        var userNesne = EgitimBilgileriUser()
                        for(bilgiler in user.children)
                        {
                            Log.e("key","key "+bilgiler.key)
                            if(bilgiler.key.equals("Egitim_Bilgileri"))
                            {
                                var egitimNesne = bilgiler.getValue(EgitimBilgileriUser::class.java)

                                if(egitimNesne!!.bolum!!.equals(spinnerBolumSec.selectedItem) && egitimNesne.girisYili!!.equals(spinnerGirisYili.selectedItem)
                                    )
                                {
                                    userNesne.bolum=egitimNesne?.bolum
                                    userNesne.mezuniyetYili=egitimNesne?.mezuniyetYili
                                    userNesne.girisYili=egitimNesne?.girisYili
                                    userNesne.ogretimTuru=egitimNesne?.ogretimTuru


                                    knt=1
                                    Log.e("Aldi","ALdi")

                                }


                            }
                            if(bilgiler.key.equals("Kisisel_Bilgiler"))
                            {
                                Log.e("knt ","knt "+knt)
                                if(knt==1)
                                {
                                    var isim = bilgiler.getValue(KisiselBilgilerUser::class.java)?.ad_soyad

                                    userNesne.diplomaNotu = bilgiler.getValue(KisiselBilgilerUser::class.java)?.user_id
                                    userNesne.calismaDurumu=isim

                                    Log.e("Aldi","ALdi 2")

                                }

                            }
                            if(bilgiler.key.equals("Firmalar"))
                            {
                                for(firma in bilgiler.children)
                                {
                                    var yetenek = firma.getValue(Firmalar::class.java)?.yetenek
                                    if(yetenek!!.equals(spinnerYetenekOgrenci.selectedItem))
                                    {
                                        knt2=1
                                    }

                                }
                            }

                        }
                        if(knt==1 && knt2==1)
                        {
                            tumKisiler?.add(userNesne)
                            tvBulunamadi.text=""
                            TextleriAktiflestir()
                        }

                        AdapterHazirla()
                    }
                    if(tumKisiler?.size==0)
                        tvBulunamadi.text="Aradığınız kriterlere uygun öğrenci bulunamadı"

                }


            })


        }




    }

    private fun TextleriAktiflestir() {
        textView51.visibility=View.VISIBLE
        textView52.visibility=View.VISIBLE
        textView53.visibility=View.VISIBLE
        textView54.visibility=View.VISIBLE
    }
    private fun TextleriGosterme(){
        textView51.visibility=View.INVISIBLE
        textView52.visibility=View.INVISIBLE
        textView53.visibility=View.INVISIBLE
        textView54.visibility=View.INVISIBLE
    }

    private fun AdapterHazirla(){
       myAdapter = OgrenciListeleRecyclerViewAdapter(this,tumKisiler!!)
        ogrenciRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        ogrenciRecy.layoutManager=linearLayoutManager
    }
/*
    private fun kisiUniIDBul() {
        dbRef= FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(user in p0.children)
                    {
                        if(user.key.equals("Egitim_Bilgileri"))
                        {
                            myUniID=user.getValue(EgitimBilgileriUser::class.java)?.universite_id

                            break
                        }

                    }

                }


            })
    }
*/
/*
    inner class init  : AsyncTask<Void, Void, Void>() {


        var myAdapter2= ArrayAdapter<String>(this@OgrenciActivity,R.layout.support_simple_spinner_dropdown_item,BolumlerArrayList)


        override fun doInBackground(vararg params: Void?): Void? {
            myAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                var myRef = FirebaseDatabase.getInstance().reference
                myRef.child("bolumler").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for(bolum in p0.children)
                        {

                            BolumlerArrayList.add(bolum.child("bolum_adi").getValue().toString())
                        }
                        Log.e("taggg","taggg"+"Adapteri aldım etttim")

                    }


                })
            for(i in 0..BolumlerArrayList.size-1)
                Log.e("taggg","taggg"+BolumlerArrayList.get(i))





            return null

        }



        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Log.e("taggg","taggg"+"Post etttim")
            spinnerBolumSec.adapter=myAdapter2

        }


    }*/
}
