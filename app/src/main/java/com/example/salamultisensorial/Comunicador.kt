package com.example.salamultisensorial

interface Comunicador {
    fun pasarDatosBtn(habilitaSig:Boolean, habilitaCar: Boolean)
    fun pasarDatosCb(simonP:Boolean, sonidosP:Boolean, colchonetaP:Boolean, tableroP:Boolean)
    fun completeEq(completoEq:Boolean)
    fun completeAt(completoAt:Boolean)
    fun completeMe(completoMe:Boolean)
    fun completeMFi(completoMFi:Boolean)
    fun completeDco(completoDco:Boolean)
    fun completeVi(completoVi:Boolean)
}