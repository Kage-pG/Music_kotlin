package com.example.music

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FirstActivity : AppCompatActivity(){

    private val dbName: String = "ArtistDB"
    private val tableName: String = "ArtistTable"
    private val dbVersion: Int = 1
    private var arrayListId: ArrayList<String> = arrayListOf()
    private var arrayListName: ArrayList<String> = arrayListOf()
    private var arrayListBitmap: ArrayList<Bitmap> = arrayListOf()

    private lateinit var firstDBAdapter: FirstDBAdapter
    private lateinit var listView: ListView
    private var select_id: String = "1"

    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val message = intent.getStringExtra(EXTRA_MESSAGE)

        firstDBAdapter = FirstDBAdapter(this)
        listView = findViewById(R.id.artist_list)
        listView.adapter = firstDBAdapter

        val dbHelper = MainActivity.ArtistDBHelper(applicationContext,dbName,null,dbVersion)
        val database = dbHelper.readableDatabase

        val sql = "select id, name, image from " + tableName

        val cursor = database.rawQuery(sql,null)

        arrayListId.clear();arrayListName.clear();arrayListBitmap.clear()

        if(cursor.count > 0){
            cursor.moveToFirst()
            while(!cursor.isAfterLast){
                arrayListId.add(cursor.getString(0))
                arrayListName.add(cursor.getString(1))
                val blob: ByteArray = cursor.getBlob(2)
                val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
                arrayListBitmap.add(bitmap)
                cursor.moveToNext()
            }
        }

        firstDBAdapter.idList = arrayListId
        firstDBAdapter.nameList = arrayListName
        firstDBAdapter.imageList = arrayListBitmap
        firstDBAdapter.notifyDataSetChanged()

        listView.setOnItemClickListener { parent, view, position, id ->
            select_id = arrayListId.get(position)
        }
    }

    fun artistTabWindow(view: View){

        val id = select_id

        val intent = Intent(this,ArtistActivity::class.java).apply {
            putExtra("SELECT_ID",id)
        }
        startActivity(intent)
    }
}