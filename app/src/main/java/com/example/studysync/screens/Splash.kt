package com.example.studysync.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studysync.MainActivity
import com.example.studysync.ui.theme.StudySyncTheme
import com.example.studysync.ui.theme.darkPurple
import com.example.studysync.ui.theme.white
import com.example.studysync.utils.SharedPrefManager

class Splash : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudySyncTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = darkPurple
                ) { innerPadding ->
                    Logo(modifier = Modifier.padding(innerPadding))
                }
            }

            // keyStore
            val sharedPrefManager = SharedPrefManager(this)
            val token = sharedPrefManager.getToken()

            Handler().postDelayed(
                {
                    if (token != null){
                        startActivity(Intent(this, MainActivity::class.java))
                    }else{
                        startActivity(Intent(this, Login::class.java))
                    }
                    finish()
                }, 2000
            )
        }
    }
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    var alpha by remember { mutableStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "StudySync",
            modifier = Modifier.alpha(animatedAlpha),
            style = TextStyle(
                fontSize = 48.sp,
                color = white
            )
        )
        Text(
            text = "Enhanced Notes Management",
            modifier = Modifier
                .alpha(animatedAlpha)
                .padding(top = 8.dp),
            style = TextStyle(
                fontSize = 16.sp,
                color = white
            )
        )
    }

    // Start the animation after a delay
    LaunchedEffect(Unit) {
        alpha = 1f
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudySyncTheme {
        Logo()
    }
}