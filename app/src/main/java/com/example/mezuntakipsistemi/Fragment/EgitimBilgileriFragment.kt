package com.example.mezuntakipsistemi.Fragment


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mezuntakipsistemi.DataClass.*
import com.example.mezuntakipsistemi.ProfilAyarlari
import com.example.mezuntakipsistemi.ProfilimActivity

import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_egitim_bilgileri.*
import org.jsoup.Jsoup

class EgitimBilgileriFragment : Fragment() {
        var dbRef : DatabaseReference ?  =null
        lateinit var lisansTur2 : Spinner
        lateinit var lisansTur3 : Spinner
        lateinit var lisansTur4 : Spinner
        lateinit var giris1: Spinner
        lateinit var giris2: Spinner
        lateinit var giris3: Spinner
        lateinit var giris4: Spinner

        lateinit var cikis1: Spinner
        lateinit var cikis2: Spinner
        lateinit var cikis3: Spinner
        lateinit var cikis4: Spinner

        lateinit var bolum : Spinner
        lateinit var egitimSayi : Spinner
        lateinit var RadioOgretim : RadioGroup
        lateinit var Radio1Ogretim : RadioButton
        lateinit var Radio2Ogretim : RadioButton
        lateinit var RadioCalisma : RadioGroup
        lateinit var RadioCalisiyor : RadioButton
        lateinit var RadioCalismiyor : RadioButton
        lateinit var girisYili : EditText
        lateinit var mezunYili : EditText
        lateinit var kaydet : Button
        lateinit var diplomaNotu : EditText
        lateinit var Firma : EditText
        var BolumlerArrayList  = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view =inflater.inflate(R.layout.fragment_egitim_bilgileri, container, false)
        var lisansSayi=1
        var flag=2
        var flag2=1
        bolum=view.findViewById(R.id.spinnerBolumO)
        var myAdapter2= ArrayAdapter<String>(activity,R.layout.support_simple_spinner_dropdown_item,BolumlerArrayList)
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
                bolum.adapter=myAdapter2


            }


        })
            lisansTur2=view.findViewById(R.id.spinnerLisansTuru2)
            lisansTur3=view.findViewById(R.id.spinnerLisansTuru3)
            lisansTur4=view.findViewById(R.id.spinnerLisansTuru4)
            giris1=view.findViewById(R.id.spinnerGiris1)
            giris2=view.findViewById(R.id.spinnerGiris2)
            giris3=view.findViewById(R.id.spinnerGiris3)
            giris4=view.findViewById(R.id.spinnerGiris4)
            cikis1=view.findViewById(R.id.spinnerCikis1)
            cikis2=view.findViewById(R.id.spinnerCikis2)
            cikis3=view.findViewById(R.id.spinnerCikis3)
            cikis4=view.findViewById(R.id.spinnerCikis4)

        egitimSayi=view.findViewById(R.id.spinnerEgitimSayisi)
        RadioOgretim=view.findViewById(R.id.RadioGrupOgretim)
        Radio1Ogretim=view.findViewById(R.id.radio1ogretim)
        Radio2Ogretim=view.findViewById(R.id.radio2ogretim)
        RadioCalisma=view.findViewById(R.id.RadioGrupCalisma)
        RadioCalisiyor=view.findViewById(R.id.radioCalisiyorum)
        RadioCalismiyor=view.findViewById(R.id.radioCalismiyorum)
        girisYili=view.findViewById(R.id.etGirisYili)
        mezunYili=view.findViewById(R.id.etMezuniyet)
        diplomaNotu=view.findViewById(R.id.etDiplomaNotu)
        kaydet=view.findViewById(R.id.btnKaydet)



        // bilgileri çek
        var ref = FirebaseDatabase.getInstance().reference
        ref.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Egitim_Bilgileri")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.getValue()!=null)
                    {
                        var nesne = p0.getValue(EgitimBilgileriUser::class.java)
                        girisYili.setText(nesne?.girisYili)
                        mezunYili.setText(nesne?.mezuniyetYili)
                        diplomaNotu.setText(nesne?.diplomaNotu)
                        if(nesne?.ogretimTuru.equals("1.Ogretim"))
                            Radio1Ogretim.isChecked=true
                        else
                            Radio2Ogretim.isChecked=true
                        if(nesne?.calismaDurumu.equals("Calisiyor"))
                            RadioCalisiyor.isSelected=true
                        else
                            RadioCalismiyor.isSelected=true
                        if(nesne?.lisansTuru.equals("Lisans"))
                        {
                            spinnerLisansTuru.setSelection(0)
                        }
                        else if (nesne?.lisansTuru.equals("YüksekLisans"))
                        {
                            spinnerLisansTuru.setSelection(1)
                        }
                        else
                        {
                            spinnerLisansTuru.setSelection(2)
                        }
                        for(i in 0..BolumlerArrayList.size-1)
                        {
                            if(BolumlerArrayList.get(i).equals(nesne?.bolum))
                            {
                                bolum.setSelection(i)
                                break
                            }
                        }


                    }

                }


            })


        egitimSayi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var x= egitimSayi.selectedItem
                giris1.visibility=View.INVISIBLE
                cikis1.visibility=View.INVISIBLE
                giris2.visibility=View.INVISIBLE
                cikis2.visibility=View.INVISIBLE
                giris3.visibility=View.INVISIBLE
                cikis3.visibility=View.INVISIBLE
                giris4.visibility=View.INVISIBLE
                cikis4.visibility=View.INVISIBLE
                lisansTur2.visibility=View.INVISIBLE
                lisansTur3.visibility=View.INVISIBLE
                lisansTur4.visibility=View.INVISIBLE
                if(x.toString().toInt()==1)
                {
                giris1.visibility=View.VISIBLE
                cikis1.visibility=View.VISIBLE
                    lisansSayi=1

                }
                else if(x.toString().toInt()==2)
                {
                    giris1.visibility=View.VISIBLE
                    cikis1.visibility=View.VISIBLE
                    giris2.visibility=View.VISIBLE
                    cikis2.visibility=View.VISIBLE
                    lisansTur2.visibility=View.VISIBLE
                    lisansSayi=2
                }
                else if(x.toString().toInt()==3)
                {
                    giris1.visibility=View.VISIBLE
                    cikis1.visibility=View.VISIBLE
                    giris2.visibility=View.VISIBLE
                    cikis2.visibility=View.VISIBLE
                    giris3.visibility=View.VISIBLE
                    cikis3.visibility=View.VISIBLE
                    lisansTur2.visibility=View.VISIBLE
                    lisansTur3.visibility=View.VISIBLE
                    lisansSayi=3
                }
                else
                {
                    giris1.visibility=View.VISIBLE
                    cikis1.visibility=View.VISIBLE
                    giris2.visibility=View.VISIBLE
                    cikis2.visibility=View.VISIBLE
                    giris3.visibility=View.VISIBLE
                    cikis3.visibility=View.VISIBLE
                    giris4.visibility=View.VISIBLE
                    cikis4.visibility=View.VISIBLE
                    lisansTur2.visibility=View.VISIBLE
                    lisansTur3.visibility=View.VISIBLE
                    lisansTur4.visibility=View.VISIBLE
                    lisansSayi=4
                }
            }

        }

        Radio1Ogretim.setOnClickListener {
            flag2=1
        }
        Radio2Ogretim.setOnClickListener {
            flag2=2
        }

        RadioCalisiyor.setOnClickListener{

            flag=1
        }
        RadioCalismiyor.setOnClickListener {

            flag=2
        }
        kaydet.setOnClickListener {
            dbRef=FirebaseDatabase.getInstance().getReference()
            if(!girisYili.text.isNullOrEmpty() && !mezunYili.text.isNullOrEmpty() && !diplomaNotu.text.isNullOrEmpty())
            {
                    var egitimUser=EgitimBilgileriUser()
               // egitimUser.bolum=bolum.text.toString()
                if(flag==1)
                {
                    egitimUser.calismaDurumu="Calisiyor"
                }
                else if(flag==2)
                {
                    egitimUser.calismaDurumu="Calismiyor"
                }


                egitimUser.bolum=bolum.selectedItem.toString()
                egitimUser.diplomaNotu=diplomaNotu.text.toString()
              //  egitimUser.universite=universite.text.toString()
                egitimUser.girisYili=girisYili.text.toString()
                egitimUser.mezuniyetYili=mezunYili.text.toString()

                var lisanslar = GirisCikisTarihleri()
              Log.e("Tag","Tag"+ lisansSayi)

                if(lisansSayi==1)
                {
                    lisanslar.lisansTuru=spinnerLisansTuru.selectedItem.toString()
                    lisanslar.girisTarih=giris1.selectedItem.toString()
                    lisanslar.cikisTarih=cikis1.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(spinnerLisansTuru.selectedItem.toString())
                        .setValue(lisanslar)
                }
                else if(lisansSayi==2)
                {
                    lisanslar.lisansTuru=spinnerLisansTuru.selectedItem.toString()
                    lisanslar.girisTarih=giris1.selectedItem.toString()
                    lisanslar.cikisTarih=cikis1.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(spinnerLisansTuru.selectedItem.toString())
                        .setValue(lisanslar)
                 lisanslar.lisansTuru=lisansTur2.selectedItem.toString()
                 lisanslar.girisTarih=giris2.selectedItem.toString()
                 lisanslar.cikisTarih=cikis2.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(lisansTur2.selectedItem.toString())
                        .setValue(lisanslar)
                }
                else if(lisansSayi==3)
                {
                    lisanslar.lisansTuru=lisansTur2.selectedItem.toString()
                    lisanslar.girisTarih=giris2.selectedItem.toString()
                    lisanslar.cikisTarih=cikis2.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(lisansTur2.selectedItem.toString())
                        .setValue(lisanslar)
                    lisanslar.lisansTuru=lisansTur3.selectedItem.toString()
                    lisanslar.girisTarih=giris3.selectedItem.toString()
                    lisanslar.cikisTarih=cikis3.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(lisansTur3.selectedItem.toString())
                        .setValue(lisanslar)
                }
                else
                {

                    lisanslar.lisansTuru=lisansTur2.selectedItem.toString()
                    lisanslar.girisTarih=giris2.selectedItem.toString()
                    lisanslar.cikisTarih=cikis2.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(lisansTur2.selectedItem.toString())
                        .setValue(lisanslar)
                    lisanslar.lisansTuru=lisansTur3.selectedItem.toString()
                    lisanslar.girisTarih=giris3.selectedItem.toString()
                    lisanslar.cikisTarih=cikis3.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(lisansTur3.selectedItem.toString())
                        .setValue(lisanslar)
                    lisanslar.lisansTuru=lisansTur4.selectedItem.toString()
                    lisanslar.girisTarih=giris4.selectedItem.toString()
                    lisanslar.cikisTarih=cikis4.selectedItem.toString()
                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("Lisanslar").child(lisansTur4.selectedItem.toString())
                        .setValue(lisanslar)


                }



              //  egitimUser.lisansTuru=spinnerLisansTuru.selectedItem.toString() // lisans türü
                if(flag2==1)
                    egitimUser.ogretimTuru="1.Ogretim"
                else if(flag2==2)
                    egitimUser.ogretimTuru="2.Ogretim"

                dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .child("Egitim_Bilgileri").setValue(egitimUser)
             //   var uniNesne = Universiteler()


/*
                var bolumNesne=Bolumler()
                bolumNesne.bolum_adi=spinnerBolumO.selectedItem.toString()
                bolumNesne.bolum_id=hashmap2.getValue(spinnerBolumO.selectedItem.toString())
*/
            //    dbRef!!.child("Bolumler").child(hashmap2.getValue(spinnerBolum.selectedItem.toString())).setValue(bolumNesne)


                if(flag==2 || flag==1)
                {
                    var act=activity as ProfilAyarlari
                    Toast.makeText(activity,"Eğitim bilgileri başarıyla kaydedildi",Toast.LENGTH_SHORT).show()
                    var intent=Intent(act,ProfilimActivity::class.java)
                    startActivity(intent)
                    act.finish()

                }/*
                if(flag==1 && !Firma.text.isNullOrEmpty())
                {


                    var firmaKey=FirebaseDatabase.getInstance().reference.push().key
                    var firmaNesne = Firmalar()
                    firmaNesne.firma_il=""
                    firmaNesne.firma_website=""
                    firmaNesne.firma_telNo=""
                    firmaNesne.firma_sektoru=""
                    firmaNesne.firma_adres=""
                    firmaNesne.firma_adi=Firma.text.toString()
                    firmaNesne.firma_id=firmaKey
                    firmaNesne.calisiyor_mu="Calisiyor"

                    dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .child("Firmalar").child(firmaKey!!).setValue(firmaNesne)

                    var act=activity as ProfilAyarlari
                    Toast.makeText(activity,"Eğitim bilgileri başarıyla kaydedildi",Toast.LENGTH_SHORT).show()
                    var intent=Intent(act,ProfilimActivity::class.java)
                    startActivity(intent)
                   act.finish()


                }*/
             //   else if(flag==1 && Firma.text.isNullOrEmpty())
                  //  Toast.makeText(activity,"Lütfen firma bilginizi giriniz",Toast.LENGTH_SHORT).show()



            }
            else
                Toast.makeText(activity,"Boş alan bırakmayınız",Toast.LENGTH_SHORT).show()

        }





        return view
    }
/*
    inner class init  : AsyncTask<Void, Void, Void>() {

        var myAdapter=ArrayAdapter<String>(activity,R.layout.support_simple_spinner_dropdown_item,UniversiteArrayList)
        var myAdapter2=ArrayAdapter<String>(activity,R.layout.support_simple_spinner_dropdown_item,BolumlerArrayList)


        override fun doInBackground(vararg params: Void?): Void? {
            /*
           var  doc= Jsoup.connect("https://sinavla.com/universiteler")
                .get()
           var x=0
            var elements = doc.select("a")
          var doc2= Jsoup.connect("https://www.hangiuniversite.com/bolumler").get()
           var elements2=doc2.select("div[class=\"block m-p-base m-mb-base\"]")
           */
           UniversiteArrayList.add("Konya Teknik Üniversitesi")
            myAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            myAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)


            return null

        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            spinnerBolumO.adapter=myAdapter2

        }


    }
*/

}
