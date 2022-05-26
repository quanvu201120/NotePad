package com.quanvu201120.notepad



import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.quanvu201120.notepad.adapter.RecycleAdapter
import com.quanvu201120.notepad.adapter.iItemRecycleClick
import com.quanvu201120.notepad.model.Database
import com.quanvu201120.notepad.model.NoteP
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.collections.ArrayList

val C_USER = "user"
val C_NOTEPAD = "notePad"

class MainActivity : AppCompatActivity(), iItemRecycleClick {

    lateinit var btn_add_main : Button
    lateinit var recyclerView: RecyclerView
    lateinit var list : ArrayList<NoteP>
    lateinit var adapter : RecycleAdapter
    lateinit var searchView: SearchView
    lateinit var database: Database
    lateinit var img_more : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_add_main = findViewById(R.id.btn_add_main)
        recyclerView = findViewById(R.id.recycleView_main)
        searchView = findViewById(R.id.searchView_main)
        img_more = findViewById(R.id.img_more_main)
        database = Database(this@MainActivity)


        list = ArrayList()
        list = database.getNote()
        list.reverse()



        adapter = RecycleAdapter(list, this@MainActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)


        btn_add_main.setOnClickListener {


            startActivity(Intent(this, AddNoteActivity::class.java))
            finish()

        }



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                list.clear()
                list.addAll(database.getNote())

                var listFilter = ArrayList<NoteP>()
                for (item in list) {
                    if (item.title!!.lowercase().contains(p0!!.lowercase())) {
                        listFilter.add(item)
                    }
                }

                list.clear()
                list.addAll(listFilter)
                adapter.notifyDataSetChanged()

                return false
            }
        })


        img_more.setOnClickListener {
            var popupMenu: PopupMenu = PopupMenu(this, img_more)
            popupMenu.inflate(R.menu.popup_main)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(p0: MenuItem?): Boolean {

                    if (p0?.itemId == R.id.item_saoluu) {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }

                    return true
                }

            })

            popupMenu.show()
        }

        SetColorStatusBar(this,R.color.purple_200)

    }


    override fun onItemClick(note: NoteP) {
        var intent = Intent(this,UpdateNoteActivity::class.java)
        intent.putExtra("NoteFromMain",note)
        startActivity(intent)
        finish()
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {

        if (item.itemId == 0){

            var alert : AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
            alert.setTitle("Bạn có muốn xóa ghi chú")
            alert.setIcon(R.drawable.ic_baseline_warning_24)
            alert.setPositiveButton("Có", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    var id = list.get(item.groupId).id

                    database.deleteNote(id)

                    list.clear()
                    list.addAll(database.getNote())
                    adapter.notifyDataSetChanged()
                }
            })
            alert.setNeutralButton("Hủy", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            alert.show()

        }

        return super.onContextItemSelected(item)
    }




}

fun signOut(){
    var auth = FirebaseAuth.getInstance()
    if (auth.currentUser != null){
        auth.signOut()
    }
}

fun SetColorStatusBar(activity : Activity, color : Int){
    val window = activity.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = ContextCompat.getColor(activity, color)
}

