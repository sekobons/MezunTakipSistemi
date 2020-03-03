package com.example.mezuntakipsistemi.Fragment


import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.FirmalarimActivity

import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_egitim_bilgileri.*
import kotlinx.android.synthetic.main.fragment_firma_ekle_dialog.*
import org.jsoup.Jsoup


class FirmaEkleDialogFragment : DialogFragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        v as RadioButton
        when(v.id){
            R.id.calisiyorumRadio -> if(calisiyorumRadio.isChecked)
            {
                Log.e("Yaz","Yaz calis")
                flag=1
            }
            R.id.calismiyorumRadio -> if(calismiyorumRadio.isChecked)
            {
                Log.e("Yaz","Yaz calismiyorum")
                flag=2
            }
        }
    }

    var dbRef : DatabaseReference ? =null
   lateinit var firmaAdi : EditText
   lateinit var SektorSpinner : Spinner
   lateinit var SehirSpinner : Spinner
   lateinit var calisiyorRadio : RadioButton
    lateinit var calismiyorRadio : RadioButton
    lateinit var radioGrup : RadioGroup
    lateinit var baslangicSpinner : Spinner
    lateinit var bitisSpinner : Spinner
    lateinit var spinnerFirmalar : Spinner
    var flag=2
   lateinit var spinnerYetenek : Spinner
   lateinit var spinnerPozisyonu : Spinner
   lateinit var spinnerCalismaSuresi : Spinner
   lateinit var butonKaydet : Button
    lateinit var ToplamKisi : Spinner
   lateinit var GeriImg : ImageView
    var firmalar =ArrayList<String> ()
    var hashmap = HashMap<String,String>()

    var sehirlerArrayList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init().execute()
        var view = inflater.inflate(R.layout.fragment_firma_ekle_dialog, container, false)
        ToplamKisi=view.findViewById(R.id.spinnerCalisanSayi)
        spinnerYetenek=view.findViewById(R.id.spinneryetenek)
        spinnerPozisyonu=view.findViewById(R.id.spinnerCalisilanPozisyon)
        spinnerCalismaSuresi=view.findViewById(R.id.spinnerSure)
        radioGrup=view.findViewById(R.id.radioGrupcum)
        calisiyorRadio=view.findViewById(R.id.calisiyorumRadio)
        calismiyorRadio=view.findViewById(R.id.calismiyorumRadio)
        firmaAdi=view.findViewById(R.id.etFirmaAdi)
        spinnerFirmalar=view.findViewById(R.id.spinnerMevcutFirmalar)
        SektorSpinner=view.findViewById(R.id.spinnerSektor)
        SehirSpinner=view.findViewById(R.id.spinnerSehirler)
        calisiyorRadio.setOnClickListener(this)
        calismiyorRadio.setOnClickListener(this)
        baslangicSpinner=view.findViewById(R.id.spinnerBaslangic)
        bitisSpinner=view.findViewById(R.id.spinnerBitis)


        GeriImg=view.findViewById(R.id.imgGeri)
        butonKaydet=view.findViewById(R.id.btnFirmaKaydet)
        butonKaydet.setOnClickListener {


                    if(spinnerFirmalar.selectedItem.toString().equals("Yok"))
                    {
                        if(!firmaAdi.text.isNullOrEmpty())
                        {
                            var firmaKey = FirebaseDatabase.getInstance().reference.push().key
                            var firmaNesne = Firmalar()
                            firmaNesne.firma_il=SehirSpinner.selectedItem.toString()
                            firmaNesne.firma_id=firmaKey
                            firmaNesne.firma_adi=firmaAdi.text.toString()
                            firmaNesne.yetenek=spinnerYetenek.selectedItem.toString()
                            firmaNesne.calisma_suresi=spinnerCalismaSuresi.selectedItem.toString()
                            firmaNesne.pozisyon=spinnerPozisyonu.selectedItem.toString()
                            firmaNesne.firma_sektoru=SektorSpinner.selectedItem.toString()
                            firmaNesne.calisan_sayisi=ToplamKisi.selectedItem.toString()
                            firmaNesne.baslangicTarih=baslangicSpinner.selectedItem.toString()
                            firmaNesne.bitisTarih=bitisSpinner.selectedItem.toString()
                            if(flag==1)
                            {
                                firmaNesne.calisiyor_mu="Calisiyor"
                                Log.e("flag","flag "+ flag)
                            }
                           else if(flag==2)
                            {
                                firmaNesne.calisiyor_mu="Calismiyor"
                                Log.e("flag","flag "+ flag)
                            }


                            dbRef=FirebaseDatabase.getInstance().getReference()
                            dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                                .child("Firmalar").child(firmaKey!!).setValue(firmaNesne)
                            Toast.makeText(activity,"Firma başarıyla eklendi",Toast.LENGTH_SHORT).show()
                            (activity as FirmalarimActivity).Finit()
                            dialog.dismiss()
                        }
                        else
                            Toast.makeText(activity,"Lütfen boş alan bırakmayınız..",Toast.LENGTH_SHORT).show()
                    }
            else
                    {
                      var firmaID= hashmap.get(spinnerFirmalar.selectedItem)
                        var firmaNesne = Firmalar()
                        firmaNesne.firma_il=SehirSpinner.selectedItem.toString()
                        firmaNesne.firma_id=firmaID
                        firmaNesne.firma_adi=spinnerFirmalar.selectedItem.toString()
                        firmaNesne.yetenek=spinnerYetenek.selectedItem.toString()
                        firmaNesne.calisma_suresi=spinnerCalismaSuresi.selectedItem.toString()
                        firmaNesne.pozisyon=spinnerPozisyonu.selectedItem.toString()
                        firmaNesne.firma_sektoru=SektorSpinner.selectedItem.toString()
                        firmaNesne.calisan_sayisi=ToplamKisi.selectedItem.toString()
                        firmaNesne.baslangicTarih=baslangicSpinner.selectedItem.toString()
                        firmaNesne.bitisTarih=bitisSpinner.selectedItem.toString()
                        if(flag==1)
                        {
                            firmaNesne.calisiyor_mu="Calisiyor"
                            Log.e("flag","flag "+ flag)
                        }
                        else if(flag==2)
                        {
                            firmaNesne.calisiyor_mu="Calismiyor"
                            Log.e("flag","flag "+ flag)
                        }


                        dbRef=FirebaseDatabase.getInstance().getReference()


                        dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                            .child("Firmalar").child(firmaID!!).setValue(firmaNesne)
                        Toast.makeText(activity,"Firma başarıyla eklendi",Toast.LENGTH_SHORT).show()
                        (activity as FirmalarimActivity).Finit()
                        dialog.dismiss()

                    }

        }
        GeriImg.setOnClickListener {
            dialog.dismiss()
        }


        return view
    }


    inner class init : AsyncTask<Void, Void, Void>() {
        var myAdapter2  =ArrayAdapter<String>(activity,R.layout.support_simple_spinner_dropdown_item,firmalar)
        var myAdapter = ArrayAdapter<String>(activity, R.layout.support_simple_spinner_dropdown_item, sehirlerArrayList)

        override fun doInBackground(vararg params: Void?): Void? {

            firmalar.add("Yok")
            var dbRef2 = FirebaseDatabase.getInstance().reference
            dbRef2.child("kullanici").addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(user in p0.children)
                    {
                        for(userAlt in user.children)
                        {
                            if(userAlt.key.equals("Firmalar"))
                            {
                                for(alt in userAlt.children)
                                {
                                    var firmaNesne = alt.getValue(Firmalar::class.java)
                                    if(!firmalar.contains(firmaNesne?.firma_adi))
                                    {
                                        firmalar.add(firmaNesne?.firma_adi!!)
                                        hashmap.put(firmaNesne?.firma_adi!!,firmaNesne?.firma_id!!)
                                    }


                                }

                            }
                        }
                    }
                }


            })

            var doc = Jsoup.connect("https://www.derszamani.net/turkiye-81-il-listesi-ile-plaka-ve-telefon-kodlari.html")
                .get()
            Log.e("ÇEKİYORUm", "BİLGİYİ")
            var elements = doc.select("td[width=\"143\"]")

            var x=0
            for (i in elements) {
                    x++
                if(x>1)
                sehirlerArrayList.add(i.text())

            }


            myAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)


            return null

        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            spinnerFirmalar.adapter=myAdapter2
            spinnerSehirler.adapter = myAdapter
        }


    }
}
