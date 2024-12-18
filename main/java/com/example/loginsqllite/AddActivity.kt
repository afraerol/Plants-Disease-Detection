package com.example.loginsqllite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var authorInput: EditText
    private lateinit var pagesInput: EditText
    private lateinit var addButton: Button
    private lateinit var exit_button: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        titleInput = findViewById(R.id.title_input)
        authorInput = findViewById(R.id.author_input)
        pagesInput = findViewById(R.id.pages_input)
        addButton = findViewById(R.id.add_button)
        exit_button=findViewById(R.id.exit_button)

        exit_button.setOnClickListener{
            finish()
        }

        addButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val author = authorInput.text.toString().trim()
            val pagesText = pagesInput.text.toString().trim()

            if (title.isEmpty() || author.isEmpty() || pagesText.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            } else {
                val pages = pagesText.toIntOrNull()
                if (pages != null) {
                    val myDB = MyDatabaseHelper(this@AddActivity)
                    myDB.addBook(title, author, pages)
                    Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Age must be a valid number", Toast.LENGTH_SHORT).show()
                }
            }
            setResult(RESULT_OK)
            finish()

        }
    }
    }

