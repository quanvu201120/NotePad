package com.quanvu201120.notepad.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.quanvu201120.notepad.*
import com.quanvu201120.notepad.model.Database
import com.quanvu201120.notepad.model.NoteP
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList


class BottomFragment2 : Fragment() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseUser: FirebaseUser
    lateinit var tv_email_mine : TextView
    lateinit var imgPopup_mine : ImageView
    lateinit var imgLogout : ImageView
    lateinit var btn_doimk_mine : Button
    lateinit var btn_saoluu_mine : Button
    lateinit var btn_taidulieu_mine : Button
    lateinit var progressBar_saoluu : ProgressBar
    lateinit var progressBar_khoiphuc : ProgressBar
    lateinit var database: Database
    lateinit var arrayList: ArrayList<NoteP>
    lateinit var arrayString : ArrayList<String>

    var back : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_bottom2, container, false)

        tv_email_mine = view.findViewById(R.id.tv_email_mine)
        btn_doimk_mine = view.findViewById(R.id.btn_doimk_mine)
        btn_saoluu_mine = view.findViewById(R.id.btn_saoluu_mine)
        btn_taidulieu_mine = view.findViewById(R.id.btn_taidulieu_mine)
        imgPopup_mine = view.findViewById(R.id.imgPopup_mine)
        progressBar_saoluu = view.findViewById(R.id.progressBar_saoluu)
        progressBar_khoiphuc = view.findViewById(R.id.progressBar_khoiphuc)
        imgLogout = view.findViewById(R.id.imgLogout)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firestore = FirebaseFirestore.getInstance()
        database = Database(requireContext())

        arrayList = ArrayList()



        if (user_sync.userNote == null || user_sync.userNote!!.size == 0){
//            Log.e("abc null", "rong")
            arrayString = ArrayList()
        }
        else{
            arrayString = user_sync.userNote!!

//            Log.e("abc null", "khong rong")
        }



        tv_email_mine.text = firebaseUser.email

        imgLogout.setOnClickListener {
            var alert = AlertDialog.Builder(requireContext())

            alert.setTitle("Đăng xuất tài khản ?")
            alert.setIcon(R.drawable.ic_baseline_logout)
            alert.setPositiveButton("Đồng ý", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    firebaseAuth.signOut()
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                    activity?.finish()
                }
            })
            alert.setNeutralButton("Hủy bỏ", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            } )

            alert.show()
        }

        imgPopup_mine.setOnClickListener {

            var popupMenu : PopupMenu = PopupMenu(requireContext(),imgPopup_mine)
            popupMenu.inflate(R.menu.popup_mine)

            popupMenu.setOnMenuItemClickListener {

                if (it.itemId == R.id.item_hdsd){

                    HDSD()

                }

                return@setOnMenuItemClickListener true
            }

            popupMenu.show()

        }

        btn_doimk_mine.setOnClickListener {

            XacThucMK()
        }

        btn_saoluu_mine.setOnClickListener {
            SaoLuu()
        }

        btn_taidulieu_mine.setOnClickListener {
            KhoiPhuc()
        }


        return view
    }



    /////////////////////////////////////////
    private fun XacThucMK() {

        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_xacthucmk_sync)

        var editText = dialog.findViewById<EditText>(R.id.edt_xacthucmk_dialog_xacthucmk)
        var button = dialog.findViewById<Button>(R.id.btn_xacnhan_dialog_xacthucmk)
        var progressBar = dialog.findViewById<ProgressBar>(R.id.progress_dialog_xacthucmk)

        button.setOnClickListener {

            button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE


            var email = firebaseUser.email
            var password : String? = editText.text.toString().trim()



            if (password!!.isEmpty()){
                button.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                editText.setError("Vui lòng nhập mật khẩu")
            }
            else{
                var credential  : AuthCredential = EmailAuthProvider.getCredential(email!!,password)
                firebaseUser.reauthenticate(credential)
                    .addOnSuccessListener {
                        DoiMatKhau()
                        dialog.dismiss()
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireContext(), "Mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show()
                        button.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
            }

        }


        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()

    }

    private fun DoiMatKhau() {

        var dialogDoiMK = Dialog(requireContext())
        dialogDoiMK.setContentView(R.layout.dialog_doimk_sync)

        var editText1 = dialogDoiMK.findViewById<EditText>(R.id.edt_mk1_dialog_doimk)
        var editText2 = dialogDoiMK.findViewById<EditText>(R.id.edt_mk2_dialog_doimk)
        var button = dialogDoiMK.findViewById<Button>(R.id.btn_xacnhan_dialog_doimk)
        var progressBar = dialogDoiMK.findViewById<ProgressBar>(R.id.progres_dialog_doimk)

        button.setOnClickListener {

            button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            var mk1  = editText1.text.toString()
            var mk2  = editText2.text.toString()

            if (mk1.isEmpty() ){
                button.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                editText1.setError("Vui lòng nhập mật khẩu")
            }

            if (mk2.isEmpty() ){
                button.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                editText2.setError("Vui lòng nhập mật khẩu")
            }

            if ( !mk1.isEmpty() && !mk2.isEmpty() ){

                if (mk1 != mk2){
                    button.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    editText2.setError("Mật khẩu không khớp")
                }
                else{

                    firebaseUser.updatePassword(mk1)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                            dialogDoiMK.dismiss()
                        }
                        .addOnFailureListener{
                            button.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Mật không không hợp lệ\nÍt nhât 6 kí tự", Toast.LENGTH_SHORT).show()
                        }
                }

            }

        }

        dialogDoiMK.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialogDoiMK.show()

    }

    private fun HDSD() {
        var dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_hdsd)

        var textview : TextView
        textview = dialog.findViewById(R.id.tv_hdsd)

        var inputstream : InputStream = resources.openRawResource(R.raw.hdsd)

        var str_hdsd = inputstream.readBytes().toString(Charset.defaultCharset())

        textview.text = str_hdsd

        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun SaoLuu() {

        btn_saoluu_mine.visibility = View.INVISIBLE
        progressBar_saoluu.visibility = View.VISIBLE

        arrayList = database.getNote()

        var userId = firebaseUser.uid

        if (arrayList == null || arrayList.size == 0){
            Toast.makeText(requireContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show()
            btn_saoluu_mine.visibility = View.VISIBLE
            progressBar_saoluu.visibility = View.GONE
        }
        else{
            for (item in arrayList){
                if (item.docId == "null"){

                    var date = Date()
                    item.docId = userId + date.time

                    arrayString.add(item.docId!!)

//                    Log.e("ABC add user", arrayString.toString())

                    database.updateNote(item)

                    firestore.collection(C_NOTEPAD).document(item.docId!!).set(item)

                }
                else{

                    if (item.docId!!.contains(userId) == false){
                        var date = Date()
                        item.docId = userId + date.time
                        database.updateNote(item)
                    }

                    firestore.collection(C_NOTEPAD).document(item.docId!!).set(item)
                    if (arrayString.indexOf(item.docId!!)!! < 0){
                        arrayString.add(item.docId!!)
                    }
                }
            }

            user_sync.userNote = arrayString

            firestore.collection(C_USER).document(userId).set(user_sync)
                .addOnSuccessListener {
                    //

                    firestore.collection(C_NOTEPAD).whereIn("docId", user_sync.userNote!!)
                        .get().addOnSuccessListener {

                            arrayNote_sync = it.toObjects(NoteP::class.java) as ArrayList<NoteP>

                            btn_saoluu_mine.visibility = View.VISIBLE
                            progressBar_saoluu.visibility = View.GONE

                            Toast.makeText(requireContext(), "Sao lưu hoàn tất", Toast.LENGTH_SHORT).show()
                        }

                    //
                }

        }





    }

    private fun KhoiPhuc(){

        btn_taidulieu_mine.visibility = View.INVISIBLE
        progressBar_khoiphuc.visibility = View.VISIBLE

        if (user_sync.userNote == null || user_sync.userNote!!.size == 0){
            btn_taidulieu_mine.visibility = View.VISIBLE
            progressBar_khoiphuc.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show()
        }
        else {

            arrayList = database.getNote()

            firestore.collection(C_NOTEPAD).whereIn("docId", arrayString)
                .get().addOnSuccessListener {
                    arrayNote_sync = it.toObjects(NoteP::class.java) as ArrayList<NoteP>

                    //

                    for (n in arrayNote_sync) {
                        var check = false
                        for (it in arrayList) {
                            if (it.docId == n.docId) {

                                var title: String? = ""
                                var content: String? = ""

                                if (n.title == it.title) {
                                    title = it.title
                                } else {
                                    title = n.title + " - " + it.title
                                }


                                if (n.content == it.content) {
                                    content = it.content
                                } else {
                                    content =
                                        "- - - - - - - - - -\n" + n.content + "\n- - - - - - - - - -\n" + it.content
                                }


                                var note = NoteP(title = title,
                                    content = content,
                                    time = it.time,
                                    docId = it.docId,
                                    id = it.id)
                                database.updateNote(note)

                                check = true

                            }
                        }

                        if (!check) {
                            database.addNote(n)
                        }

                    }

                    btn_taidulieu_mine.visibility = View.VISIBLE
                    progressBar_khoiphuc.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(),
                        "Đã tải dữ liệu về thiết bị",
                        Toast.LENGTH_SHORT).show()

                    //

                }

        }

    }
}