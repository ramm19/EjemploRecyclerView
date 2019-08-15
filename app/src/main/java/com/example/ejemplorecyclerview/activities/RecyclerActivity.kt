package com.example.ejemplorecyclerview.activities

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ejemplorecyclerview.R
import com.example.ejemplorecyclerview.adapters.OrdenAdapter
import com.example.ejemplorecyclerview.listeners.RecyclerOrdenListener
import com.example.ejemplorecyclerview.models.Orden
import kotlinx.android.synthetic.main.activity_recycler.*
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

const val URL_ORDENES = "http://5b8cb3e77366ab0014a29b04.mockapi.io/ordenes"

class RecyclerActivity : AppCompatActivity() {

    private var ordenes: MutableList<Orden> = mutableListOf()

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: OrdenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        dibujarOrdenes()
    }

    private fun dibujarOrdenes() {
        requestOrdenes(this,
        {
          doRecyclerViewOrdenes()
        }, {error ->
            //Toast.makeText(this,"No existen ordenes!! o existe un error ${error}", Toast.LENGTH_LONG).show()
                showDialogErrorCallApi("No existen ordenes!! o existe un error ${error}")
        })

        /*ordenes = getOrdenesTemp()
        doRecyclerViewOrdenes()*/
    }

    private fun doRecyclerViewOrdenes() {

        recycler = recyclerViewServices
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = OrdenAdapter(ordenes, object: RecyclerOrdenListener(){
            override fun onClick(orden: Orden, posicion: String) {
                Toast.makeText(this@RecyclerActivity,"El estado de la orden ${orden.titulo} es ${orden.estado}", Toast.LENGTH_LONG).show()
            }
        })

        recycler.adapter = adapter
    }

    private fun requestOrdenes(
        context: Context,
        success: ((MutableList<Orden>) -> Unit),
        error: ((String) -> Unit)
    ) {
        if (ordenes.isEmpty()) {
            val request = JsonArrayRequest(Request.Method.GET, URL_ORDENES, null,
                { response ->
                    ordenes = parseOrdenes(response)
                    success.invoke(ordenes)
                },
                { volleyError ->
                    error.invoke(volleyError.toString())
                })

            Volley.newRequestQueue(context)
                .add(request)

        } else {
            success.invoke(ordenes)
        }
    }

    private fun parseOrdenes(jsonArray: JSONArray): MutableList<Orden> {
        val ordenesTemp = mutableListOf<Orden>()
        for (index in 0 until jsonArray.length()) {
            val ordenTemp = parseOrden(jsonArray.getJSONObject(index))
            ordenesTemp.add(ordenTemp)
        }

        return ordenesTemp
    }

    private fun parseOrden(jsonOrden: JSONObject): Orden {
        return Orden(
            jsonOrden.getString("id"),
            jsonOrden.getString("titulo"),
            jsonOrden.getString("estado"),
            jsonOrden.getString("fecha")
        )
    }

    private fun getOrdenesTemp(): ArrayList<Orden>{
        return object: ArrayList<Orden>(){
            init {
                add(Orden("1","Seattle", "Pendiente", "01/01/2019"))
                add(Orden("2","Seattle", "Pendiente", "01/01/2019"))
            }
        }
    }

    fun showDialogErrorCallApi(mensaje: String){
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("No se pueden mostrar las ordenes")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", DialogInterface.OnClickListener{ dialogInterface, i ->  }).show()
    }

}
