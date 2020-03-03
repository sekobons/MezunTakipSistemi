package com.example.mezuntakipsistemi

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mezuntakipsistemi.Adapter.FirmalarRecyclerViewAdapter
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_firmalar.*
import kotlinx.android.synthetic.main.activity_firmalar.view.*
import org.jsoup.Jsoup

class FirmalarActivity : AppCompatActivity(),View.OnClickListener {



    var secilen =0
        var dbRef : DatabaseReference ? =null

        var firmaListe :ArrayList<Firmalar> ? =null
        var set :HashSet<String> ? =null
        var myAdapter : FirmalarRecyclerViewAdapter ? =null

    var sehirlerArrayList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firmalar)
        radioSehir.setOnClickListener(this)
        radioSehirveSektor.setOnClickListener(this)
        radioSektor.setOnClickListener(this)
        radioSehir.isSelected=true
        init().execute()
        Finit()
            spinnerCalisanSayisi.onItemSelectedListener =object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    firmaListe?.clear()
                    myAdapter?.notifyDataSetChanged()
                    btnListele.isEnabled=true
                }

            }
        spinnerSehir.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("GRİ","GİRDİ222")
                firmaListe?.clear()
                myAdapter?.notifyDataSetChanged()
                btnListele.isEnabled=true

            }

        }

        spinnerSektor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("GRİ","GİRDİ")
                firmaListe?.clear()
                myAdapter?.notifyDataSetChanged()
                btnListele.isEnabled=true

            }




    }




    }
    private fun Finit() {
       // kisiUniIDBul()
        btnListele.setOnClickListener {
            progressBar.visibility=View.VISIBLE
            FirmalariGetir()
           // FirmalariGetir()


        }
        progressBar.visibility=View.INVISIBLE

    }


/*
    private fun kisiUniIDBul() {
        // anlık kişinin üniIDsini bulmak
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
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
*/

    private fun FirmalariGetir() {
        if(firmaListe==null)
        {
            firmaListe=ArrayList()
            set= HashSet()

        }



       btnListele.isEnabled=false
        var knt=0




        dbRef=FirebaseDatabase.getInstance().reference
        dbRef!!.child("kullanici").addListenerForSingleValueEvent(object  :ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(user in p0.children)
                {

                    for(userAlt in user.children)
                    {

                        if(userAlt.key!!.equals("Firmalar"))
                        {

                            for(firmalar in userAlt.children)
                                {

                                    var firmaNesne=firmalar.getValue(Firmalar::class.java)
                                    Log.e("m","Gm "+spinnerCalisanSayisi.selectedItem.toString() + " "+firmaNesne?.calisan_sayisi)
                                //    if(!set!!.contains(firmaNesne?.firma_id))
                                //    {
                                  //      set!!.add(firmaNesne?.firma_id!!)

                                        if(secilen==1)
                                        {
                                            if(spinnerSehir.selectedItem.equals(firmaNesne?.firma_il) && spinnerCalisanSayisi.selectedItem.toString().equals(firmaNesne?.calisan_sayisi))
                                            {

                                                firmaListe!!.add(firmaNesne!!)
                                                myAdapter?.notifyDataSetChanged()

                                            }

                                        }
                                        else if(secilen==2)
                                        {
                                            if(spinnerSektor.selectedItem.equals(firmaNesne?.firma_sektoru)&& spinnerCalisanSayisi.selectedItem.toString().equals(firmaNesne?.calisan_sayisi) )
                                            {

                                                firmaListe!!.add(firmaNesne!!)
                                                myAdapter?.notifyDataSetChanged()
                                            }

                                        }
                                        else if (secilen==3)
                                        {
                                            if(spinnerSektor.selectedItem.equals(firmaNesne?.firma_sektoru)
                                                && spinnerSehir.selectedItem.equals(firmaNesne?.firma_il) &&  spinnerCalisanSayisi.selectedItem.toString().equals(firmaNesne?.calisan_sayisi))
                                            {

                                                firmaListe!!.add(firmaNesne!!)
                                                myAdapter?.notifyDataSetChanged()
                                            }

                                        }


                                        AdapteraHazirla()
                                //    }


                                }

                        }
                      //  progressBar.visibility=View.INVISIBLE
                    }


                }
                progressBar.visibility=View.INVISIBLE

            }


        })





    }
    private fun AdapteraHazirla()
    {
            Log.e("size","size" +firmaListe!!.size)
        progressBar.visibility=View.INVISIBLE
            myAdapter= FirmalarRecyclerViewAdapter(this,firmaListe!!)
            firmalarRecy.adapter=myAdapter
            var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            firmalarRecy.layoutManager=linearLayoutManager

    }

    override fun onClick(v: View?) {

         var vId = radioGrup1.checkedRadioButtonId

        when(vId)
        {

            R.id.radioSehir ->
            {
                spinnerSehir.visibility=View.VISIBLE
                spinnerSektor.visibility=View.INVISIBLE
                tvCalisanSayisi.visibility=View.VISIBLE
                spinnerCalisanSayisi.visibility=View.VISIBLE
                secilen=1
                btnListele.isEnabled=true



               firmaListe?.clear()
                myAdapter?.notifyDataSetChanged()

            }

            R.id.radioSektor ->{
                spinnerSehir.visibility=View.INVISIBLE
                spinnerSektor.visibility=View.VISIBLE
                tvCalisanSayisi.visibility=View.VISIBLE
                spinnerCalisanSayisi.visibility=View.VISIBLE
                secilen=2
                btnListele.isEnabled=true
              firmaListe?.clear()
                myAdapter?.notifyDataSetChanged()
            }
            R.id.radioSehirveSektor -> {
                spinnerSehir.visibility=View.VISIBLE
                spinnerSektor.visibility=View.VISIBLE
                tvCalisanSayisi.visibility=View.VISIBLE
                spinnerCalisanSayisi.visibility=View.VISIBLE
                btnListele.isEnabled=true
                secilen=3

               firmaListe?.clear()
                myAdapter?.notifyDataSetChanged()


            }

        }
    }

    inner class init : AsyncTask<Void, Void, Void>() {

        var myAdapterS = ArrayAdapter<String>(this@FirmalarActivity, R.layout.support_simple_spinner_dropdown_item, sehirlerArrayList)


        override fun doInBackground(vararg params: Void?): Void? {
            var doc = Jsoup.connect("https://www.derszamani.net/turkiye-81-il-listesi-ile-plaka-ve-telefon-kodlari.html")
                .get()
            Log.e("ÇEKİYORUm", "BİLGİYİ")
            var elements = doc.select("td[width=\"143\"]")

            var x = 0
            for (i in elements) {
                x++
                if (x >1)
                    sehirlerArrayList.add(i.text())

            }



            myAdapterS.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)


            return null

        }


        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            spinnerSehir.adapter = myAdapterS
          //  Finit()

        }
    }
}


