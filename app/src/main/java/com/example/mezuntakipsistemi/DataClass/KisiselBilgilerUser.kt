package com.example.mezuntakipsistemi.DataClass

class KisiselBilgilerUser {
    var ogrNo : String ? =null
    var ad_soyad : String ? =null
    var dogumTarihi : String ? =null
    var yasadigiSehir:  String ? =null
    var telNo : String  ? =null
    var webSite : String ? =null
    var profil_resmi : String  ? =null
    var user_id : String ? =null
    constructor(){
    }
    constructor(ogrNo : String,ad_soyad : String,dogumTarihi : String,yasadigiSehir:  String,telNo : String,webSite : String,
                profil_resmi : String,user_id : String)
    {
        this.ogrNo=ogrNo
        this.ad_soyad=ad_soyad
        this.dogumTarihi=dogumTarihi
        this.yasadigiSehir=yasadigiSehir
        this.telNo=telNo
        this.webSite=webSite
        this.profil_resmi=profil_resmi
        this.user_id=user_id
    }
}