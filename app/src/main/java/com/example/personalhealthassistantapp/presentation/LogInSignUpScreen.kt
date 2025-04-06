package com.example.personalhealthassistantapp.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalhealthassistantapp.R

@Composable
fun LoginSignupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authType: Boolean,
) {

    val authenticationTypeLogin = rememberSaveable {
        mutableStateOf(authType)
    }
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val matched = rememberSaveable {
        mutableStateOf(true)
    }
    val confirmPassword = rememberSaveable {
        mutableStateOf("")

    }

    Column(
        modifier = modifier
            .background(color = colorResource(id = R.color.backgroundColor))
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(250.dp), // Adjust height as needed
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.btn_color) // Replace with your color
            )
        ) {
            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_plus), // Replace with your drawable
                        contentDescription = "Logo", modifier = modifier.size(60.dp)
                    )
                    Spacer(modifier = modifier.height(8.dp))
                    Text(
                        text = if (authenticationTypeLogin.value) LocalContext.current.getString(R.string.sign_in) else LocalContext.current.getString(
                            R.string.sign_up_for_free
                        ),
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Default
                    )
                }
            }
        }

        Column(modifier.padding(horizontal = 15.dp, vertical = 25.dp)) {
            Text(
                text = "Email Address",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Spacer(modifier = modifier.height(7.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = "Email Icon"
                    )
                },
            )

            Spacer(modifier = modifier.height(20.dp))

            Text(
                text = "Password",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Spacer(modifier = modifier.height(7.dp))

            OutlinedTextField(value = password.value,
                onValueChange = {
                    password.value = it
                },
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ock),
                        contentDescription = "Email Icon"
                    )
                },
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_off),
                        contentDescription = "Email Icon"
                    )
                })


            if (!authenticationTypeLogin.value) {

                Spacer(modifier = modifier.height(20.dp))

                Text(
                    text = "Password Confirmation",
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )

                Spacer(modifier = modifier.height(7.dp))

                OutlinedTextField(value = confirmPassword.value,
                    onValueChange = {
                        confirmPassword.value = it
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .background(color = Color.White),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ock),
                            contentDescription = "Email Icon"
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.eye_off),
                            contentDescription = "Email Icon"
                        )
                    })

                Spacer(modifier = modifier.height(8.dp))

                if (!matched.value) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3F3), // Light pink background
                        ), border = BorderStroke(1.dp, Color(0xFFFFC0C0)), // Light red border
                        shape = RoundedCornerShape(8.dp), // Rounded corners
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error Icon",
                                tint = Color(0xFFE53935), // Red color
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ERROR: Passwords do not match!",
                                color = Color(0xFF333333), // Darker text color
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }

                Button(
                    onClick = {
                        if (confirmPassword.value != password.value && !authenticationTypeLogin.value) {
                            matched.value = false
                            return@Button
                        }
                        navController.navigate(ScreensName.SelectAvatarScreen.name)
                    },
                    modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.btn_color)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (authenticationTypeLogin.value) LocalContext.current.getString(R.string.sign_in) else LocalContext.current.getString(
                            R.string.sign_up
                        ), fontWeight = FontWeight.Bold, color = Color.White,fontSize = 18.sp
                    )
                    Spacer(modifier = modifier.width(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.monotone_arrow_right_md),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = modifier.height(10.dp))

                val annotatedText = buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append(
                            if (!authenticationTypeLogin.value) LocalContext.current.getString(R.string.sign_in) else LocalContext.current.getString(
                                R.string.sign_up
                            )
                        )
                    }
                }

                val context = LocalContext.current
                Text(text = annotatedText,
                    modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            if (annotatedText.text.contains(context.getString(R.string.sign_in))) {
                                navController.navigate(ScreensName.LoginScreen.name)
                            } else {
                                navController.navigate(ScreensName.RegisterScreen.name)
                            }
                        })

        }
    }
}