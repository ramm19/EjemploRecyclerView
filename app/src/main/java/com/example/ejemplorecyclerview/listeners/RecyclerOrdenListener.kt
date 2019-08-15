package com.example.ejemplorecyclerview.listeners

import com.example.ejemplorecyclerview.models.Orden

abstract class RecyclerOrdenListener{
    abstract fun onClick(orden: Orden, posicion: String)
}