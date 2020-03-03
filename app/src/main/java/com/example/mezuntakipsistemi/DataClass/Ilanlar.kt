package com.example.mezuntakipsistemi.DataClass

class Ilanlar {
    var firma_adi : String ? =null
    var is_pozisyon : String ? =null
    var askerlik_durumu : String ? =null
    var is_tanimi : String ? =null
    var egitim_durumu : String ? =null
    var calisma_sekli : String ? =null
    var nitelikler : String ? =null
    var olusturan_id : String ? =null
    var olusturanUni_id : String ? =null
    var olusturulma_tarihi : String ? =null
    var ilan_key : String ? =null

    constructor(){
    }
    constructor(firma_adi : String,is_pozisyon : String,askerlik_durumu : String,is_tanimi : String,egitim_durumu : String,
                calisma_sekli : String,nitelikler : String,olusturan_id : String , olusturanUni_id : String,olusturma_tarihi : String,
                ilan_key : String)
    {
        this.firma_adi=firma_adi
        this.is_pozisyon=is_pozisyon
        this.askerlik_durumu=askerlik_durumu
        this.is_tanimi=is_tanimi
        this.calisma_sekli=calisma_sekli
        this.nitelikler=nitelikler
        this.egitim_durumu=egitim_durumu
        this.olusturanUni_id=olusturanUni_id
        this.olusturan_id=olusturan_id
        this.olusturulma_tarihi=olusturma_tarihi
        this.ilan_key=ilan_key
    }
}