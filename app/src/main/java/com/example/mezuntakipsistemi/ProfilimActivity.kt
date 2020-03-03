package com.example.mezuntakipsistemi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mezuntakipsistemi.Adapter.DeneyimlerRecyAdapter

import com.example.mezuntakipsistemi.DataClass.EgitimBilgileriUser
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.DataClass.GirisCikisTarihleri
import com.example.mezuntakipsistemi.DataClass.KisiselBilgilerUser
import com.example.mezuntakipsistemi.Fragment.ProfilResmiYukleFragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profilim.*
import java.io.ByteArrayOutputStream


class ProfilimActivity : AppCompatActivity(),ProfilResmiYukleFragment.onProfilResimListener {
    var dbRef : DatabaseReference ? =null
    var myAdapter : DeneyimlerRecyAdapter ? =null
    var Deneyimler : ArrayList<Firmalar> ?= null
    var set : HashSet<String> ? =null
    var izinlerverildi = false
    lateinit var fragment : ProfilResmiYukleFragment
    var galeridengelenURI : Uri?=null
    var kameradancekilenbitmap : Bitmap?=null
    var mbyte=1000000.toDouble()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilim)



        Finit()

    }
    override fun getResimYolu(resimPath: Uri?) {
        galeridengelenURI=resimPath
        Picasso.get().load(galeridengelenURI).resize(200,185).into(xx)
    }


    override fun getResimBitmap(bitmap: Bitmap) {
        kameradancekilenbitmap=bitmap

        xx.setImageBitmap(kameradancekilenbitmap)
    }
    // worker thread ile yapılacak asynctask sayesinde
    inner class BackgroundResimCompress : AsyncTask<Uri, Double, ByteArray>
    {
        var mybitmap:Bitmap?=null
        constructor(){}
        constructor(bitmap : Bitmap)
        {
            if(bitmap!=null)
            {
                mybitmap=bitmap
            }
        }
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: Uri?): ByteArray {
            // galeriden seçilmiş demek ki
            if(mybitmap==null)
            {
                //kullanıcının galeriden seçtiği uri bilgisini bitmape dönüştürüyor
                mybitmap= MediaStore.Images.Media.getBitmap(this@ProfilimActivity.contentResolver,params[0])
                Log.e("BEN","Orjinal resim : "+(mybitmap!!.byteCount).toDouble()/mbyte)
            }
            //resim sıkıştırma işlemi
            var resimBytes:ByteArray?=null
            for(i in 1..5)
            {
                resimBytes=convertBitmapByte(mybitmap,100/i)
                publishProgress(resimBytes!!.size.toDouble())
            }
            return resimBytes!!

        }


        private fun convertBitmapByte(bitmap: Bitmap?, i: Int): ByteArray? {
            var stream= ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG,i,stream)
            return stream.toByteArray()
        }

        override fun onProgressUpdate(vararg values: Double?) {
            super.onProgressUpdate(*values)

        }
        //do in backgrounddan gelen değer result
        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            uploadResimtoFirebase(result)
        }

    }
    private fun uploadResimtoFirebase(result: ByteArray?) {
       // progressGoster()
        var storageReferans = FirebaseStorage.getInstance().getReference()
        var resimeklenecekyer =
            storageReferans.child("images/users" + FirebaseAuth.getInstance().currentUser?.uid + "/profil_resim")
        var uploadTask = resimeklenecekyer.putBytes(result!!)
        uploadTask.addOnFailureListener {
            Toast.makeText(this@ProfilimActivity, "Upload hata", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(this@ProfilimActivity, "Upload başarili", Toast.LENGTH_SHORT).show()
        }
        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it

                }
            }
            return@Continuation resimeklenecekyer.downloadUrl

        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Toast.makeText(
                    this@ProfilimActivity,
                    "Resim Yolu : " + downloadUri.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                FirebaseDatabase.getInstance().reference
                    .child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .child("Kisisel_Bilgiler").child("profil_resmi")
                    .setValue(downloadUri.toString())
               // progressGizle()

            } else {
                // Handle failures
                // ...
            }
        }

    }

    private fun Finit() {
        tvFotoDegis.setOnClickListener {
            if(izinlerverildi)
            {

                var dialog=ProfilResmiYukleFragment()
                dialog.show(supportFragmentManager,"tag")
                btnKaydetKP.visibility=View.VISIBLE


            }else
            {

                izinleriiste()
               // btnKaydetKP.visibility=View.VISIBLE


            }

        }
        btnKaydetKP.setOnClickListener {

            if(galeridengelenURI!=null)
            {
                fotografCompressed(galeridengelenURI!!)
            }
            else if(kameradancekilenbitmap!=null)
            {
                fotografCompressed(kameradancekilenbitmap!!)
            }
            btnKaydetKP.visibility=View.INVISIBLE

        }
            ekleimage.setOnClickListener {
            var intent= Intent(this,FirmalarimActivity::class.java)
            startActivity(intent)
        }
        if(Deneyimler==null)
        {
            Deneyimler=ArrayList()
            set= HashSet()
        }


        Log.e("tag","asd"+FirebaseAuth.getInstance().currentUser?.uid)
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef!!.child("kullanici").child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {


                }

                override fun onDataChange(p0: DataSnapshot) {
                  for(user in p0.children)
                  {
                     if(user.key.equals("Egitim_Bilgileri"))
                     {
                      var egitimUser=user.getValue(EgitimBilgileriUser::class.java)

                         tvBolum.text=egitimUser?.bolum
                         tvLisansYili.text=egitimUser?.girisYili
                         tvMezunYili.text=egitimUser?.mezuniyetYili


                         tvCalismaDurumu.text=egitimUser?.calismaDurumu
                       //  tvLisansTuru.text=egitimUser?.lisansTuru
                     }
                      if(user.key.equals("Lisanslar"))
                      {
                          var str=""
                          for(lisans in user.children)
                          {

                                  var egitimNesne=lisans.getValue(GirisCikisTarihleri::class.java)
                                str=str + egitimNesne?.lisansTuru + " " + egitimNesne?.girisTarih + " " +egitimNesne?.cikisTarih +"  \n"

                          }
                          tvLisansTuru.text=str
                      }
                      if(user.key.equals("Kisisel_Bilgiler"))
                      {
                          var kisiselUser = user.getValue(KisiselBilgilerUser::class.java)
                          tvAdSoyad.text = kisiselUser?.ad_soyad
                          var foto = kisiselUser?.profil_resmi
                          if(foto!="")
                          {
                              Picasso.get().load(foto).into(xx)
                          }

                          tvYasadigiSehir.text=kisiselUser?.yasadigiSehir


                      }
                      if(user.key.equals("Firmalar"))
                      {
                        for(firma in user.children)
                        {

                            var firmaNesne = Firmalar()
                            firmaNesne.firma_id=firma.getValue(Firmalar::class.java)?.firma_id
                            if(!set!!.contains(firmaNesne.firma_id))
                            {
                                set?.add(firmaNesne?.firma_id!!)
                                firmaNesne.firma_adi=firma.getValue(Firmalar::class.java)?.firma_adi
                                firmaNesne.pozisyon=firma.getValue(Firmalar::class.java)?.pozisyon
                                firmaNesne.yetenek=firma.getValue(Firmalar::class.java)?.yetenek
                                firmaNesne.calisma_suresi=firma.getValue(Firmalar::class.java)?.calisma_suresi
                                firmaNesne.baslangicTarih=firma.getValue(Firmalar::class.java)?.baslangicTarih
                                firmaNesne.bitisTarih=firma.getValue(Firmalar::class.java)?.bitisTarih
                                Deneyimler!!.add(firmaNesne)

                            }

                        }
                          if(myAdapter==null)
                          {
                              adapterHazirla()
                          }

                          }


                  }

                }


            })

    }
    private fun adapterHazirla()
    {

        myAdapter= DeneyimlerRecyAdapter(this,Deneyimler!!)
        DeneyimlerRecy.adapter=myAdapter
        var linearLayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        DeneyimlerRecy.layoutManager=linearLayoutManager

    }
    private fun fotografCompressed(galeriURI: Uri) {
        var compressed=BackgroundResimCompress()
        compressed.execute(galeriURI) // do in background metodu tetiklenmiş oluyor
    }
    private fun fotografCompressed(kamerabitmap: Bitmap) {
        var compressed=BackgroundResimCompress(kamerabitmap)
        var uri: Uri?=null
        compressed.execute(uri)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==150)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED
                && grantResults[2]== PackageManager.PERMISSION_GRANTED)
            {
                var dialog= ProfilResmiYukleFragment()
                dialog.show(supportFragmentManager,"resimtag")
            }
            else
            {
                Toast.makeText(this,"Tüm izinleri vermelisiniz ", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun izinleriiste() {
        var izinler=arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,android.Manifest.permission.CAMERA)
        if(ContextCompat.checkSelfPermission(this,izinler[0])== PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,izinler[1])== PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,izinler[2])== PackageManager.PERMISSION_GRANTED)
        {
            izinlerverildi=true
        }
        else
            ActivityCompat.requestPermissions(this,izinler,150)
    }


}
