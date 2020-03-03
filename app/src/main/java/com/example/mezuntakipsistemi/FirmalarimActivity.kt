package com.example.mezuntakipsistemi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.mezuntakipsistemi.Adapter.FirmalarimRecyclerViewAdapter
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.EventBus.EventbusDataEvents
import com.example.mezuntakipsistemi.Fragment.FirmaDuzenleDialogFragment
import com.example.mezuntakipsistemi.Fragment.FirmaEkleDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_firmalarim.*
import org.greenrobot.eventbus.EventBus

class FirmalarimActivity : AppCompatActivity() {
        var dbRef : DatabaseReference ? =null
   var tumFirmalarim= ArrayList<Firmalar>()
    var set= HashSet<String>()
    var myAdapter : FirmalarimRecyclerViewAdapter ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firmalarim)
        Finit()
    }

    fun Finit() {
            imgEkle.setOnClickListener {
                    var dialog=FirmaEkleDialogFragment()
                dialog.show(supportFragmentManager,"ge")

            }

        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .child("Firmalar").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(firma in p0.children)
                    {

                        var firmam = Firmalar()
                        var firmaNesne=firma.getValue(Firmalar::class.java)
                        if(!set.contains(firmaNesne?.firma_id))
                        {
                            set.add(firmaNesne?.firma_id!!)
                            firmam.firma_adi=firmaNesne.firma_adi
                            firmam.firma_id=firmaNesne.firma_id
                            tumFirmalarim.add(firmam)
                        }


                    }
                        firmalariListele()
                }


            })

    }

    private fun firmalariListele() {
        myAdapter= FirmalarimRecyclerViewAdapter(this,tumFirmalarim)
        firmalarimRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        firmalarimRecy.layoutManager=linearLayoutManager
    }
    fun firmaDuzenlemek(firmaId : String)
    {
        var dialog=FirmaDuzenleDialogFragment()
        dialog.show(supportFragmentManager,"asd")
        EventBus.getDefault().postSticky(EventbusDataEvents.FirmaIDGonder(firmaId))

    }
}
