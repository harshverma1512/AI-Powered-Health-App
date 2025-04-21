package com.example.personalhealthassistantapp.utility

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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