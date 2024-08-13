package com.example.studysync.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.studysync.R
import com.example.studysync.ui.theme.StudySyncTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.studysync.MainActivity
import com.example.studysync.api.Retrofitinstance
import com.example.studysync.ui.theme.ButtonCustomised
import com.example.studysync.ui.theme.OutlinedTextFieldCustomised
import com.example.studysync.ui.theme.TextButtonCustomised
import com.example.studysync.ui.theme.TextPurple
import com.example.studysync.utils.CustomToast
import com.example.studysync.utils.SharedPrefManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            StudySyncTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    LoginScreen(this, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen( context: Context, modifier: Modifier = Modifier) {

    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // logo img
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", Modifier.size(150.dp))
//        Text(text = "Welcome back!", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = usernameOrEmail,
            onValueChange = {
                usernameOrEmail = it
            },
            label = { Text("Enter Your Username / Email", style = TextPurple) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldCustomised
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Enter Your Password", style = TextPurple) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { /* Show/Hide password logic */ }) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Toggle password visibility")
                }
            },
            colors = OutlinedTextFieldCustomised
        )

        TextButton(onClick = { /* Forgot password logic */ }, colors = TextButtonCustomised) {
            Text(text = "Forgot Password?")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                HandleLogin(context as Activity, usernameOrEmail, password)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonCustomised
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account?")
            TextButton(onClick = {
                context.startActivity(Intent(context, SignUp::class.java))
                val act = context as Activity
                act.finish()
            }, colors = TextButtonCustomised) {
                Text(text = "Signup")
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                CustomToast.showToast(context, "We are working on it")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextButtonCustomised
        ) {
            Icon(imageVector = Icons.Default.Email, contentDescription = "Google Logo")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Login with Google")
        }
    }
}

fun HandleLogin(activity: Activity, email: String, password: String) {
    if(email == "" || password == ""){
        CustomToast.showToast(activity, "Please fill all the fields")
    }else{
        Retrofitinstance.apiInterface.login(mapOf(
            "email" to email,
            "password" to password
        )).enqueue(
            object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    val sharedPrefManager = SharedPrefManager(activity)
                    if(response.isSuccessful()){
                        val responseBody = response.body()?.string()
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val message = when {
                                jsonObject.has("message") -> jsonObject.getString("message")
                                jsonObject.has("error") -> jsonObject.getString("error")
                                else -> "Unknown response"
                            }
                            if(jsonObject.getString("status") == "ERROR"){
                                CustomToast.showToast(activity, message)
                            }else{
                                sharedPrefManager.storeToken(jsonObject.getString("token"))
                                CustomToast.showToast(activity, "Logged in as ${jsonObject.getJSONObject("user").getString("username")}")
                                // redirect to Home Screen
                                activity.startActivity(Intent(activity, MainActivity::class.java))
                                activity.finish()
                            }
                        }
                    }else{
                        val responseBody = response.errorBody()?.string()
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            CustomToast.showToast(activity, jsonObject.getString("message"))
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    CustomToast.showToast(activity, "Something went wrong")
                }
            }
        )
    }
}
