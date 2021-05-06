
package com.berkantcanerkanat.safepassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.berkantcanerkanat.safepassword.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedpref = applicationContext.getSharedPreferences("com.berkantcanerkanat.safepassword", MODE_PRIVATE)
        val password = sharedpref.getString("password",null)

        //password text
       binding.passwordText.addTextChangedListener(object : TextWatcher {
           override fun afterTextChanged(s: Editable?) {
               if(password != null && password.equals(binding.passwordText.text.toString().trim())){
                   //alert Text
                   binding.alertTextView.visibility = View.INVISIBLE

                   //ana ekran gecis
                   val intent = Intent(this@MainActivity,PasswordsActivity::class.java)
                   finish()
                   startActivity(intent)
               }else{
                   //alert Text
                   binding.alertTextView.visibility = View.VISIBLE
                       if(password != null){
                           binding.alertTextView.text = "Your password is wrong"
                       }else{
                           binding.alertTextView.text = "You are not enrolled yet"
                       }

               }
           }

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

           }
       })

        //go button ile gidis
        binding.goButton.setOnClickListener {
            val input = binding.passwordText.text.toString().trim()
            if(password != null && password.equals(input)){
                binding.alertTextView.visibility = View.INVISIBLE

                //ana ekran gecis
                val intent = Intent(this@MainActivity,PasswordsActivity::class.java)
                finish()
                startActivity(intent)
            }else{
                //alert Text
                binding.alertTextView.visibility = View.VISIBLE
                if(password != null){
                    binding.alertTextView.text = "Your password is wrong"
                }else{
                    binding.alertTextView.text = "You are not enrolled yet"
                }
            }
        }

        //first User
            binding.firstUserTextView.setOnClickListener {
                if(password != null){
                    binding.alertTextView.visibility = View.VISIBLE
                    binding.alertTextView.text = "You are already enrolled input your password"
                    return@setOnClickListener
                }
                val intent = Intent(applicationContext,FirstUserActivity::class.java)
                finish()
                startActivity(intent)
            }

    }
}