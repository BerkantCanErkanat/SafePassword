package com.berkantcanerkanat.safepassword

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.berkantcanerkanat.safepassword.databinding.ActivityPasswordDetailsBinding
import com.berkantcanerkanat.safepassword.databinding.ActivityPasswordsBinding
import java.util.*

class PasswordDetails : AppCompatActivity() {
    lateinit var binding : ActivityPasswordDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val index = intent.getIntExtra("from",-1)

        if(index != -1){//itemdan gelmişiz
            binding.detailTitleText.setText(Singleton.passwordList?.get(index)?.title)
            binding.detailPasswordText.setText(Singleton.passwordList?.get(index)?.password)
        }


        //random button
        binding.randomButton.setOnClickListener {
            val password = PasswordGenerator.generatePassword()
            binding.detailPasswordText.setText(password)
        }

        //save button
        binding.saveButton.setOnClickListener {
            val title = binding.detailTitleText.text.toString().trim()
            val password = binding.detailPasswordText.text.toString().trim()

            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(password)){
                //index = -1 ise yeni row olusur baska ise update edilir
                dbOperation(index,title,password)
                finish()
            }else{
                Toast.makeText(applicationContext,"Do not leave any input empty",Toast.LENGTH_SHORT).show()
            }
        }

        //delete button
        binding.deleteButton.setOnClickListener {
            if(index == -1){
                Toast.makeText(applicationContext,"Nothing to delete",Toast.LENGTH_SHORT).show()
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("DELETING")
                builder.setMessage("Are you sure you want to delete this password")

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    val db = openOrCreateDatabase("Passwords", MODE_PRIVATE,null)
                    db.delete("passwords","id = ?", arrayOf(Singleton.passwordList!![index].id))
                    Toast.makeText(applicationContext,"successfully deleted",Toast.LENGTH_SHORT).show()
                    Singleton.passwordList!!.removeAt(index)
                    finish()
                }
                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                }

                builder.show()
            }

        }
    }
    fun dbOperation(index : Int,title:String,password:String){
        try {
            val db = openOrCreateDatabase("Passwords", MODE_PRIVATE,null)
            db.execSQL("CREATE TABLE IF NOT EXISTS passwords (id VARCHAR ,title VARCHAR,password VARCHAR,digit VARCHAR)")

            if(index == -1){//yeni row olusup arrayliste ekle
                val sqlString = "INSERT INTO passwords (id,title,password,digit) VALUES (?,?, ?,?)"
                val statement = db.compileStatement(sqlString)
                val newId = UUID.randomUUID().toString()
                // Verilerimizi ekliyoruz
                statement.bindString(1,newId)
                statement.bindString(2,title)
                statement.bindString(3,password)
                statement.bindString(4, password.length.toString())
                statement.execute()

                Singleton.passwordList!!.add(PasswordModel(newId,title,password,
                    password.length.toString()
                ))
            }else{//update et arraylist guncelle
                val oldId = Singleton.passwordList?.get(index)?.id
                val contentValues = ContentValues()
                contentValues.put("id", oldId)
                contentValues.put("title", title)
                contentValues.put("password", password)
                contentValues.put("digit",password.length.toString())
                db.update("passwords", contentValues, "id = ?", arrayOf(oldId))

                Singleton.passwordList?.get(index)?.title = title
                Singleton.passwordList?.get(index)?.password = password
                Singleton.passwordList?.get(index)?.digit = password.length.toString()
            }
            Toast.makeText(applicationContext,"successfully saved",Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()
        }


    }
}