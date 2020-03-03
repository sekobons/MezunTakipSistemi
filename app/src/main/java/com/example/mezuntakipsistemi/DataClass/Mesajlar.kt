package com.example.mezuntakipsistemi.DataClass

class Mesajlar {
    var mesaj : String ? =null
    var tarih : String ? =null
    var mesaj_id : String ? =null
    var kullanici_id : String ? =null

    constructor(){}
    constructor(mesaj : String,tarih : String,mesaj_id : String,kullanici_id : String)
    {
        this.mesaj=mesaj
        this.tarih=tarih
        this.mesaj_id=mesaj_id
        this.kullanici_id=kullanici_id
    }
}