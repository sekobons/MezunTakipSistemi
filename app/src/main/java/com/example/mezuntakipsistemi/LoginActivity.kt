package com.example.mezuntakipsistemi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var mAuthListener : FirebaseAuth.AuthStateListener ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AuthListenerBaslat()
        init()
    }
    private fun AuthListenerBaslat() {
        mAuthListener = object : FirebaseAuth.AuthStateListener
        {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici=p0.currentUser
                if(kullanici?.uid==null || kullanici?.uid.equals("Ks5HjB5i7tVuFtdn7a4rksmoDjS2"))
                {
                    Toast.makeText(this@LoginActivity,"Kullanici yok",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    var intent= Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }


        }

    }



    private fun init() {
        btnYoneticiGiris.setOnClickListener {
            var intent=Intent(this,YoneticiLogin::class.java)
            startActivity(intent)
        }

        btnGiris.setOnClickListener {
            if(!etMail.text.isNullOrEmpty() && !etSifre.text.isNullOrEmpty())
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(etMail.text.toString(),etSifre.text.toString())
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>
                    {
                        override fun onComplete(p0: Task<AuthResult>) {
                            if(p0.isSuccessful)
                            {
                                Toast.makeText(this@LoginActivity,"Basariyla giris yapildli",Toast.LENGTH_SHORT).show()
                                var intent= Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else
                            {
                                Toast.makeText(this@LoginActivity,"Hatali giris" +p0.exception?.message.toString(),Toast.LENGTH_SHORT).show()
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
