package com.example.mezuntakipsistemi.DataClass

class OzelMesaj {
    var yazan_ID : String ? =null
    var alici_ID : String ? =null
    var mesajlar : ArrayList<Mesajlar> ? =null
    constructor(){}
    constructor(yazan_ID : String,alici_ID : String,mesajlar : ArrayList<Mesajlar>){
            this.yazan_ID=yazan_ID
            this.alici_ID=alici_ID
            this.mesajlar=mesajlar
    }
}