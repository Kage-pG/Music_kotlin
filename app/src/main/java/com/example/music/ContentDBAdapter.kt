package com.example.music

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.music.ui.main.PlaceholderFragment

class ContentDBAdapter(
    val context: Context,
    placeholderFragment: PlaceholderFragment
): BaseAdapter(){

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

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val listLayout = inflater.inflate(R.layout.list_content,null)

        var contentImage = listLayout.findViewById<ImageView>(R.id.contentImage)
        contentImage.setImageBitmap(imageList.get(position))
        var contentName = listLayout.findViewById<TextView>(R.id.contentName)
        contentName.text = nameList.get(position)
        return listLayout
    }
}