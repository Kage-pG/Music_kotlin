package com.example.music.ui.main

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.music.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    private val dbName: String = "ContentDB"
    private val tableName: String = "ContentTable"
    private val dbVersion: Int = 1
    private var arrayListId: ArrayList<String> = arrayListOf()
    private var arrayListName: ArrayList<String> = arrayListOf()
    private var arrayListBitmap: ArrayList<Bitmap> = arrayListOf()


    private lateinit var contentDBAdapter: ContentDBAdapter

    private var this_context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_artist, container, false)
        val listView: ListView = root.findViewById(R.id.contentList) as ListView

        contentDBAdapter = ContentDBAdapter(context!!,this)

        this_context = getActivity()?.getApplicationContext()

        val dbHelper = MainActivity.ArtistDBHelper(this_context!!,"ArtistDB",null,dbVersion)
        val database = dbHelper.readableDatabase

        val sql = "select id, name, image from " + "ArtistTable"

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



        contentDBAdapter.idList = arrayListId
        contentDBAdapter.nameList = arrayListName
        contentDBAdapter.imageList = arrayListBitmap
        contentDBAdapter.notifyDataSetChanged()

        pageViewModel.text.observe(this, Observer<String> {
            listView.adapter = contentDBAdapter
        })
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}