package com.example.mezuntakipsistemi.Adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mezuntakipsistemi.DataClass.Firmalar
import com.example.mezuntakipsistemi.EventBus.EventbusDataEvents
import com.example.mezuntakipsistemi.FirmalarimActivity
import com.example.mezuntakipsistemi.Fragment.FirmaDuzenleDialogFragment
import com.example.mezuntakipsistemi.Fragment.FirmaEkleDialogFragment
import com.example.mezuntakipsistemi.R
import kotlinx.android.synthetic.main.tek_satir_deneyimler.view.*
import kotlinx.android.synthetic.main.tek_satir_firma.view.*
import org.greenrobot.eventbus.EventBus

import java.util.ArrayList

class FirmalarimRecyclerViewAdapter(Context : AppCompatActivity, firmalarim: ArrayList<Firmalar>) : RecyclerView.Adapter<FirmalarimRecyclerViewAdapter.FirmalarimHolder>() {
      var myFirma = firmalarim
      var myContext = Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FirmalarimRecyclerViewAdapter.FirmalarimHolder {
                var inflater = LayoutInflater.from(p0.context)
                var tekSatirDeneyim=inflater.inflate(R.layout.tek_satir_firma,p0,false)
        return FirmalarimHolder(tekSatirDeneyim)
    }

    override fun getItemCount(): Int {
        return myFirma.size
    }

    override fun onBindViewHolder(p0: FirmalarimRecyclerViewAdapter.FirmalarimHolder, p1: Int) {
        var oAnkiFirma=myFirma.get(p1)
        p0.setData(oAnkiFirma,p1)
    }
    inner class FirmalarimHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!)
    {
        var tekSatirLayout=itemView as ConstraintLayout
        var adi = tekSatirLayout.tvFirmaAdı

        fun setData(oAnkiFirma : Firmalar,position: Int)
        {
            tekSatirLayout.setOnClickListener {
                var dialog = FirmaDuzenleDialogFragment()
                dialog.show(myContext.supportFragmentManager,"asd")
                EventBus.getDefault().postSticky(EventbusDataEvents.FirmaIDGonder(oAnkiFirma.firma_id!!))
            }
              adi.text=oAnkiFirma.firma_adi
        }



    }
}