package com.example.mezuntakipsistemi
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mezuntakipsistemi.Adapter.OgrenciAramaRecy
import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

        var myAuthStateListener : FirebaseAuth.AuthStateListener ? =null
        var set : HashSet<String >  ? =null
        var dbRef : DatabaseReference  ?=null
        var arananKisiler : ArrayList<KisiselBilgilerUser> ?=null
        var myAdapter : OgrenciAramaRecy ? =null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAuthmyListener()
        Finit()


    }

    private fun Finit() {
     //   kisiUniIDBul()



        if(arananKisiler==null)
        {
            arananKisiler=ArrayList()
            set= HashSet()
        }

                dbRef= FirebaseDatabase.getInstance().getReference()
                arananKisiler!!.clear()
                myAdapter?.notifyDataSetChanged()
                set!!.clear()

                btnAra.setOnClickListener {
                    if(!etAranacakKisi.text.isNullOrEmpty())
                    {
                        progressArama.visibility=View.VISIBLE
                        var aranan = etAranacakKisi.text.toString().toLowerCase()
                        dbRef!!.child("kullanici").addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                for(user in p0.children)
                                {
                                    var key = user.key
                                //    Log.e("key","key "+key)
                                    if(!set!!.contains(key))
                                    {
                                        set!!.add(key!!)
                                        for(kisiBilgiler in user.children)
                                        {
                                            var myNesne = KisiselBilgilerUser()

                                            if(kisiBilgiler.key.equals("Kisisel_Bilgiler"))
                                            {

                                                var bilgiNesne = kisiBilgiler.getValue(KisiselBilgilerUser::class.java)
                                                var foto =bilgiNesne?.profil_resmi
                                                var tmp =  bilgiNesne?.ad_soyad
                                                myNesne!!.ad_soyad=bilgiNesne?.ad_soyad!!.toLowerCase()

                                                if(myNesne!!.ad_soyad!!.contains(aranan) &&  myNesne!!.ad_soyad!!.startsWith(aranan[0]))
                                                {
                                                    Log.e("Bulundu","Buluundu  :"+myNesne.ad_soyad)
                                                    myNesne.ad_soyad=tmp
                                                    if(foto!="")
                                                        myNesne.profil_resmi=foto
                                                    myNesne?.user_id=bilgiNesne?.user_id
                                                    arananKisiler!!.add(myNesne)
                                                }


                                            }



                                        }
                                    }

                                }
                                adapterHazirla()
                           //     Finit()
                                 //recy gonder


                            }


                        })
                    }
                    else
                        Toast.makeText(this@MainActivity,"Lütfen boş bırakmayınız",Toast.LENGTH_SHORT).show()



            // btnAra.isEnabled=false
                    Finit()
                }



    }

/*
    private fun kisiUniIDBul() {
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

                            break
                        }

                    }

                }


            })
    }*/

        private fun adapterHazirla()
            {
            if(arananKisiler?.size==0)
            {
                textView28.visibility=View.VISIBLE
            }
                else
                textView28.visibility=View.INVISIBLE

                progressArama.visibility=View.INVISIBLE
                myAdapter = OgrenciAramaRecy(this,arananKisiler!!)
                aramaRecy.adapter=myAdapter
                var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                aramaRecy.layoutManager=linearLayoutManager


            }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.menuCikis-> {
                cikisyap()
                return true
            }
            R.id.mesajlarMenu -> {
                var intent = Intent(this@MainActivity,MesajlarActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menuProfil -> {
                var intent=Intent(this@MainActivity,ProfilAyarlari::class.java)
                startActivity(intent)


                return true
            }
            R.id.menuProfilim ->{
                    var intent=Intent(this@MainActivity,ProfilimActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.menuFirmalar -> {
                var intent=Intent(this@MainActivity,FirmalarActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menuOgrenci -> {
                var intent=Intent(this@MainActivity,OgrenciActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menuIlanlar -> {
                var intent=Intent(this@MainActivity,IsilanlariActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun cikisyap() {
        FirebaseAuth.getInstance().signOut()
    }
    override fun onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener (myAuthStateListener!!)
        super.onStop()
        if(myAuthStateListener!=null)
        {
            FirebaseAuth.getInstance().removeAuthStateListener (myAuthStateListener!!)
        }
    }
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener  (myAuthStateListener!!)

    }
    private fun initAuthmyListener() {
        myAuthStateListener=object:FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici=p0.currentUser
                if(kullanici==null)
                {
                    Toast.makeText(this@MainActivity,"initmyAuthmListener tetiklendi", Toast.LENGTH_LONG).show()
                    var intent= Intent(this@MainActivity,LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) //kullanıcı başka activitye geçince varolan işlemleri silmek için
                    startActivity(intent)
                    finish()
                }

            }

        }
    }
}
