package com.berkantcanerkanat.safepassword

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private var context: Context?,private var passwordList: ArrayList<PasswordModel>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = passwordList[position].title
        holder.itemDigit.text = passwordList[position].digit

        //edit icin genel gecis
        holder.itemView.setOnClickListener {
            val intent = Intent(context,PasswordDetails::class.java)
            intent.putExtra("from",position)
            context!!.startActivity(intent)
        }

        //copy ye tıklanmıs
        holder.copyPassword.setOnClickListener {
            val password = passwordList[position].password
            val clipboardManager = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("password", password)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context,"${Singleton.passwordList?.get(position)?.title} password copied",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return passwordList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var itemTitle : TextView
        var itemDigit : TextView
        var copyPassword : ImageView
        init {
            itemTitle = itemView.findViewById(R.id.rowTitleText)
            itemDigit = itemView.findViewById(R.id.numOfDigitText)
            copyPassword = itemView.findViewById(R.id.copyImageView)
        }
    }

}