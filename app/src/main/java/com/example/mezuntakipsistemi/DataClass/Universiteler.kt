package com.example.mezuntakipsistemi.DataClass

class Universiteler {
    var universite_id : String ? =null
    var universite_adi  : String ? =null
    constructor(universite_id : String,universite_adi : String)
    {
        this.universite_adi=universite_adi
        this.universite_id=universite_id
    }
    constructor(){
    }
}