package com.example.salamultisensorial

class Observaciones {
    var fechaObservacion:String = ""
        get() {
            return field
        }

    var observacion:String = ""
        get() {
            return field
        }
    constructor(fechaObservacion:String?, observacion:String){
        this.fechaObservacion = fechaObservacion.toString()
        this.observacion = observacion
    }
    override fun toString(): String {
        return fechaObservacion
    }

}