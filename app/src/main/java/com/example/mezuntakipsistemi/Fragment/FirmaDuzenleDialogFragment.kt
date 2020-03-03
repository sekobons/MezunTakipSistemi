package com.example.mezuntakipsistemi.Fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.EventBus.EventbusDataEvents

import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_firma_duzenle_dialog.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jsoup.Jsoup


class FirmaDuzenleDialogFragment : DialogFragment() {
    var dbRef : DatabaseReference  ? =null
    var sehirlerArrayList = ArrayList<String>()
    var firmaID : String ? =null
    lateinit var firmaAdi : EditText
    lateinit var SektorSpinner : Spinner
    lateinit var SehirSpinner : Spinner
    lateinit var telNo : EditText
    lateinit var webSite : EditText
    lateinit var spinnerYetenek : Spinner
    lateinit var spinnerPozisyon : Spinner
    lateinit var spinnerSure : Spinner
    lateinit var butonKaydet : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init().execute()
        var view = inflater.inflate(R.layout.fragment_firma_duzenle_dialog, container, false)
        firmaAdi=view.findViewById(R.id.etFirmaAdi2)
        SektorSpinner=view.findViewById(R.id.spinnerSektor2)
        SehirSpinner=view.findViewById(R.id.spinnerSehirler2)
        telNo=view.findViewById(R.id.etTelNo2)
        webSite=view.findViewById(R.id.etWebSitesi2)
        butonKaydet=view.findViewById(R.id.btnDuzenleKaydet)
        spinnerYetenek=view.findViewById(R.id.spinnerYetenekDuzenle)
        spinnerPozisyon=view.findViewById(R.id.spinnerPozisyonDuzenle)
        spinnerSure=view.findViewById(R.id.spinnerSureDuzenle)

        butonKaydet.setOnClickListener {
            if(!firmaAdi.text.isNullOrEmpty() && !telNo.text.isNullOrEmpty())
            {
                var firmaNesne=Firmalar()
                firmaNesne.firma_sektoru=SektorSpinner.selectedItem.toString()

                firmaNesne.firma_website=webSite.text.toString()
                firmaNesne.firma_adi=firmaAdi.text.toString()
                firmaNesne.firma_telNo=telNo.text.toString()
                firmaNesne.firma_id=firmaID
                firmaNesne.firma_il=SehirSpinner.selectedItem.toString()
                firmaNesne.calisma_suresi=spinnerSure.selectedItem.toString()
                firmaNesne.yetenek=spinnerYetenek.selectedItem.toString()
                firmaNesne.pozisyon=spinnerPozisyon.selectedItem.toString()
                dbRef=FirebaseDatabase.getInstance().getReference()
                dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .child("Firmalar").child(firmaID!!).setValue(firmaNesne)
                Toast.makeText(activity,"Değişiklikler başarıyla kaydedildi",Toast.LENGTH_SHORT).show()
                dialog.dismiss()

            }
            else
                Toast.makeText(activity,"Lütfen boş alan bırakmayınız..",Toast.LENGTH_SHORT).show()



        }





        return view
    }
    inner class init : AsyncTask<Void, Void, Void>() {

        var myAdapter = ArrayAdapter<String>(activity, R.layout.support_simple_spinner_dropdown_item, sehirlerArrayList)


        override fun doInBackground(vararg params: Void?): Void? {
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
            SehirSpinner.adapter = myAdapter

            dbRef=FirebaseDatabase.getInstance().getReference()
            dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                .child("Firmalar").orderByKey().equalTo(firmaID).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for(firma in p0.children)
                        {
                            var firmaNesne=firma.getValue(Firmalar::class.java)
                            firmaAdi.setText(firmaNesne?.firma_adi)
                            telNo.setText(firmaNesne?.firma_telNo)
                            webSite.setText(firmaNesne?.firma_website)


                            Log.e("DATABASE FOR ","İÇİNDEYİZ")
                            var pozisyon = resources.getStringArray(R.array.Pozisyon)
                            for(i in 0..pozisyon.size-1)
                            {
                                if(firmaNesne?.pozisyon.equals(pozisyon.get(i)))
                                {
                                    spinnerPozisyon.setSelection(i)
                                }
                            }
                            var yetenek = resources.getStringArray(R.array.Yetenekler)
                            for(i in 0..yetenek.size-1)
                            {
                                if(firmaNesne?.yetenek.equals(yetenek.get(i)))
                                {
                                    spinnerYetenek.setSelection(i)
                                }
                            }
                            var sure = resources.getStringArray(R.array.Süreler)
                            for(i in 0..sure.size-1)
                            {
                                if(firmaNesne?.calisma_suresi.equals(sure.get(i)))
                                {
                                    spinnerSure.setSelection(i)
                                }
                            }
                            for(i in 0..sehirlerArrayList.size-1)
                            {

                                if(firmaNesne?.firma_il!!.equals(sehirlerArrayList.get(i)))
                                {

                                    SehirSpinner.setSelection(i)
                                    break
                                }
                            }
                            var sektorler=resources.getStringArray(R.array.Sektorler)
                            for(i in 0..sektorler.size-1)
                            {

                                if(firmaNesne?.firma_sektoru!!.equals(sektorler.get(i)))
                                {

                                    SektorSpinner.setSelection(i)
                                    break
                                }
                            }

                        }



                    }


                })
        }


    }
    @Subscribe(sticky = true)
    internal fun EventFirmaID(id : EventbusDataEvents.FirmaIDGonder)
    {

        firmaID=id.firmaid
        Log.e("Firma ID ALDIM","asdda "+firmaID)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }


}
