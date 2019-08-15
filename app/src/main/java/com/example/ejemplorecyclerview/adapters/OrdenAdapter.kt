package com.example.ejemplorecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemplorecyclerview.R
import com.example.ejemplorecyclerview.listeners.RecyclerOrdenListener
import com.example.ejemplorecyclerview.models.Orden
import kotlinx.android.synthetic.main.recycler_ordenes.view.*

class OrdenAdapter (private val ordenes: List<Orden>, private val listener: RecyclerOrdenListener):
    RecyclerView.Adapter<OrdenAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_ordenes,parent,false))

    override fun getItemCount() = ordenes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(ordenes[position], listener)


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orden: Orden, listener: RecyclerOrdenListener) = with(itemView){
            textViewTitle.text = orden.titulo
            textViewStatus.text = orden.estado
            textViewFecha.text = orden.fecha
            setOnClickListener { listener.onClick(orden, orden.id) }
        }
    }
}


