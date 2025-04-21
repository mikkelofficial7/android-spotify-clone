package com.view.musicplayer.spotifyclone.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.RedGradientEnd
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreenGrey40
import com.view.musicplayer.spotifyclone.ui.theme.White80

@Composable
fun LoginScreen(onSubmit: (String, String, String) -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var isFullNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isAgeError by remember { mutableStateOf(false) }

    fun filterEmailInput(input: String): String {
        return input.filter { it.isLetterOrDigit() || it == '@' || it == '.' }
    }

    fun isValidEmail(input: String): Boolean {
        return input.contains(".") && input.contains("@")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black80)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(LocalContext.current.getString(R.string.login_more), color = White80, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                isFullNameError = fullName.isEmpty()
            },
            label = { Text(LocalContext.current.getString(R.string.fullname), color = if (isFullNameError) RedGradientEnd else White80) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = SpotifyAccent80,
                unfocusedTextColor = White80,
                focusedBorderColor = if (isFullNameError) RedGradientEnd else White80,
                unfocusedBorderColor = if (isFullNameError) RedGradientEnd else White80,
                cursorColor = SpotifyGreenGrey40
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = filterEmailInput(it)
                isEmailError = email.isEmpty()
            },
            label = { Text(LocalContext.current.getString(R.string.email), color = if (isEmailError) RedGradientEnd else White80) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = SpotifyAccent80,
                unfocusedTextColor = White80,
                focusedBorderColor = if (isEmailError) RedGradientEnd else White80,
                unfocusedBorderColor = if (isEmailError) RedGradientEnd else White80,
                cursorColor = SpotifyGreenGrey40
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { value ->
                if (value.all { it.isDigit() }) {
                    age = value
                }
                isAgeError = age.isEmpty()
            },
            label = { Text(LocalContext.current.getString(R.string.age), color = if (isAgeError) RedGradientEnd else White80) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = SpotifyAccent80,
                unfocusedTextColor = White80,
                focusedBorderColor = if (isAgeError) RedGradientEnd else White80,
                unfocusedBorderColor = if (isAgeError) RedGradientEnd else White80,
                cursorColor = SpotifyGreenGrey40
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                isFullNameError = false
                isEmailError = false
                isAgeError = false

                if (fullName.isEmpty()) {
                    isFullNameError = true
                }
                if (email.isEmpty() || !isValidEmail(email)) {
                    isEmailError = true
                }
                if (age.isEmpty()) {
                    isAgeError = true
                }

                if (!isFullNameError && !isAgeError && !isEmailError) {
                    onSubmit(fullName, email, age)
                }
            },
            colors = ButtonDefaults.run { buttonColors(SpotifyAccent80) },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(LocalContext.current.getString(R.string.login), color = Black80, fontWeight = FontWeight.Bold)
        }
    }
}
