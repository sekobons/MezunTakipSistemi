package com.example.mezuntakipsistemi
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mezuntakipsistemi.DataClass.Yonetici
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_yonetici.*

class YoneticiActivity : AppCompatActivity() {
    var myAuthStateListener : FirebaseAuth.AuthStateListener ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yonetici)
        initAuthmyListener()
        Finit()

    }

    private fun Finit() {
            var dbRef = FirebaseDatabase.getInstance().reference
            dbRef.child("yonetici").orderByKey().equalTo(FirebaseAuth.getInstance().currentUser?.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                     for(user in p0.children)
                     {
                         var yonetici = user.getValue(Yonetici::class.java)
                         tvYoneticiAd.text=yonetici?.yonetici_ad
                         tvYoneticiSoyad.text=yonetici?.yonetici_soyad
                         tvYoneticiUnvan.text=yonetici?.yonetici_unvan
                     }

                    }


                })
        btnCik.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }

        btnYeniEkle.setOnClickListener {
            if(!etEklenecekBolum.text.isNullOrEmpty())
            {
                var ref = FirebaseDatabase.getInstance().reference
                var key =ref.push().key
                ref.child("bolumler").child(key!!).child("bolum_adi").setValue(etEklenecekBolum.text.toString())
                Toast.makeText(this,"Bölüm başarıyla eklendi",Toast.LENGTH_LONG).show()
                etEklenecekBolum.setText("")

            }
        }
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
                    Toast.makeText(this@YoneticiActivity,"initmyAuthmListener tetiklendi", Toast.LENGTH_LONG).show()
                    var intent= Intent(this@YoneticiActivity,LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) //kullanıcı başka activitye geçince varolan işlemleri silmek için
                    startActivity(intent)
                    finish()
                }

            }

        }
    }


}
