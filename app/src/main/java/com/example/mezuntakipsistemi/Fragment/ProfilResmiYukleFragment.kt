package com.example.mezuntakipsistemi.Fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.mezuntakipsistemi.R

class ProfilResmiYukleFragment : DialogFragment() {
    interface onProfilResimListener  //fragmentten activityde fotoyu aktarmak için interface kullandık
    {
        fun getResimYolu(resimPath: Uri?)
        fun getResimBitmap(bitmap: Bitmap)

    }
    lateinit var mProfilResmiListener: onProfilResimListener
    lateinit var galeridensec : TextView
    lateinit var kameradancek : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(R.layout.fragment_profil_resmi_yukle, container, false)
        galeridensec=view.findViewById(R.id.tvGaleridenSec)
        kameradancek=view.findViewById(R.id.tvKamerdanCek)

        galeridensec.setOnClickListener {
            var intent= Intent(Intent.ACTION_GET_CONTENT) // resimleri almak için
            intent.type="image/*"
            startActivityForResult(intent,100)
        }
        kameradancek.setOnClickListener {
            var intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,200)
        }





        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==100 && resultCode== Activity.RESULT_OK && data!=null)
        {
            var galeridensecilen=data.data
            mProfilResmiListener.getResimYolu(galeridensecilen)
            dialog.dismiss()

        }
        else if(requestCode==200 && resultCode== Activity.RESULT_OK && data!=null)
        {
            var kameradancekilen : Bitmap
            kameradancekilen=data.extras.get("data") as Bitmap
            mProfilResmiListener.getResimBitmap(kameradancekilen)
            dialog.dismiss()

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    // resmi activitye yollama
    override fun onAttach(context: Context?) {
        mProfilResmiListener=activity as onProfilResimListener
        super.onAttach(context)
    }


}
