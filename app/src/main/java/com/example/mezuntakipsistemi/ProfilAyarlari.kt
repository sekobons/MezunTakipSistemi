package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.example.mezuntakipsistemi.Fragment.EgitimBilgileriFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profil.*

class ProfilAyarlari : AppCompatActivity() {
    var dbRef : DatabaseReference ? =null
    var ppvar_mi =true
    var pp_link : String ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        init()
    }

    private fun init() {
            var ref = FirebaseDatabase.getInstance().reference
            ref.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Kisisel_Bilgiler").addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.getValue()!=null)
                        {

                            var nesne=p0.getValue(KisiselBilgilerUser::class.java)
                                etOgrNo.setText( nesne?.ogrNo)
                                etAdSoyad.setText( nesne?.ad_soyad)
                                etDogumTarihi.setText( nesne?.dogumTarihi)
                                etYasadigiSehir.setText( nesne?.yasadigiSehir)
                                etTelefonNo.setText( nesne?.telNo)
                                etSite.setText( nesne?.webSite)
                            if(nesne?.profil_resmi.equals("")){
                                ppvar_mi=false
                            }
                            else
                                pp_link=nesne?.profil_resmi

                        }

                    }


                }
            )

        btnDevam.setOnClickListener {

            if(!etAdSoyad.text.isNullOrEmpty() && !etDogumTarihi.text.isNullOrEmpty() && !etOgrNo.text.isNullOrEmpty() && !etTelefonNo.text.isNullOrEmpty()
            && !etYasadigiSehir.text.isNullOrEmpty())
            {
                datBaseKaydet()
                var transcation=supportFragmentManager.beginTransaction()
                icConst.visibility= View.GONE
                transcation.replace(R.id.framecontainer,EgitimBilgileriFragment())
                transcation.addToBackStack("asd")
                transcation.commit()
            }
            else
            {
                Toast.makeText(this@ProfilAyarlari,"Boş alan bırakmayınız..",Toast.LENGTH_SHORT).show()
            }


        }
    }
        // Kişisel bilgileri database'e kaydet

    private fun datBaseKaydet() {
        dbRef=FirebaseDatabase.getInstance().getReference()
        var userNesne=KisiselBilgilerUser()
        if(!ppvar_mi)
        userNesne.profil_resmi=""
        else
            userNesne.profil_resmi=pp_link
        userNesne.dogumTarihi=etDogumTarihi.text.toString()
        userNesne.ogrNo=etOgrNo.text.toString()
        userNesne.webSite=etSite.text.toString()
        userNesne.telNo=etTelefonNo.text.toString()
        userNesne.user_id=FirebaseAuth.getInstance().currentUser?.uid!!
        userNesne.yasadigiSehir=etYasadigiSehir.text.toString()
        userNesne.ad_soyad=etAdSoyad.text.toString()

        dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Kisisel_Bilgiler")
            .setValue(userNesne)

    }
}
