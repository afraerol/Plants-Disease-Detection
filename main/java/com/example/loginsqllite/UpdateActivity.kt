package com.example.loginsqllite

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var authorInput: EditText
    private lateinit var pagesInput: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var exitButton2: FloatingActionButton

    private var id: String? = null
    private var title: String? = null
    private var author: String? = null
    private var pages: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        titleInput = findViewById(R.id.title_input2)
        authorInput = findViewById(R.id.author_input2)
        pagesInput = findViewById(R.id.pages_input2)
        updateButton = findViewById(R.id.update_button)
        deleteButton = findViewById(R.id.delete_button)
        exitButton2 = findViewById(R.id.exit_button2)

        getAndSetIntentData()
        supportActionBar?.title = title

        exitButton2.setOnClickListener {
            finish()
        }

        updateButton.setOnClickListener {
            val myDB = MyDatabaseHelper(this@UpdateActivity)
            title = titleInput.text.toString().trim()
            author = authorInput.text.toString().trim()
            pages = pagesInput.text.toString().trim()
            myDB.updateData(id!!, title!!, author!!, pages!!)
            setResult(RESULT_OK)
            finish()

        }

        deleteButton.setOnClickListener {
            confirmDialog()
        }
    }

    private fun getAndSetIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("title") &&
            intent.hasExtra("author") && intent.hasExtra("pages")) {
            id = intent.getStringExtra("id")
            title = intent.getStringExtra("title")
            author = intent.getStringExtra("author")
            pages = intent.getStringExtra("pages")

            titleInput.setText(title)
            authorInput.setText(author)
            pagesInput.setText(pages)
            Log.d("stev", "$title $author $pages")
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete $title ?")
        builder.setMessage("Are you sure you want to delete $title ?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            val myDB = MyDatabaseHelper(this@UpdateActivity)
            myDB.deleteOneRow(id!!)
            setResult(RESULT_OK)
            finish()
        }
        builder.setNegativeButton("No", null)
        builder.create().show()
    }
}
