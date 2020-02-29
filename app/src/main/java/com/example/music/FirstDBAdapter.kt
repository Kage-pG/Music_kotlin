package com.example.music

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class FirstDBAdapter(val context: Activity): BaseAdapter() {
    val inflater: LayoutInflater
        get() = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var idList: ArrayList<String> = arrayListOf()
    var nameList: ArrayList<String> = arrayListOf()
    var imageList: ArrayList<Bitmap> = arrayListOf()

    override fun getCount(): Int{
        return idList.count()
    }

    override fun getItem(index: Int): Any{
        return idList.get(index)
    }

    override fun getItemId(index: Int): Long{
        return index.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View{
        val listLayout = inflater.inflate(R.layout.first_artist_list,null)

        var artistImage = listLayout.findViewById<ImageView>(R.id.artistImage)
        artistImage.setImageBitmap(imageList.get(position))
        var artistName = listLayout.findViewById<TextView>(R.id.artistName)
        artistName.text = nameList.get(position)
        return listLayout
    }
}