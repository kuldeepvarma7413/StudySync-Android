package com.example.studysync.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studysync.MainActivity
import com.example.studysync.R
import com.example.studysync.api.Retrofitinstance
import com.example.studysync.ui.theme.ButtonCustomised
import com.example.studysync.ui.theme.OutlinedTextFieldCustomised
import com.example.studysync.ui.theme.StudySyncTheme
import com.example.studysync.ui.theme.TextPurple
import com.example.studysync.ui.theme.darkPurple
import com.example.studysync.ui.theme.white
import com.example.studysync.utils.CustomToast
import com.example.studysync.utils.SharedPrefManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudySyncTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    ForgetPassword(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ForgetPassword(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            Modifier.size(150.dp)
        )
//        Text(text = "Welcome back!", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Enter Your Email", style = TextPurple) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { /* Show/Hide password logic */ }) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = OutlinedTextFieldCustomised
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                handleForgetPassword(context, email)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonCustomised
        ) {
            Text(text = "Forget Password")
        }
    }
}

fun handleForgetPassword(context: Context, email: String) {
    if (email == "") {
        CustomToast.showToast(context, "Please fill all the fields")
    } else {
        Retrofitinstance.apiInterface.forgetPassword(email).enqueue(
            object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    val sharedPrefManager = SharedPrefManager(context)
                    if (response.isSuccessful()) {
                        val responseBody = response.body()?.string()
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val message = jsonObject.getString("message")
                            CustomToast.showToast(context, message)

                            Handler().postDelayed({
                                context.startActivity(Intent(context, Login::class.java))
                                (context as Activity).finish()
                            }, 2000)
                        }
                    } else {
                        val responseBody = response.errorBody()?.string()
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val message = when {
                                jsonObject.has("message") -> jsonObject.getString("message")
                                jsonObject.has("error") -> jsonObject.getString("error")
                                else -> "Unknown response"
                            }
                            CustomToast.showToast(context, message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    CustomToast.showToast(context, "Something went wrong")
                }
            })
    }
}