package com.quanvu201120.notepad.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.notepad.*
import com.quanvu201120.notepad.adapter.IitemBottomClick
import com.quanvu201120.notepad.adapter.RecycleAdapterBottom
import com.quanvu201120.notepad.model.NoteP
import java.util.*
import kotlin.collections.ArrayList


class BottomFragment1 : Fragment(), IitemBottomClick{

    lateinit var recyclerView: RecyclerView
    lateinit var adapter : RecycleAdapterBottom
    lateinit var firestore: FirebaseFirestore
    lateinit var arrayTmp : ArrayList<NoteP>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_bottom1, container, false)

        recyclerView = view.findViewById(R.id.recycleView_bottom1)
        firestore = FirebaseFirestore.getInstance()

        arrayTmp = ArrayList()
        arrayTmp = arrayNote_sync


        adapter = RecycleAdapterBottom(arrayNote_sync,this@BottomFragment1)

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)



        return view
    }

    override fun itemBClick(note: NoteP) {

        var dialog : Dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_show_note)

        var textview1 : TextView = dialog.findViewById(R.id.tv_title_popup)
        var textview2 : TextView = dialog.findViewById(R.id.tv_content_popup)

        textview1.setText(note.title)
        textview2.setText(note.content)

        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var position : Int = item.groupId

        if (item.itemId == 0){
            var noteUpdate = arrayNote_sync.get(position)
            UpdateNoteSync(noteUpdate,position)

        }
        else{
            var noteDelete = arrayNote_sync.get(position)

            DeleteNoteSync(noteDelete, position)

        }

        return super.onContextItemSelected(item)
    }

    fun DeleteNoteSync(note : NoteP, position : Int){

        var alert : AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alert.setTitle("Xác nhận xóa")
        alert.setIcon(R.drawable.ic_baseline_delete)
        alert.setPositiveButton("Xác nhận", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

                firestore.collection(C_NOTEPAD).document(note.docId!!)
                    .delete()
                    .addOnSuccessListener {

                        arrayNote_sync.remove(note)
                        user_sync.userNote?.remove(note.docId!!)

                        firestore.collection(C_USER).document(user_sync.userId)
                            .set(user_sync)
                            .addOnSuccessListener {


                                adapter.notifyItemRemoved(position)

//                                Log.e("ABC delete", arrayNote_sync.toString())
//                                Log.e("ABC delete", user_sync.userNote.toString())

                            }

                    }

            }
        })
        alert.setNeutralButton("Hủy",object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })
        alert.show()
    }

    fun UpdateNoteSync(note : NoteP, position : Int){

        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_edit_sync)

        var edt_title : EditText = dialog.findViewById(R.id.edt_title_popup_sync)
        var edt_content : EditText = dialog.findViewById(R.id.edt_content_popup_sync)
        var btn_popup : Button = dialog.findViewById(R.id.btn_popup_sync)
        var progressBar : ProgressBar = dialog.findViewById(R.id.pro_edit_sync)

        edt_title.text = Editable.Factory.getInstance().newEditable(note.title)
        edt_content.text = Editable.Factory.getInstance().newEditable(note.content)

        btn_popup.setOnClickListener {

            btn_popup.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)


            var mtitle = edt_title.text.toString()
            var mcontent = edt_content.text.toString()
            var mdocId = note.docId
            var mtime = "${hour}h${minute}m ${day}-${month}-${year}"
            var mid = note.id

            var noteUpdate = NoteP(title = mtitle, content = mcontent, time = mtime, id = mid, docId = mdocId)

            if (mtitle == note.title && mcontent == note.content){
                Toast.makeText(requireContext(), "Cập nhật không thay đổi", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            else{

                firestore.collection(C_NOTEPAD).document(noteUpdate.docId!!)
                    .set(noteUpdate)
                    .addOnSuccessListener {
                        arrayNote_sync.set(arrayNote_sync.indexOf(note),noteUpdate)
                        adapter.notifyItemChanged(position)
                        Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

            }

        }

        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)
        dialog.show()

    }



}