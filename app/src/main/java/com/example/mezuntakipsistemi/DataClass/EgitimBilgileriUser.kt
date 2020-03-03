package com.example.mezuntakipsistemi.DataClass

class EgitimBilgileriUser {
    var universite: String? = null
    var bolum: String? = null
    var ogretimTuru: String? = null
    var girisYili: String? = null
    var mezuniyetYili: String? = null
    var diplomaNotu: String? = null
    var calismaDurumu: String? = null
    var universite_id : String ? = null
    var bolum_id : String ? = null
    var lisansTuru : String  ? =null


    constructor() {
    }

    constructor(
        universite: String,
        bolum: String,
        ogretimTuru: String,
        girisYili: String,
        mezuniyetYili: String,
        diplomaNotu: String,
        calismaDurumu: String
    ) {
        this.universite = universite
        this.bolum = bolum
        this.ogretimTuru = ogretimTuru
        this.girisYili = girisYili
        this.mezuniyetYili = mezuniyetYili
        this.diplomaNotu = diplomaNotu
        this.calismaDurumu = calismaDurumu
        this.universite_id=universite_id
        this.bolum_id=bolum_id
        this.lisansTuru=lisansTuru

    }
}