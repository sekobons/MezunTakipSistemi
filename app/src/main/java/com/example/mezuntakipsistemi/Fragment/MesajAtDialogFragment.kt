package com.example.mezuntakipsistemi.Fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mezuntakipsistemi.DataClass.Mesajlar
import com.example.mezuntakipsistemi.EventBus.EventbusDataEvents

import com.example.mezuntakipsistemi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*


class MesajAtDialogFragment : DialogFragment() {
        lateinit var mesaj : EditText
        lateinit var gonder : Button
        var kisiID : String ? =null
        var dbRef : DatabaseReference ? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_mesaj_at_dialog, container, false)
            mesaj=view.findViewById(R.id.etFirstMesaj)
            gonder=view.findViewById(R.id.btnGonder)
        gonder.setOnClickListener {
            if(!mesaj.text.isNullOrEmpty())
            {
                dbRef= FirebaseDatabase.getInstance().reference
                dbRef!!.child("özel_mesaj").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .child(kisiID!!).addListenerForSingleValueEvent(object : ValueEventListener
                    {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.getValue()!=null)
                            {
                                Toast.makeText(activity,"Zaten ilk mesajı atmışsınız.Mesajlar kısmından takip edebilirsiniz",
                                    Toast.LENGTH_LONG).show()

                            }
                            else
                            {

                                    var mesajlar= Mesajlar()
                                    mesajlar?.mesaj=mesaj.text.toString()
                                    mesajlar.tarih=getMesajTarihi()
                                    mesajlar?.kullanici_id= FirebaseAuth.getInstance().currentUser?.uid

                                    var mesajKey=dbRef!!.push().key
                                    dbRef!!.child("özel_mesaj").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                                        .child(kisiID!!).child("sohbet_mesajlari").child(mesajKey!!).setValue(mesajlar)
                                    dbRef!!.child("özel_mesaj").child(kisiID!!).child(FirebaseAuth.getInstance().currentUser?.uid!!).
                                        child("sohbet_mesajlari") .child(mesajKey).setValue(mesajlar)
                                    Toast.makeText(activity,"İlk mesajınız başarıyla gönderildi", Toast.LENGTH_SHORT).show()



                            }

                        }




                    })

            }


        }

        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
    private fun getMesajTarihi(): String? {
        var sdf= SimpleDateFormat("HH:mm:ss", Locale("tr"))
        return sdf.format(Date())
    }
    @Subscribe(sticky = true)
    internal fun mesajIDBulma(ID : EventbusDataEvents.MesajIdGonder)
    {
        kisiID=ID.kisi_id
        Log.e("seko","Atılacak ID : "+kisiID)
    }
}
