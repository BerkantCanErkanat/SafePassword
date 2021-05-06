package com.berkantcanerkanat.safepassword

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkantcanerkanat.safepassword.databinding.ActivityFirstUserBinding
import com.berkantcanerkanat.safepassword.databinding.ActivityFirstUserBinding.bind
import com.berkantcanerkanat.safepassword.databinding.ActivityFirstUserBinding.inflate
import com.berkantcanerkanat.safepassword.databinding.ActivityPasswordsBinding

class PasswordsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPasswordsBinding
    var passwordsList = arrayListOf<PasswordModel>()
    var adapter : RecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //dataları çek
        dataBaseOperations()

        //info text degisimi
        if(passwordsList.size == 0){//database bos password yok
            binding.infoText.text = "No Password"
        }else{
            binding.infoText.text = "${passwordsList.size} Password(s)"
        }

        //recyler view
        val layoutmanager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutmanager
        adapter = RecyclerViewAdapter(this@PasswordsActivity,Singleton.passwordList!!)
        binding.recyclerView.adapter = adapter


        //random password
        binding.randomPasswordView.setOnClickListener {
           val password = PasswordGenerator.generatePassword()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("password", password)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(applicationContext,"random password copied",Toast.LENGTH_SHORT).show()
        }

        //yeni sifre uretme
        binding.newPasswordView.setOnClickListener {
            val intent = Intent(this@PasswordsActivity,PasswordDetails::class.java)
            intent.putExtra("from",-1)
            startActivity(intent)
        }

    }

    override fun onResume() {// ne zamn calısıyor dikkat et adapter nullsa coker
        super.onResume()

        if(adapter != null){
            adapter!!.notifyDataSetChanged()
        }

    }

    private fun dataBaseOperations(){
        try {
            val db = openOrCreateDatabase("Passwords", MODE_PRIVATE,null)
            db.execSQL("CREATE TABLE IF NOT EXISTS passwords (id VARCHAR ,title VARCHAR,password VARCHAR,digit VARCHAR)")
            val cursor = db.rawQuery("SELECT * FROM passwords",null)
            val titleIx = cursor.getColumnIndex("title")
            val passwordIx = cursor.getColumnIndex("password")
            val digitIx = cursor.getColumnIndex("digit")
            val idIx = cursor.getColumnIndex("id")

            while(cursor.moveToNext()){
                passwordsList.add(PasswordModel(cursor.getString(idIx),cursor.getString(titleIx),cursor.getString(passwordIx),cursor.getString(digitIx)))
            }
            cursor.close()

        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            Singleton.passwordList = passwordsList
        }

    }
}