package com.quanvu201120.notepad

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.quanvu201120.notepad.model.Database
import com.quanvu201120.notepad.model.NoteP
import org.w3c.dom.Text
import java.io.*
import java.lang.Exception
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    lateinit var edt_title: EditText
    lateinit var edt_content: EditText
    lateinit var btn_save: Button
    lateinit var tv_error1 : TextView
    lateinit var tv_error2 : TextView
    lateinit var database: Database

    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        edt_title = findViewById(R.id.edt_title_add)
        edt_content = findViewById(R.id.edt_content_add)
        btn_save = findViewById(R.id.btn_save_add)
        tv_error1 = findViewById(R.id.tv_error1)
        tv_error2 = findViewById(R.id.tv_error2)

        database = Database(this)
        calendar = Calendar.getInstance()

        btn_save.setOnClickListener {

            var title : String = edt_title.text.toString()
            var content : String = edt_content.text.toString()

            if (title.isEmpty()){
                tv_error1.visibility = View.VISIBLE
            }

            if (content.isEmpty()){
                tv_error2.visibility = View.VISIBLE
            }

            if (!title.isEmpty() && !content.isEmpty()){

                var year = calendar.get(Calendar.YEAR)
                var month = calendar.get(Calendar.MONTH)
                var day = calendar.get(Calendar.DAY_OF_MONTH)
                var hour = calendar.get(Calendar.HOUR_OF_DAY)
                var minute = calendar.get(Calendar.MINUTE)

                var time = "${hour}h${minute}m ${day}-${month}-${year}"

                var note = NoteP(  title =  title,  content = content,   time = time)

                database.addNote(note)

                Toast.makeText(this, "Tạo ghi chú thành công", Toast.LENGTH_SHORT).show()
                intentt()

            }

        }
        SetColorStatusBar(this,R.color.purple_200)


    }

//    fun writeNote(title : String, content : String){
//        try {
//            var file : File = File(filesDir,title + ".txt")
//            var fileOutputStream : FileOutputStream = FileOutputStream(file)
//            var outputStreamWriter : OutputStreamWriter = OutputStreamWriter(fileOutputStream)
//            var bufferedWriter : BufferedWriter = BufferedWriter(outputStreamWriter)
//
//            bufferedWriter.write(content)
//
//            bufferedWriter.close()
//            outputStreamWriter.close()
//            fileOutputStream.close()
//
//        }
//        catch (e : Exception){
//            e.printStackTrace()
//        }
//    }
//
//    fun readNote(title: String){
//        try {
//            var fileInputStream = FileInputStream(title)
//            var inputStreamReader = InputStreamReader(fileInputStream)
//            var bufferedReader = BufferedReader(inputStreamReader)
//            var stringBuilder = StringBuilder()
//
//            var line : String? = bufferedReader.readLine()
//
//            while (line != null){
//                stringBuilder.append(line)
//                line = bufferedReader.readLine()
//            }
//
//
//            bufferedReader.close()
//            inputStreamReader.close()
//            fileInputStream.close()
//        }
//        catch (e : Exception){
//            e.printStackTrace()
//        }
//    }

    fun intentt(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        intentt()
    }

}