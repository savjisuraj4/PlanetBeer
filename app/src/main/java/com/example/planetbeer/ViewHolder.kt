package com.example.planetbeer

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(mView: View) :RecyclerView.ViewHolder(mView) {
    var beerName:TextView?=null
    var tagLine:TextView?=null
    private var mClickListener: ClickListener?=null
    init {
        beerName= itemView.findViewById(R.id.beerName)
        tagLine=itemView.findViewById(R.id.tagLine)
        itemView.setOnClickListener {view->
            mClickListener!!.setOnItemClick(view,adapterPosition)
        }
    }

    interface ClickListener{
        fun setOnItemClick(view: View,position:Int)
    }
    fun setOnCLickListener(clickListener: ClickListener){
        mClickListener=clickListener
    }
}