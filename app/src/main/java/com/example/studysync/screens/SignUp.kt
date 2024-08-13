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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.sp
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

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudySyncTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    SignUpScreen(this, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(context: Context, modifier: Modifier = Modifier) {

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // logo img
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            Modifier.size(150.dp)
        )
//        Text(text = "Welcome back!", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text("Enter Your Name", style = TextPurple) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldCustomised
        )


        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = { Text("Enter Your Username", style = TextPurple) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldCustomised
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Enter Your Email", style = TextPurple) },
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
                HandleSignUp(context as Activity, name, username, email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonCustomised
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account?")
            TextButton(onClick = {
                context.startActivity(Intent(context, Login::class.java))
                val act = context as Activity
                act.finish()
            }, colors = TextButtonCustomised) {
                Text(text = "Login")
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

fun HandleSignUp(
    activity: Activity,
    name: String,
    username: String,
    email: String,
    password: String
) {
    if (email == "" || password == "" || name == "" || username == "") {
        CustomToast.showToast(activity, "Please fill all the fields")
    } else {
        if (!validateData(activity, username, email, password)) {
            return
        }
        Retrofitinstance.apiInterface.signup(
            mapOf(
                "name" to name,
                "username" to username,
                "email" to email,
                "password" to password
            )
        ).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.isSuccessful()) {
                        val responseBody = response.body()?.string()
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val message = when {
                                jsonObject.has("message") -> jsonObject.getString("message")
                                jsonObject.has("error") -> jsonObject.getString("error")
                                else -> "Unknown response"
                            }
                            CustomToast.showToast(activity, message)
                            if(jsonObject.has("message")){
                                // redirect to Login Screen
                                activity.startActivity(Intent(activity, Login::class.java))
                                activity.finish()
                            }
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
                            CustomToast.showToast(activity, message)
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

fun validateData(activity: Activity, username: String, email: String, password: String): Boolean {
    // validate username
    val usernameRegex = "^(?=.{3,20}\$)[a-zA-Z0-9_.]+\$"
    val emailRegex = "^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    val passwordRegex = "^(?=.*\\d)(?=.*[!@#\$%^&*])(?=.*[a-z])(?=.*[A-Z]).{6,}\$"

    if (!username.matches(usernameRegex.toRegex())) {
        CustomToast.showToast(activity, "Invalid username")
        return false
    }
    if (!email.matches(emailRegex.toRegex())) {
        CustomToast.showToast(activity, "Invalid email")
        return false
    }
    if (password.length < 6) {
        CustomToast.showToast(activity, "Password must be atleast 6 characters long")
        return false
    }
    println(password.matches(passwordRegex.toRegex()))
    if (!password.matches(passwordRegex.toRegex())) {
        println(password)
        CustomToast.showToast(
            activity,
            "Password must contain atleast 1 uppercase, 1 lowercase, 1 number and 1 special character"
        )
        return false
    }
    return true
}
