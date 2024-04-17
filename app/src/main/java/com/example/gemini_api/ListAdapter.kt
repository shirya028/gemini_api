package com.example.gemini_api

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(var items:List<chatData>) : RecyclerView.Adapter<ListAdapter.myAdpterViewHolder>(){

    inner class myAdpterViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myAdpterViewHolder
    {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.text_design,parent,false)
        return myAdpterViewHolder(view);
    }

    override fun getItemCount(): Int {
        return  items.size
    }

    override fun onBindViewHolder(holder: myAdpterViewHolder, position: Int) {

        holder.itemView.apply {


            var txt1=findViewById<TextView>(R.id.textView);
            txt1.text=items[position].textData;
            var image=findViewById<ImageView>(R.id.imageView);
            var image2=findViewById<ImageView>(R.id.imageView2);
            if(items[position].userPic)
                image.setImageResource(R.drawable.img);
            else {
                image.setImageDrawable(null);
                image2.setImageResource(R.drawable.robo)
            }

            if(items[position].uri!=null) {
                var image=findViewById<ImageView>(R.id.userImg)
                image.setImageURI(items[position].uri)
            }
        }
    }
}