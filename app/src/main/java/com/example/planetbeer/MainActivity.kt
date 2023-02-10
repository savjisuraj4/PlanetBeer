@file:Suppress("DEPRECATION")

package com.example.planetbeer

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    var beerList = ArrayList<BeerInfo>()
    lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var beerInfo: BeerInfo
    val url = "https://api.punkapi.com/v2/beers"
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var beerListAdapter: BeerListAdapter
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            recyclerView = findViewById(R.id.recyclerView)
            linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

            recyclerView.setHasFixedSize(false)
            beerList.clear()

        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

        }
        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
            swipeRefreshLayout.isRefreshing = false
        }

        fetchData()

    }
    private fun fetchData() {
        val progressDialog=ProgressDialog(this)

        progressDialog.setMessage("Loading..")
        progressDialog.show()
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

        val request =object: JsonArrayRequest( url, { response ->
            try {
                for (i in 0..response.length().minus(1)) {
                    val id = response.getJSONObject(i).get("id").toString()
                    val name = response.getJSONObject(i).get("name").toString()
                    val tagline = response.getJSONObject(i).get("tagline").toString()
                    beerInfo = BeerInfo(id,name, tagline)
                    beerList.add(beerInfo)
                }
                progressDialog.dismiss()
                beerListAdapter = BeerListAdapter(this, beerList)
                recyclerView.adapter = beerListAdapter
            }catch (e:Exception){
                progressDialog.dismiss()
                displayError(e.message.toString())


            }
        }, { error ->
            progressDialog.dismiss()
            displayError(error.message.toString())
        }){

        }
        queue.add(request)
    }
    fun displayError(error:String){
        AlertDialog.Builder(this)
            .setIcon(R.drawable.baseline_error_24)
            .setTitle("Error")
            .setMessage(error)
            .setNeutralButton("Ok") { dialog, which -> }
            .create()
            .show()
    }
    fun startBeerFullInfo(BeerId: String) {
        val intent=Intent(applicationContext, BeerFullInfo::class.java)
        intent.putExtra("BeerId",BeerId)
        startActivity(intent)
    }
}