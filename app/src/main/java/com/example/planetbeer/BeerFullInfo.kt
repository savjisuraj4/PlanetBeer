@file:Suppress("DEPRECATION")

package com.example.planetbeer

import android.annotation.SuppressLint
import android.app.ProgressDialog

import android.graphics.Color
import android.os.Bundle
import android.text.Html

import android.text.Spanned

import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject


class BeerFullInfo : AppCompatActivity() {
    lateinit var beerId:String
    lateinit var beerNameInFullInfo:TextView
    lateinit var firstBrewed:TextView
    lateinit var beerImage:ImageView
    lateinit var description:TextView
    lateinit var volume:TextView
    lateinit var abv:TextView
    lateinit var ibu:TextView
    lateinit var ebc:TextView
    lateinit var target_fg:TextView
    lateinit var target_og:TextView
    lateinit var srm:TextView
    lateinit var ph:TextView
    lateinit var attenuation_level:TextView
    lateinit var boil_volume:TextView
    lateinit var ingredients:TextView
    lateinit var food_pairing:TextView
    lateinit var brewers_tips:TextView
    var url="https://api.punkapi.com/v2/beers/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_full_info)
        beerId=intent.getStringExtra("BeerId").toString()
        beerNameInFullInfo=findViewById(R.id.beerNameInFullInfo)
        firstBrewed=findViewById(R.id.firstBrewed)
        beerImage=findViewById(R.id.beerImage)
        description=findViewById(R.id.description)
        volume=findViewById(R.id.volume)
        abv=findViewById(R.id.abv)
        ibu=findViewById(R.id.ibu)
        ebc=findViewById(R.id.ebc)
        target_fg=findViewById(R.id.target_fg)
        target_og=findViewById(R.id.target_og)
        srm=findViewById(R.id.srm)
        ph=findViewById(R.id.ph)
        attenuation_level=findViewById(R.id.attenuation_level)
        boil_volume=findViewById(R.id.boil_volume)
        ingredients=findViewById(R.id.ingredients)
        food_pairing=findViewById(R.id.food_pairing)
        brewers_tips=findViewById(R.id.brewers_tips)
        fetchData()
    }

    private fun fetchData() {
        val progressDialog= ProgressDialog(this)

        progressDialog.setMessage("Loading..")
        progressDialog.show()
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val request = @SuppressLint("SetTextI18n")
        object: JsonArrayRequest( url+beerId, { response ->
            try {

                val jsonObject=response.getJSONObject(0)
                beerNameInFullInfo.text=response.getJSONObject(0).get("name").toString()
                firstBrewed.text=htmlQuery("first Brewed: ",Color.WHITE,jsonObject.get("first_brewed").toString(),Color.MAGENTA)
                description.text=htmlQuery("Description: ",Color.CYAN,jsonObject.get("description").toString(),Color.WHITE)
                abv.text="abv: "+jsonObject.get("abv").toString()
                ibu.text="ibu: "+jsonObject.get("ibu").toString()
                ebc.text="ebc: "+jsonObject.get("ebc").toString()
                Picasso.Builder(this).listener { picasso, url, exception ->
                    Toast.makeText(this, exception.message.toString(), Toast.LENGTH_LONG).show()
                }
                    .build().load(jsonObject.getString("image_url"))
                    .into(beerImage)
                val volumeText=jsonObject.getJSONObject("volume").get("value").toString()+" "+
                        jsonObject.getJSONObject("volume").get("unit").toString()
                volume.text=htmlQuery("volume: ",Color.WHITE,volumeText,Color.GREEN)

                target_fg.text="target fg: "+jsonObject.get("target_fg").toString()
                target_og.text="target og: "+jsonObject.get("target_og").toString()
                srm.text="srm: "+jsonObject.get("srm").toString()
                ph.text="ph: "+jsonObject.get("ph").toString()
                attenuation_level.text="attenuation level:"+jsonObject.get("attenuation_level").toString()
                val boilVolumeText=jsonObject.getJSONObject("boil_volume").get("value").toString()+" "+
                        jsonObject.getJSONObject("boil_volume").get("unit").toString()
                boil_volume.text=htmlQuery("boil volume: ",Color.WHITE,boilVolumeText,Color.GREEN)

                var food_pairing_text=""
                for(i in 0..jsonObject.getJSONArray("food_pairing").length().minus(1)){
                    food_pairing_text+=(i+1).toString()+"] "+jsonObject.getJSONArray("food_pairing").getString(i)
                }
                food_pairing.text=htmlQuery("food pairing: ",Color.GREEN,food_pairing_text,Color.WHITE)

                var ingredients_text=""
                ingredients_text+=getingredients_text(jsonObject,"malt")
                ingredients_text+=getingredients_text(jsonObject,"hops")
                ingredients.text=htmlQuery("Ingredients: ",Color.GREEN,ingredients_text,Color.WHITE)
                brewers_tips.text="brewers tips: "+jsonObject.get("brewers_tips").toString()
                brewers_tips.text=htmlQuery("brewers tips: ",Color.GREEN,jsonObject.get("brewers_tips").toString(),Color.WHITE)
                progressDialog.dismiss()
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
    private fun htmlQuery(value1:String, color1: Int, value2: String, color2: Int): Spanned? {
        return Html.fromHtml("<font color=${color1}>${value1}</font>" +
                "<font color=${color2}>${value2} </font>")
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
    fun getingredients_text(jsonObject: JSONObject,string: String):String{
        var ingredients_text=""
        for(i in 0..jsonObject.getJSONObject("ingredients").getJSONArray(string).length().minus(1)){
            ingredients_text+=htmlQuery("",Color.GREEN,
                jsonObject.getJSONObject("ingredients").getJSONArray(string).getJSONObject(i).getString("name")+",",Color.WHITE)
        }
        return ingredients_text
    }

}