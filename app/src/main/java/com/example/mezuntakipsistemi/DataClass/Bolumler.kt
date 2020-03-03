package com.example.mezuntakipsistemi.DataClass

class Bolumler {
    var bolum_adi : String ? =null
    var bolum_id : String ? =null
    constructor(){
    }
    constructor(bolum_adi :String,bolum_id : String)
    {
        this.bolum_adi=bolum_adi
        this.bolum_id=bolum_id
    }
}