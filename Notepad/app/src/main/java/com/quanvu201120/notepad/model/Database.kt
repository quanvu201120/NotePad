package com.quanvu201120.notepad.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

val DB_NAME = "NotePadDb"
val TB_NAME = "MyTable"
val VER = 1
val KEY_ID = "id"
val KEY_TITLE = "title"
val KEY_CONTENT = "content"
val KEY_TIME = "time"
val KEY_DOC_ID = "docId"

class Database (context: Context) : SQLiteOpenHelper(context, DB_NAME,null, VER) {
    override fun onCreate(p0: SQLiteDatabase?) {

        var sql = "CREATE TABLE $TB_NAME($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,$KEY_DOC_ID TEXT,$KEY_TITLE TEXT,$KEY_CONTENT TEXT,$KEY_TIME TEXT)"

        p0?.execSQL(sql)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TB_NAME")
        onCreate(p0)
    }


    @SuppressLint("Range")
    fun getNote() : ArrayList<NoteP> {

        var arrayNote : ArrayList<NoteP> = ArrayList()

        var db = this.readableDatabase

        var cursor : Cursor? = null

        cursor = db.query(TB_NAME,null,null,null,null,null,null,null)


        while (cursor.moveToNext()){

            var id  = cursor.getInt( cursor.getColumnIndex(KEY_ID) )
            var title = cursor.getString( cursor.getColumnIndex(KEY_TITLE) )
            var content = cursor.getString( cursor.getColumnIndex(KEY_CONTENT) )
            var time = cursor.getString( cursor.getColumnIndex(KEY_TIME) )
            var docId = cursor.getString(cursor.getColumnIndex(KEY_DOC_ID))

            var tmp = NoteP(title = title,content = content,time = time,id = id, docId = docId)

//            Log.e( "ABC DB get", tmp.toString() )

            arrayNote.add(tmp)

        }
        db.close()
        return arrayNote

    }


    fun addNote(note: NoteP) {

        var db = this.writableDatabase

        var contentValues = ContentValues()

        contentValues.put(KEY_DOC_ID,note.docId)
        contentValues.put(KEY_TITLE,note.title)
        contentValues.put(KEY_CONTENT,note.content)
        contentValues.put(KEY_TIME,note.time)


        db.insert(TB_NAME,null,contentValues)

        db.close()
    }



    fun updateNote(note : NoteP){

        val db = this.writableDatabase

        var contentValue = ContentValues()
        contentValue.put(KEY_DOC_ID,note.docId)
        contentValue.put(KEY_TITLE,note.title)
        contentValue.put(KEY_CONTENT,note.content)
        contentValue.put(KEY_TIME,note.time)


        db.update(TB_NAME,contentValue, KEY_ID + "=" + note.id,null)
        db.close()

    }


    fun deleteNote(id : Int){
        val db = this.writableDatabase

        db.delete(TB_NAME, KEY_ID + "=" + id,null)

        db.close()
    }


}



