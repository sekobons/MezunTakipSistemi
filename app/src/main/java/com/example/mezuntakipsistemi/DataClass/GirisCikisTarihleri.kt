package com.example.mezuntakipsistemi.DataClass

class GirisCikisTarihleri {
    var girisTarih : String  ?=null
    var cikisTarih : String  ? =null
    var lisansTuru  :String ? =null
     constructor(){}
    constructor(girisTarih : String,cikisTarih : String,lisansTuru : String)
    {
        this.cikisTarih=cikisTarih
        this.girisTarih=girisTarih
        this.lisansTuru=lisansTuru
    }
}