package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.mezuntakipsistemi.Adapter.IlanlarRecyclerViewAdapter
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.Ilanlar
import com.example.mezuntakipsistemi.Fragment.IlanEkleFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_isilanlari.*

class IsilanlariActivity : AppCompatActivity() {
        var dbRef : DatabaseReference ? =null
        var myAdapter : IlanlarRecyclerViewAdapter ? =null
        var myUniID : String  ? =null
        var set : HashSet<String> ? =null
        var tumIlanlar :ArrayList<Ilanlar> ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isilanlari)
        kisiUniIDBul()
        Finit()
        ilanlariGetir()
    }

     fun ilanlariGetir() {
         if(tumIlanlar==null)
         {
             tumIlanlar=ArrayList()
             set= HashSet()
         }

         dbRef!!.child("İs_İlanları").addListenerForSingleValueEvent(object : ValueEventListener
         {
             override fun onCancelled(p0: DatabaseError) {

             }

             override fun onDataChange(p0: DataSnapshot) {
                 for(ilan in p0.children)
                 {
                     var ilanNesne = Ilanlar()
                     var ilanlar = ilan.getValue(Ilanlar::class.java)
                     if(ilanlar?.olusturanUni_id.equals(myUniID))
                     {
                         Log.e("Ayni","Ayni üni")
                         if(!set!!.contains(ilanlar?.ilan_key))
                         {
                             set!!.add(ilanlar?.ilan_key!!)
                             ilanNesne.firma_adi=ilanlar?.firma_adi
                             ilanNesne.olusturulma_tarihi=ilanlar?.olusturulma_tarihi
                             ilanNesne.is_pozisyon=ilanlar?.is_pozisyon
                             ilanNesne.ilan_key=ilanlar?.ilan_key
                             tumIlanlar!!.add(ilanNesne)
                             myAdapter?.notifyDataSetChanged()
                         }


                     }


                     adapterHazirla()
                 }
             }


         })

    }

    private fun adapterHazirla() {
        if(tumIlanlar?.size!=0)
        {
            textView49.visibility= View.VISIBLE
            textView55.visibility=View.VISIBLE
            textView56.visibility=View.VISIBLE
        }
        myAdapter= IlanlarRecyclerViewAdapter(this,tumIlanlar!!)
        ilanlarRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        ilanlarRecy.layoutManager=linearLayoutManager
    }

    private fun Finit() {
        btnIlanEkle.setOnClickListener {
            var dialog = IlanEkleFragment()
            dialog.show(supportFragmentManager,"asd")

        }
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
