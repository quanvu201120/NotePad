package com.quanvu201120.notepad

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.quanvu201120.notepad.model.ConnectionLiveData
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var edt_email : EditText
    lateinit var edt_pass : EditText
    lateinit var checkbox : CheckBox
    lateinit var btn_login : Button
    lateinit var tv_quenmk : TextView
    lateinit var tv_dangky : TextView
    lateinit var img_connect : ImageView
    lateinit var tv_connect : TextView
    lateinit var progressBar: ProgressBar

    lateinit var connect: ConnectionLiveData

    lateinit var auth : FirebaseAuth

    val STR_EMAIL = "Email"
    val STR_PASS = "Pass"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edt_email = findViewById(R.id.edt_email_login)
        edt_pass = findViewById(R.id.edt_pass_login)
        checkbox = findViewById(R.id.cb_nhomk_login)
        btn_login = findViewById(R.id.btn_dangnhap_login)
        tv_quenmk = findViewById(R.id.tv_quenmk_login)
        tv_dangky = findViewById(R.id.tv_dangky_login)
        img_connect = findViewById(R.id.img_not_connect_login)
        tv_connect = findViewById(R.id.tv_not_connect_login)
        progressBar = findViewById(R.id.progress_dangnhap)

        auth = FirebaseAuth.getInstance()

        //
        connect = ConnectionLiveData(application)
        connect.observe(this, {

            if (it){
                btn_login.isEnabled = true
                img_connect.visibility = View.GONE
                tv_connect.visibility = View.GONE
            }
            else{
                btn_login.isEnabled = false
                img_connect.visibility = View.VISIBLE
                tv_connect.visibility = View.VISIBLE
            }

        })
        //

        //

        if (auth.currentUser != null){
            intentLoading()
        }


        //

        var sharedPreferences : SharedPreferences = getSharedPreferences("ACCOUNT", Activity.MODE_PRIVATE)


        val getEmail : String? = sharedPreferences.getString(STR_EMAIL,null)
        val getPass : String? = sharedPreferences.getString(STR_PASS,null)

        if (getEmail != null && getPass != null){
            edt_email.text = ToEditable(getEmail)
            edt_pass.text = ToEditable(getPass)
            checkbox.isChecked = true
        }

        btn_login.setOnClickListener {

            btn_login.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            var email = edt_email.text.toString()
            var pass = edt_pass.text.toString()

            if (email.isEmpty()){
                edt_email.setError("Vui lòng nhập email")
                btn_login.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            if (pass.isEmpty()){
                edt_pass.setError("Vui lòng nhập mật khẩu")
                btn_login.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            if (!pass.isEmpty() && !email.isEmpty()){
                var sharedPreferences2 : SharedPreferences = getSharedPreferences("ACCOUNT", Activity.MODE_PRIVATE)
                var editer : SharedPreferences.Editor = sharedPreferences2.edit()

                if (checkbox.isChecked){

                    editer.putString(STR_EMAIL,email)
                    editer.putString(STR_PASS,pass)
                    editer.commit()
                }
                else{
                    editer.clear().commit()
                }

                auth.signInWithEmailAndPassword(email,pass)
                    .addOnSuccessListener {
                        intentLoading()
                    }
                    .addOnFailureListener{

                        var e_format = "The email address is badly formatted."
                        var e_pass = "The password is invalid or the user does not have a password."
                        var e_email = "There is no user record corresponding to this identifier. The user may have been deleted."

                        if (it.message == e_format){
                            Toast.makeText(this@LoginActivity, "Email sai định dạng", Toast.LENGTH_SHORT).show()
                        }
                        else if(it.message == e_pass){
                            Toast.makeText(this@LoginActivity, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@LoginActivity, "Email chưa đăng ký tài khoản", Toast.LENGTH_SHORT).show()
                        }

                        btn_login.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                    //activity sao luu

            }


        }

        tv_dangky.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
        }

        tv_quenmk.setOnClickListener {

            var dialog : Dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_quen_mat_khau)

            var edt_quenmk_dialog : EditText = dialog.findViewById(R.id.edt_quenmk_dialog)
            var progressBar : ProgressBar = dialog.findViewById(R.id.progress_quenmk)
            var button : Button = dialog.findViewById(R.id.btn_gui_dialog)
            button.setOnClickListener {

                button.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                var email_reset = edt_quenmk_dialog.text.toString()

                if (email_reset.isEmpty()){
                    edt_quenmk_dialog.setError("Vui lòng nhập email")
                }
                else{
                    auth.sendPasswordResetEmail(email_reset)
                        .addOnSuccessListener {
                            Toast.makeText(this@LoginActivity, "Mật khẩu mới đã được gửi vào email", Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener{
                            val e_email = "There is no user record corresponding to this identifier. The user may have been deleted."
                            val e_format = "The email address is badly formatted."
                            if (it.message == e_email){
                                Toast.makeText(this@LoginActivity, "Email không chính xác", Toast.LENGTH_LONG).show()
                            }
                            else{
                                Toast.makeText(this@LoginActivity, "Email sai định dạng", Toast.LENGTH_LONG).show()
                            }
                            button.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        }
                }

            }

            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.show()

        }
        SetColorStatusBar(this,R.color.purple_200)


    }


    fun  ToEditable(string : String) : Editable{
        return Editable.Factory.getInstance().newEditable(string)
    }

    fun intentLoading(){
        startActivity(Intent(this@LoginActivity, LoadingActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

    }

}