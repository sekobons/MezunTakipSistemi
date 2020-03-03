package com.example.mezuntakipsistemi.Fragment


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.Ilanlar
import com.example.mezuntakipsistemi.IsilanlariActivity

import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_isilanlari.*
import kotlinx.android.synthetic.main.fragment_ilan_ekle.*
import java.text.SimpleDateFormat
import java.util.*


class IlanEkleFragment : DialogFragment() {
        var dbRef : DatabaseReference ? =null
        var myUniID : String ? =null
        lateinit var firmaAdi : EditText
        lateinit var textPozisyon : TextView
        lateinit var textAskerlik : TextView
        lateinit var textEgitim : TextView
        lateinit var textCalisma : TextView
        lateinit var spinnerPoz : Spinner
        lateinit var spinnerAskerlik : Spinner
        lateinit var spinnerEgitim : Spinner
        lateinit var spinnerCalismaP : Spinner
        lateinit var nitelikler : EditText
        lateinit var isTanimi : EditText
        lateinit var butonEkle : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kisiUniIDBul()

        var view= inflater.inflate(R.layout.fragment_ilan_ekle, container, false)
        firmaAdi=view.findViewById(R.id.etFirmaIlan)
        textPozisyon=view.findViewById(R.id.tvPzs)
        textAskerlik=view.findViewById(R.id.tvAsk)
        textEgitim=view.findViewById(R.id.tvEgt)
        textCalisma=view.findViewById(R.id.tvCls)
        spinnerPoz=view.findViewById(R.id.spinnerIsPozisyon)
        spinnerAskerlik=view.findViewById(R.id.spinnerIsAskerlik)
        spinnerCalismaP=view.findViewById(R.id.spinnerIsCalismaSekli)
        spinnerEgitim=view.findViewById(R.id.spinnerIsEgitimDurumu)
        nitelikler = view.findViewById(R.id.etIsNitelik)
        isTanimi=view.findViewById(R.id.etIsTanimi)
        butonEkle=view.findViewById(R.id.btnIlanAyrintiEkle)

        butonEkle.setOnClickListener {
            if(!firmaAdi.text.isNullOrEmpty() && !isTanimi.text.isNullOrEmpty() && !nitelikler.text.isNullOrEmpty())
            {
                var ilanID = FirebaseDatabase.getInstance().reference.push().key
                var ilanNesne = Ilanlar()
                ilanNesne.olusturan_id=FirebaseAuth.getInstance().currentUser?.uid
                ilanNesne.olusturanUni_id=myUniID
                ilanNesne.egitim_durumu=spinnerEgitim.selectedItem.toString()
                ilanNesne.nitelikler=nitelikler.text.toString()
                ilanNesne.calisma_sekli=spinnerCalismaP.selectedItem.toString()
                ilanNesne.is_tanimi=isTanimi.text.toString()
                ilanNesne.firma_adi=firmaAdi.text.toString()
                ilanNesne.olusturulma_tarihi=getMesajTarihi()
                ilanNesne.ilan_key=ilanID
                ilanNesne.askerlik_durumu=spinnerAskerlik.selectedItem.toString()
                ilanNesne.is_pozisyon=spinnerPoz.selectedItem.toString()
                dbRef!!.child("İs_İlanları").child(ilanID!!).setValue(ilanNesne)
                Toast.makeText(activity,"Başarıyla eklendi",Toast.LENGTH_SHORT).show()
                (activity as IsilanlariActivity).ilanlariGetir()
                dialog.dismiss()
            }
            else
                Toast.makeText(activity,"Lütfen boş alan bırakmayınız",Toast.LENGTH_SHORT).show()

        }




        return view
    }
    private fun getMesajTarihi(): String? {
        var sdf = SimpleDateFormat("d/M/y", Locale("tr"))
        return sdf.format(Date())
    }
    private fun kisiUniIDBul() {
        // anlık kişinin üniIDsini bulmak
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
                            //      Log.e("GİRİLEN YER ","1")
                            break
                        }

                    }

                }


            })

    }


}
