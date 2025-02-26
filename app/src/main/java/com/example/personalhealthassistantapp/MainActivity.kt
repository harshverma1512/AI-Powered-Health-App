package com.example.personalhealthassistantapp

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.personalhealthassistantapp.ui.theme.PersonalHealthAssistantAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<ChatViewModel>()
            PersonalHealthAssistantAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ChatBoxHeader()
                        ChatBox(
                            modifier = Modifier
                                .padding(innerPadding)
                                .weight(1F),
                            messageList = viewModel.messageList
                        )
                        MessageInput{ question ->
                            viewModel.sendMsg(question)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBox(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    LazyColumn(modifier = modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(messageList.reversed().size) {
            Text(text = messageList[it].message, modifier = Modifier.padding(10.dp), color = Color.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun MessageInput(sendMessage : (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .weight(1f) // This makes it take all available space
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 5.dp),
            label = { Text("Enter message") }
        )

        IconButton(
            onClick = {
                sendMessage(message)
                message = ""
            },
            modifier = Modifier.padding(end = 10.dp) // Optional padding for better spacing
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }

}

@Composable
fun ChatBoxHeader(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary), contentAlignment = Alignment.CenterStart
    ) {
        Text(text = "Healthy Bot", modifier.padding(20.dp), color = Color.White, fontSize = 20.sp)
    }
}