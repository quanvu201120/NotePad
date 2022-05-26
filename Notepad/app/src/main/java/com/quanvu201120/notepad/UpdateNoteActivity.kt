package com.quanvu201120.notepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.quanvu201120.notepad.model.Database
import com.quanvu201120.notepad.model.NoteP
import java.util.*

class UpdateNoteActivity : AppCompatActivity() {

    lateinit var img_back_update : ImageView
    lateinit var img_check_update : ImageView
    lateinit var edt_title_update : EditText
    lateinit var edt_content_update : EditText

    lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        img_back_update = findViewById(R.id.img_back_update)
        img_check_update = findViewById(R.id.img_check_update)
        edt_title_update = findViewById(R.id.edt_title_update)
        edt_content_update = findViewById(R.id.edt_content_update)
        database = Database(this)

        var gNote : NoteP = intent.getParcelableExtra<NoteP>("NoteFromMain") as NoteP

        //gNote : NoteP = intent.getParcelableExtra<NoteP>("NoteFromMain") as NoteP

        edt_title_update.text = FunEditTable(gNote.title!!)
        edt_content_update.text = Editable.Factory.getInstance().newEditable(gNote.content)

        img_back_update.setOnClickListener {
            back()
        }

        img_check_update.setOnClickListener {

            val title = edt_title_update.text.toString()
            val content = edt_content_update.text.toString()

            if (title.isEmpty()){
                edt_title_update.setError("Vui lòng nhập tiêu đề")
            }
            if (content.isEmpty()){
                edt_content_update.setError("Vui lòng nhập nội dung")
            }

            if (!title.isEmpty() && !content.isEmpty()){

                if (title != gNote.title || content != gNote.content){
                    var calendar : Calendar = Calendar.getInstance()

                    var year = calendar.get(Calendar.YEAR)
                    var month = calendar.get(Calendar.MONTH)
                    var day = calendar.get(Calendar.DAY_OF_MONTH)
                    var hour = calendar.get(Calendar.HOUR_OF_DAY)
                    var minute = calendar.get(Calendar.MINUTE)

                    var time = "${hour}h${minute}m ${day}-${month}-${year}"

                    var noteUpdate = NoteP(id = gNote.id, title = title, content = content, time = time, docId = gNote.docId)

                    database.deleteNote(gNote.id)
                    database.addNote(noteUpdate)

                    Toast.makeText(this, "Đã cập nhật ghi chú", Toast.LENGTH_SHORT).show()

                    back()
                }
                else{
                    back()
                }

            }

        }
        SetColorStatusBar(this,R.color.purple_200)

    }

    fun FunEditTable(string : String) : Editable{
        return Editable.Factory.getInstance().newEditable(string)
    }


    fun back(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        back()
    }
}