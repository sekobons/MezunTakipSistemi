package com.example.mezuntakipsistemi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_yonetici_login2.*

class YoneticiLogin : AppCompatActivity() {
    var mAuthListener : FirebaseAuth.AuthStateListener ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yonetici_login2)
        AuthListenerBaslat()
        Finit()
    }

    private fun AuthListenerBaslat() {
        mAuthListener = object : FirebaseAuth.AuthStateListener
        {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici=p0.currentUser
                if(kullanici?.uid==null)
                {
                    Toast.makeText(this@YoneticiLogin,"Kullanici yok",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    var intent= Intent(this@YoneticiLogin,YoneticiActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }


        }

    }

    private fun Finit() {
        btnYoneticiGir.setOnClickListener {
            if(!etyoneticiMail.text.isNullOrEmpty() && !etYoneticiSifre.text.isNullOrEmpty())
            {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(etyoneticiMail.text.toString(),etYoneticiSifre.text.toString())
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>
                    {
                        override fun onComplete(p0: Task<AuthResult>) {
                            if(p0.isSuccessful)
                            {
                                Toast.makeText(this@YoneticiLogin,"Basariyla giris yapildli", Toast.LENGTH_SHORT).show()
                                var intent= Intent(this@YoneticiLogin,YoneticiActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else
                            {
                                Toast.makeText(this@YoneticiLogin,"Hatali giris" +p0.exception?.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                    })

            }
        }

    }
    override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener!!)
    }
}
