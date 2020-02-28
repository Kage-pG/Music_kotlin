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

class ArtistDBAdapter(val context: Activity): BaseAdapter() {
    val inflater: LayoutInflater get() = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
        val listLayout = inflater.inflate(R.layout.list_artistdb,null)

        var listImage = listLayout.findViewById<ImageView>(R.id.listImage)
        listImage.setImageBitmap(imageList.get(position))
        var textId = listLayout.findViewById<TextView>(R.id.textId)
        textId.text = idList.get(position)
        var textName = listLayout.findViewById<TextView>(R.id.textName)
        textName.text = nameList.get(position)

        return listLayout
    }
}