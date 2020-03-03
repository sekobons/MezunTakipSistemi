package com.example.mezuntakipsistemi.DataClass

class Yonetici {
    var yonetici_id : String ?= null
    var yonetici_ad : String ? =null
    var yonetici_soyad : String ? =null
    var yonetici_unvan : String ? =null
    constructor(){}
    constructor(yonetici_id : String,yonetici_ad : String,yonetici_soyad : String,yonetici_unvan : String)
    {
        this.yonetici_ad=yonetici_ad
        this.yonetici_id=yonetici_id
        this.yonetici_soyad=yonetici_soyad
        this.yonetici_unvan=yonetici_unvan
    }
}