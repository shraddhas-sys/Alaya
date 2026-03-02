package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alaya.ui.theme.AlayaDeepPurple
import com.example.alaya.ui.theme.AlayaNudeCream
import com.example.alaya.view.ui.theme.AlayaTheme
import com.example.alaya.viewmodel.ProfileViewModel

class AlayaProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                ProfileScreen(
                    onBack = { finish() },
                    onLogout = { finish() }
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    // Get user data from ViewModel
    val username = viewModel.userName
    val email = viewModel.userEmail

// State to handle which action dialog to show
    var showDialogType by remember { mutableStateOf<String?>(null) }
    var tempValue by remember { mutableStateOf("") }

    val primaryColor = AlayaDeepPurple
    val backgroundColor = AlayaNudeCream

    // Dynamic AlertDialog for all profile actions

    if (showDialogType != null) {
        AlertDialog(
            onDismissRequest = { showDialogType = null; tempValue = "" },
            containerColor = Color.White,
            title = {
                val title = when(showDialogType) {
                    "name" -> "Update Username"
                    "password" -> "Change Password"
                    "logout" -> "Logout Account"
                    "delete" -> "Confirm Account Deletion"
                    "email_info" -> "Email Info"
                    else -> ""
                }
                Text(text = title, color = if(showDialogType == "delete") Color.Red else primaryColor, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (showDialogType in listOf("name", "password")) {
                        OutlinedTextField(
                            value = tempValue,
                            onValueChange = { tempValue = it },
                            label = { Text("Enter value") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                focusedLabelColor = primaryColor
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        val msg = when(showDialogType) {
                            "logout" -> "Are you sure you want to log out?"
                            "delete" -> "This action is permanent. All your data will be deleted."
                            "email_info" -> "You cannot change this email address."
                            else -> ""
                        }
                        Text(text = msg, color = Color.Gray)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when(showDialogType) {
                            "name" -> if(tempValue.isNotBlank()) viewModel.updateName(tempValue)
                            "password" -> if(tempValue.length >= 6) viewModel.updatePassword(tempValue)
                            "logout" -> viewModel.logout(onLogout)
                            "delete" -> viewModel.deleteAccount(onLogout)
                        }
                        showDialogType = null
                        tempValue = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if(showDialogType == "delete") Color.Red else primaryColor)
                ) {
                    Text(if(showDialogType == "email_info") "Okay" else "Confirm")
                }
            },
            dismissButton = {
                if (showDialogType != "email_info") {
                    TextButton(onClick = { showDialogType = null }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor).verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = primaryColor,
                shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)
            ) {}

            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 50.dp, start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.White)
                    }
                    Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = username.take(1).uppercase(),
                            fontSize = 40.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(username, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(email, fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }

        // Settings part
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Text("Account Details", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            ProfileMenuRow(Icons.Outlined.Email, "Email: $email", primaryColor.copy(alpha = 0.1f), primaryColor) {
                showDialogType = "email_info"
            }

            Text("Settings", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            ProfileMenuRow(Icons.Outlined.Person, "Change Username", primaryColor.copy(alpha = 0.1f), primaryColor) {
                tempValue = username
                showDialogType = "name"
            }
            ProfileMenuRow(Icons.Outlined.Lock, "Change Password", primaryColor.copy(alpha = 0.1f), primaryColor) {
                tempValue = ""
                showDialogType = "password"
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("Danger Zone", color = Color.Red.copy(alpha = 0.7f), fontSize = 13.sp, fontWeight = FontWeight.Bold)

            ProfileMenuRow(Icons.Default.DeleteForever, "Delete Account", Color.Red.copy(alpha = 0.1f), Color.Red) {
                showDialogType = "delete"
            }
            ProfileMenuRow(Icons.AutoMirrored.Filled.Logout, "Log out", Color.Gray.copy(alpha = 0.1f), Color.Gray) {
                showDialogType = "logout"
            }
        }
    }
}

// Profile menu part
@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    label: String,
    iconBg: Color,
    tintColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        color = Color.White
    ) {
        Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(35.dp).background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = label,
                color = if(label == "Delete Account") Color.Red else AlayaDeepPurple,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            if (!label.startsWith("Email")) {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
            }
        }
    }
}