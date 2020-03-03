package com.example.mezuntakipsistemi.DataClass

class Firmalar {
    var firma_adi : String   ?=null
    var firma_sektoru : String ? =null
    var firma_adres : String ? =null
    var firma_website : String ? =null
    var firma_telNo : String ? =null
    var firma_id : String ? =null
    var firma_il : String ? =null
    var calisan_sayisi : String ? =null
    var pozisyon : String ? =null
    var calisiyor_mu : String ? =null
    var calisma_suresi : String ? =null
    var yetenek : String ? =null
    var baslangicTarih : String ? =null
    var bitisTarih : String ? =null
    constructor(){

    }
    constructor(firma_adi : String,firma_sektoru : String,firma_adres : String,firma_website : String,firma_telNo : String,calisma_suresi : String,yetenek : String
    ,pozisyon : String,calisiyor_mu : String,calisan_sayisi : String,baslangicTarih : String,bitisTarih : String)
    {
        this.calisiyor_mu=calisiyor_mu
        this.firma_adi=firma_adi
        this.firma_adres=firma_adres
        this.firma_sektoru=firma_sektoru
        this.firma_telNo=firma_telNo
        this.firma_website=firma_website
        this.firma_id=firma_id
        this.firma_il=firma_il
        this.calisma_suresi=calisma_suresi
        this.yetenek=yetenek
        this.pozisyon=pozisyon
        this.calisan_sayisi=calisan_sayisi
        this.baslangicTarih=baslangicTarih
        this.bitisTarih=bitisTarih
    }
}