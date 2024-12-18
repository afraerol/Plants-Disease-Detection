package com.example.loginsqllite

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyPlantsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var emptyImageView: ImageView
    private lateinit var noDataTextView: TextView

    private lateinit var myDB: MyDatabaseHelper
    private lateinit var bookId: ArrayList<String>
    private lateinit var bookTitle: ArrayList<String>
    private lateinit var bookAuthor: ArrayList<String>
    private lateinit var bookPages: ArrayList<String>
    private lateinit var customAdapter: CustomAdapter

    companion object {
        const val ADD_BOOK_REQUEST_CODE = 1
        const val UPDATE_BOOK_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_my_plants, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        addButton = view.findViewById(R.id.add_button)
        emptyImageView = view.findViewById(R.id.empty_imageview)
        noDataTextView = view.findViewById(R.id.no_data)

        addButton.setOnClickListener {
            val intent = Intent(activity, AddActivity::class.java)
            startActivityForResult(intent, ADD_BOOK_REQUEST_CODE)
        }

        myDB = MyDatabaseHelper(requireContext())
        bookId = ArrayList()
        bookTitle = ArrayList()
        bookAuthor = ArrayList()
        bookPages = ArrayList()

        storeDataInArrays()

        customAdapter = CustomAdapter(requireActivity(), requireContext(), bookId, bookTitle, bookAuthor, bookPages)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_BOOK_REQUEST_CODE || requestCode == UPDATE_BOOK_REQUEST_CODE) {
                storeDataInArrays()
                customAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun storeDataInArrays() {
        val cursor: Cursor? = myDB.readAllData()
        if (cursor?.count == 0) {
            emptyImageView.visibility = View.VISIBLE
            noDataTextView.visibility = View.VISIBLE
        } else {
            bookId.clear()
            bookTitle.clear()
            bookAuthor.clear()
            bookPages.clear()
            while (cursor?.moveToNext() == true) {
                bookId.add(cursor.getString(0))
                bookTitle.add(cursor.getString(1))
                bookAuthor.add(cursor.getString(2))
                bookPages.add(cursor.getString(3))
            }
            emptyImageView.visibility = View.GONE
            noDataTextView.visibility = View.GONE
        }
    }
}
