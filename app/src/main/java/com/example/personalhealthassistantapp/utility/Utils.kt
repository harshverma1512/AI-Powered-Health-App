package com.example.personalhealthassistantapp.utility


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.personalhealthassistantapp.R


object Utils {

    const val GEMINI_KEY = "AIzaSyBdEME3G08fi4tMdMLIasIQjeVuUfv2IDE"


    @Composable
    fun BackBtn(
        onBackClick: () -> Unit = {},
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_icon),
            contentDescription = "",
            modifier = Modifier.clickable {
                onBackClick()
            })
    }
}