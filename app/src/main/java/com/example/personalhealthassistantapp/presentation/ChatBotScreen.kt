package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R
import com.example.personalhealthassistantapp.data.model.MessageModel
import com.example.personalhealthassistantapp.presentation.ui.bot
import com.example.personalhealthassistantapp.presentation.ui.user
import com.example.personalhealthassistantapp.presentation.viewmodel.ChatViewModel


@Composable
fun ChatBotScreen(navController: NavController,  viewModel: ChatViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.backgroundColor))
    ) {
        ChatBoxHeader()
        ChatBox(
            modifier = Modifier
                .weight(1F),
            messageList = viewModel.messageList
        )
        MessageInput { question ->
            viewModel.sendMsg(question)
        }
    }
}

@Composable
fun ChatBox(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    LazyColumn(modifier = modifier) {
        items(messageList.reversed().size) {
            MessageDesign(messageList[it])
        }
    }
}

@Composable
fun MessageDesign(message: MessageModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .background(
                    color = if (message.isUser) user else bot,
                    shape = RoundedCornerShape(12.dp)
                )
                .wrapContentWidth() // Prevents bubble from stretching too much
                .padding(horizontal = 12.dp, vertical = 8.dp) // Adds space inside the bubble
        ) {
            Text(
                text = message.message,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp) // Ensures text doesn't touch edges
            )
        }
    }
}


@Composable
fun MessageInput(sendMessage: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), // Adds padding around the input field
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp), // Ensures spacing between text field and button
            label = { Text("Enter message") }
        )

        IconButton(onClick = {
            if (message.isNotBlank()) {
                sendMessage(message.trim())
                message = ""
                keyboardController?.hide()
            }
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}


@Composable
fun ChatBoxHeader(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.btn_color)),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = "Your Assistance",
            modifier.padding(horizontal = 20.dp , vertical = 25.dp),
            color = Color.White,
            fontSize = 20.sp
        )
    }
}