package com.example.music

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

const val EXTRA_MESSAGE = "com.example.music.MESSAGE"

class MainActivity : AppCompatActivity() {

    private val dbName: String = "ArtistDB"
    private val tableName: String = "ArtistTable"
    private val dbVersion: Int = 1
    private var arrayListId: ArrayList<String> = arrayListOf()
    private var arrayListName: ArrayList<String> = arrayListOf()
    private var arrayListBitmap: ArrayList<Bitmap> = arrayListOf()

    private lateinit var editId: EditText
    private lateinit var editName: EditText
    private lateinit var imageView: ImageView

    private lateinit var buttonPicture: Button
    private lateinit var buttonSelect: Button
    private lateinit var buttonInsert: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button

    private lateinit var artistDBAdapter: ArtistDBAdapter
    private lateinit var listView: ListView

    private val requestCodeForPicture = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editId = findViewById(R.id.editId)
        editName = findViewById(R.id.editName)
        imageView = findViewById(R.id.imageView)

        buttonPicture = findViewById(R.id.buttonPicture)
        buttonPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,requestCodeForPicture)
        }

        buttonSelect = findViewById(R.id.buttonSelect)
        buttonSelect.setOnClickListener {
            selectData()
            artistDBAdapter.idList = arrayListId
            artistDBAdapter.nameList = arrayListName
            artistDBAdapter.imageList = arrayListBitmap
            artistDBAdapter.notifyDataSetChanged()
        }

        buttonInsert = findViewById(R.id.buttonInsert)
        buttonInsert.setOnClickListener {
            val bitmapDrawable = imageView.drawable as BitmapDrawable?
            if(bitmapDrawable != null){
                insertData(editId.text.toString(),editName.text.toString(),bitmapDrawable.bitmap)
            }
        }

        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val bitmapDrawable = imageView.drawable as BitmapDrawable?
            if(bitmapDrawable != null){
                updateData(editId.text.toString(),editName.text.toString(),bitmapDrawable.bitmap)
            }
        }

        buttonDelete = findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            deleteData(editId.text.toString())
        }

        buttonDelete = findViewById(R.id.buttonGo)

        artistDBAdapter = ArtistDBAdapter(this)
        listView = findViewById(R.id.listView)
        listView.adapter = artistDBAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            editId.setText(arrayListId.get(position), TextView.BufferType.NORMAL)
            editName.setText(arrayListName.get(position),TextView.BufferType.NORMAL)
            imageView.setImageBitmap(arrayListBitmap.get(position))
        }
    }

    class ArtistDBHelper(context: Context,databaseName:String,factory: SQLiteDatabase.CursorFactory?,version: Int) :
            SQLiteOpenHelper(context,databaseName,factory,version){

        override fun onCreate(database: SQLiteDatabase?) {
            database?.execSQL("create table if not exists ArtistTable (id text primary key, name text, image BLOB)")
        }

        override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            if (oldVersion < newVersion) {
                database?.execSQL("alter table ArtistTable add column deleteFlag integer default 0")
            }
        }
    }

    private fun selectData(){
        try{
            arrayListId.clear();arrayListName.clear();arrayListBitmap.clear()

            val dbHelper = ArtistDBHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.readableDatabase

            val sql = "select id,name,image from " + tableName

            val cursor = database.rawQuery(sql,null)

            if (cursor.count > 0){
                cursor.moveToFirst()
                while (!cursor.isAfterLast){
                    arrayListId.add(cursor.getString(0))
                    arrayListName.add(cursor.getString(1))
                    val blob: ByteArray = cursor.getBlob(2)
                    val bitmap = BitmapFactory.decodeByteArray(blob,0,blob.size)
                    arrayListBitmap.add(bitmap)
                    cursor.moveToNext()
                }
            }
        }catch (exception: Exception){
            Log.e("selectData",exception.toString())
        }
    }

    private fun insertData(id: String, name: String, bitmap: Bitmap){
        try{
            val dbHelper = ArtistDBHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("id",id)
            values.put("name",name)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            values.put("image",bytes)

            database.insertOrThrow(tableName,null,values)
        }catch (exception: Exception){
            Log.e("insertData",exception.toString())
        }
    }

    private fun updateData(whereId: String, newName: String, newBitmap: Bitmap){
        try {
            val dbHelper = ArtistDBHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("name",newName)
            val byteArrayOutputStream = ByteArrayOutputStream()
            newBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            values.put("image",bytes)

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId)
            database.update(tableName,values,whereClauses,whereArgs)
        }catch (exception: Exception){
            Log.e("updateData",exception.toString())
        }
    }

    private fun deleteData(whereId: String){
        try {
            val dbHelper = ArtistDBHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId)
            database.delete(tableName,whereClauses,whereArgs)
        }catch (exception: Exception){
            Log.e("deleteData",exception.toString())
        }
    }

    fun firstWindow(view: View){

        val intent = Intent(this,FirstActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE,"FIRST")
        }

        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeForPicture && resultCode == Activity.RESULT_OK){
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
            if (inputStream != null){
                inputStream.close()
            }
        }
    }
}
