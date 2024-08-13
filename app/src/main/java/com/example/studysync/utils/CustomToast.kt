package com.example.studysync.utils

import android.content.Context
import android.widget.Toast

class CustomToast private constructor(){
    companion object{
        private var toast : Toast? = null
        fun showToast(context: Context, message: String){
            toast?.cancel()
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }
}