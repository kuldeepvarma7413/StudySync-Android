package com.example.studysync.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4E3366),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF000000),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4E3366),
    secondary = darkPurple,
    tertiary = darkPurple,

    // manual colors (hex values)
    onPrimary = Color(0xFF4E3366),
    background = Color(0xFFFFFFFF),
    //Other default colors to override
    surface = Color(0xFF4E3366),
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun StudySyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val ButtonCustomised
@Composable
get() = ButtonDefaults.buttonColors(
    contentColor = white,
    containerColor = darkPurple
)

val TextButtonCustomised
@Composable
get() = ButtonDefaults.textButtonColors(
    contentColor = MaterialTheme.colorScheme.primary,
    containerColor = Color.Transparent
)

val TextPurple
@Composable
get() = TextStyle(
    color = MaterialTheme.colorScheme.primary,
)

@OptIn(ExperimentalMaterial3Api::class)
val OutlinedTextFieldCustomised

@Composable
get() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.primary
)