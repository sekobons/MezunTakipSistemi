package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mezuntakipsistemi.DataClass.Ilanlar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ilanlar_ayrinti.*

class IlanlarAyrintiActivity : AppCompatActivity() {
        var dbRef : DatabaseReference ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ilanlar_ayrinti)
        Finit()
    }

    private fun Finit() {
            var ilan_id = intent.getStringExtra("ilan_id")
            dbRef=FirebaseDatabase.getInstance().getReference()
            dbRef!!.child("İs_İlanları").orderByKey().equalTo(ilan_id).addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(ilan in p0.children)
                    {
                        var tekIlan = ilan.getValue(Ilanlar::class.java)
                        tvFirmaAdAyrinti.text=tekIlan?.firma_adi
                        tvAskerlikAyrinti.text=tekIlan?.askerlik_durumu
                        tvCalismaAyrinti.text=tekIlan?.calisma_sekli
                        tvEgitimAyrinti.text=tekIlan?.egitim_durumu
                        tvPozisyonAyrinti.text=tekIlan?.is_pozisyon
                        tvNitelikleri.text=tekIlan?.nitelikler
                        tvTanimi.text=tekIlan?.is_tanimi
                    }

                }


            })
    }
}
