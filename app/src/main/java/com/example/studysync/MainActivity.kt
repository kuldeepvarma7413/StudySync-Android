package com.example.studysync

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.security.ConfirmationPrompt
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.studysync.screens.Login
import com.example.studysync.ui.theme.StudySyncTheme
import com.example.studysync.utils.SharedPrefManager
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudySyncTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TopBar(this, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(context: Context, modifier: Modifier = Modifier) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                "StudySync"
            )
        },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(text = { Text("Report") }, onClick = {
                    // Handle option 1 click
                    menuExpanded = false
//                    context.startActivity(Intent(context, Report::class.java))
                })
                DropdownMenuItem(text = { Text("Logout") }, onClick = {
                    // confirm if user wants to logout
                    showLogoutDialog = true
                    menuExpanded = false
                })
            }
        }
    )
    if (showLogoutDialog) {
        AlertDialog(onDismissRequest = {
            showLogoutDialog = false
        }, confirmButton = {
            Button(onClick = {
                showLogoutDialog = false
                val sharedPref = SharedPrefManager(context)
                sharedPref.clearToken()
                context.startActivity(Intent(context, Login::class.java))
                (context as Activity).finish()
            }) {
                Text(text = "Ok")
            }
        }, dismissButton = {
            TextButton(onClick = { showLogoutDialog = false }) {
                Text(text = "Cancel")
            }
        }, text = {
            Row (horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                Text(text = "Are you sure to Logout?", color = Color.DarkGray, modifier = Modifier.padding(start = 5.dp))
            }
        },
            // below line is used to add padding to our alert dialog
            modifier = Modifier.padding(16.dp),

            // below line is used to add rounded corners to our alert dialog
            shape = RoundedCornerShape(16.dp),

            // below line is used to add background color to our alert dialog
            containerColor = Color.White,

            // below line is used to add icon color to our alert dialog
            iconContentColor = Color.Red,

            // below line is used to add title color to our alert dialog
            titleContentColor = Color.Black,

            // below line is used to add text color to our alert dialog
            textContentColor = Color.DarkGray,

            // below line is used to add elevation to our alert dialog
            tonalElevation = 8.dp,

            // below line is used to add dialog properties to our alert dialog
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudySyncTheme {
        Greeting("Android")
    }
}