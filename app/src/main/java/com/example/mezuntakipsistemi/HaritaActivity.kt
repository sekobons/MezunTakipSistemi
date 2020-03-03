package com.example.mezuntakipsistemi

import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HaritaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_harita)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun Finit() {
        var adres = intent.getStringExtra("firma_adres")
        var firma_adi = intent.getStringExtra("firma_adi")
        var adresler  : List<Address> ? =null
        var geocoder =Geocoder(this@HaritaActivity)
        adresler =geocoder.getFromLocationName(adres,1)
        var adresNesne =adresler.get(0)
        var latLng = LatLng(adresNesne.latitude,adresNesne.longitude)
        mMap.addMarker(MarkerOptions().position(latLng).title(firma_adi))
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))




    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        Finit()
    }
}
