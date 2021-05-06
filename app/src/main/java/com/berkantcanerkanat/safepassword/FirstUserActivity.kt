package com.berkantcanerkanat.safepassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.berkantcanerkanat.safepassword.databinding.ActivityFirstUserBinding
import com.berkantcanerkanat.safepassword.databinding.ActivityFirstUserBinding.bind
import com.berkantcanerkanat.safepassword.databinding.ActivityFirstUserBinding.inflate
import com.berkantcanerkanat.safepassword.databinding.ActivityMainBinding

class FirstUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.enrollButton.setOnClickListener {
            val pass1 = binding.firstPasswordText.text.toString().trim()
            val pass2 = binding.firstPasswordText2.text.toString().trim()


            if(!TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2) && pass1.equals(pass2)){
                val sp = applicationContext.getSharedPreferences("com.berkantcanerkanat.safepassword", MODE_PRIVATE)
                sp.edit().putString("password",pass1.trim()).apply()

                //ana ekran gecis
                val intent = Intent(this@FirstUserActivity,PasswordsActivity::class.java)
                finish()
                startActivity(intent)
            }
        }


    }
}