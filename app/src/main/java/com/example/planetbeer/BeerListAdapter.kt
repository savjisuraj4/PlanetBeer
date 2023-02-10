package com.example.planetbeer

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class BeerListAdapter(mainActivity: MainActivity,arrayList: ArrayList<BeerInfo>):
    RecyclerView.Adapter<ViewHolder>() {
    var mainActivity: MainActivity
    var arrayList: ArrayList<BeerInfo>
    lateinit var viewHolder: ViewHolder
    init {
        this.mainActivity=mainActivity
        this.arrayList=arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.beer_view, parent, false)
        viewHolder= ViewHolder(itemView)
        try {
            viewHolder.setOnCLickListener(object : ViewHolder.ClickListener {
                override fun setOnItemClick(view: View, position: Int) {
                    mainActivity.startBeerFullInfo(arrayList[position].id)
                }

            })
        }catch (e:Exception){
            Toast.makeText(mainActivity,e.message,Toast.LENGTH_LONG).show()

        }

        return viewHolder
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.beerName?.text= arrayList[position].beerName
        holder.tagLine?.text=Html.fromHtml("<font color=${Color.RED}>tagline</font>" +
                "<font>: ${arrayList[position].tagLine}</font>")
        val typeface = Typeface.createFromAsset(mainActivity.assets, "fonts/TwCenClassMTStd-Regular.otf")
        holder.beerName?.typeface = typeface

    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

}