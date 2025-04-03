package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalhealthassistantapp.R

@Composable
fun SelectAvatarScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FE)) // Light background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back press */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Select Avatar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        // Avatar Selection Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AvatarItem(selected = false)
            AvatarItem(selected = true)
            AvatarItem(selected = false)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title & Description
        Text(
            text = "Select Avatar 🙌",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "We have 25 premade avatars for your convenience. Kindly choose one of them!",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Upload Image Section
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable {

                }
                .border(2.dp, Color(0xFF406AFF), RoundedCornerShape(12.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Upload",
                tint = Color(0xFF406AFF),
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Or upload your image locally",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Max size: 5mb, Format: jpg, png",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = { /* Handle Continue */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF406AFF)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(painter = painterResource(id = R.drawable.monotone_arrow_right_md), contentDescription = "Next")
        }
    }
}

@Composable
fun AvatarItem(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) Color(0xFF406AFF) else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Avatar",
            tint = if (selected) Color(0xFF406AFF) else Color.Gray,
            modifier = Modifier.size(70.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSelectAvatarScreen() {
    SelectAvatarScreen()
}
