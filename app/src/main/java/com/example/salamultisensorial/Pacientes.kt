package com.example.salamultisensorial

class Pacientes {
    var dni:String = ""
        get() {
            return field
        }
        set(nuevoDni) {
            field = nuevoDni
        }

    var nombre:String
        get() {
            return field
        }
        set(nuevoNombre) {
            field = nuevoNombre
        }

    var fechaDeNacimiento = ""
    var diagnostico = ""

    constructor(nombre:String, fechaDeNacimiento:String, diagnostico:String){
        this.diagnostico = diagnostico
        this.fechaDeNacimiento = fechaDeNacimiento
        this.nombre = nombre
    }

    constructor(dni:String?, nombre:String){
        this.dni = dni.toString()
        this.nombre = nombre
    }

    override fun toString(): String {
        return nombre
    }
}