package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.mezuntakipsistemi.Adapter.DeneyimlerRecyAdapter
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.DataClass.GirisCikisTarihleri
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.example.mezuntakipsistemi.EventBus.EventbusDataEvents
import com.example.mezuntakipsistemi.Fragment.MesajAtDialogFragment
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_kisi_profilleri.*
import org.greenrobot.eventbus.EventBus

class KisiProfilleriActivity : AppCompatActivity() {
        var kullanici_ID : String ? =null
        var dbRef : DatabaseReference ? =null
        var myAdapter : DeneyimlerRecyAdapter? =null
        var Deneyimler : ArrayList<Firmalar> ?= null
        var set : HashSet<String> ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kisi_profilleri)
        Finit()
    }

    private fun Finit() {
        if(Deneyimler==null)
        {
            Deneyimler=ArrayList()
            set= HashSet()
        }


        kullanici_ID=intent.getStringExtra("user_id")
        btnMesajAt.setOnClickListener {
            var dialog = MesajAtDialogFragment()
            dialog.show(supportFragmentManager,"asd")
            EventBus.getDefault().postSticky(EventbusDataEvents.MesajIdGonder(kullanici_ID!!))


        }
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("kullanici").child(kullanici_ID!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(user in p0.children)
                {
                    if(user.key.equals("Kisisel_Bilgiler"))
                    {
                        var isim = user.getValue(KisiselBilgilerUser::class.java)?.ad_soyad
                        var sehir = user.getValue(KisiselBilgilerUser::class.java)?.yasadigiSehir
                        var foto = user.getValue(KisiselBilgilerUser::class.java)?.profil_resmi
                        if(foto!="")
                        {
                            Picasso.get().load(foto).into(KpPP)
                        }
                        tvAdSoyadKP.text=isim
                        tvSehirKP.text=sehir
                    }
                    if(user.key.equals("Egitim_Bilgileri"))
                    {
                        var nesne = user.getValue(EgitimBilgileriUser::class.java)
                        tvBolumKP.text=nesne?.bolum
                        tvGirisKP.text=nesne?.girisYili
                        tvMezuniyetKP.text=nesne?.mezuniyetYili
                        tvCalismaKP.text=nesne?.calismaDurumu
                     //   tvLisansKP.text=nesne?.lisansTuru
                    }
                    if(user.key.equals("Lisanslar"))
                    {
                        var str=""
                        for(lisans in user.children)
                        {

                            var egitimNesne=lisans.getValue(GirisCikisTarihleri::class.java)
                            str=str + egitimNesne?.lisansTuru + " " + egitimNesne?.girisTarih + " " +egitimNesne?.cikisTarih +"  \n"

                        }
                        tvLisansKP.text=str
                    }
                    if(user.key.equals("Firmalar"))
                    {
                        for(firma in user.children)
                        {

                            var firmaNesne = Firmalar()
                            firmaNesne.firma_id=firma.getValue(Firmalar::class.java)?.firma_id
                            if(!set!!.contains(firmaNesne.firma_id))
                            {
                                set?.add(firmaNesne?.firma_id!!)
                                firmaNesne.firma_adi=firma.getValue(Firmalar::class.java)?.firma_adi
                                firmaNesne.pozisyon=firma.getValue(Firmalar::class.java)?.pozisyon
                                firmaNesne.yetenek=firma.getValue(Firmalar::class.java)?.yetenek
                                firmaNesne.calisma_suresi=firma.getValue(Firmalar::class.java)?.calisma_suresi
                                firmaNesne.baslangicTarih=firma.getValue(Firmalar::class.java)?.baslangicTarih
                                firmaNesne.bitisTarih=firma.getValue(Firmalar::class.java)?.bitisTarih
                                Deneyimler!!.add(firmaNesne)

                            }

                        }
                        if(myAdapter==null)
                        {
                            adapterHazirla()
                        }
                    }

                }

            }


        })


    }

    private fun adapterHazirla() {
        myAdapter= DeneyimlerRecyAdapter(this,Deneyimler!!)
        KpDeneyimlerRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        KpDeneyimlerRecy.layoutManager=linearLayoutManager

    }
}
