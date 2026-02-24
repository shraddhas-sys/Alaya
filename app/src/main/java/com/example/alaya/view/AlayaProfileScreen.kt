package com.example.alaya.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.alaya.ui.theme.CardBg
import com.example.alaya.ui.theme.DangerRed
import com.example.alaya.ui.theme.DarkText
import com.example.alaya.ui.theme.ExtraDeepPurpleBody
import com.example.alaya.ui.theme.VeryLightPurpleHeader
import com.example.alaya.view.ui.theme.*
import com.example.alaya.viewmodel.ProfileViewModel // ViewModel import

class AlayaProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlayaTheme {
                ProfileScreen(
                    onBack = { finish() },
                    onLogout = {
                        finish()
                    }
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
    val username = viewModel.userName
    val email = viewModel.userEmail

    var showDialogType by remember { mutableStateOf<String?>(null) }
    var tempValue by remember { mutableStateOf("") }

    if (showDialogType != null) {
        AlertDialog(
            onDismissRequest = {
                showDialogType = null
                tempValue = ""
            },
            containerColor = CardBg,
            title = {
                val title = when(showDialogType) {
                    "name" -> "Update Username"
                    "email" -> "Update Email"
                    "password" -> "Change Password"
                    "logout" -> "Logout Account"
                    "delete" -> "Confirm Deletion"
                    else -> "Alaya"
                }
                Text(text = title, color = if(showDialogType == "delete") DangerRed else Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (showDialogType in listOf("name", "email", "password")) {
                        OutlinedTextField(
                            value = tempValue,
                            onValueChange = { tempValue = it },
                            label = { Text(if(showDialogType == "password") "New Password" else "Enter new value", color = Color.Gray) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF9C27B0)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        val msg = if(showDialogType == "logout") "Are you sure you want to log out?" else "This action is permanent. All data will be wiped."
                        Text(text = msg, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when(showDialogType) {
                            "name" -> {
                                showDialogType = null
                            }
                            "logout" -> onLogout()
                            "delete" -> onLogout()
                        }
                        showDialogType = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(showDialogType == "delete") DangerRed else Color(0xFF9C27B0)
                    )
                ) {
                    Text(if(showDialogType == "delete") "Delete Forever" else "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogType = null }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.5f))
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ExtraDeepPurpleBody)
            .verticalScroll(rememberScrollState())
    ) {
        // TOP HEADER
        Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = VeryLightPurpleHeader,
                shape = RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp)
            ) {}

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 50.dp, start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(35.dp).background(DarkText.copy(alpha = 0.08f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = DarkText, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Text("profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
                }

                Spacer(modifier = Modifier.height(25.dp))
                Surface(modifier = Modifier.size(105.dp), shape = CircleShape, color = ExtraDeepPurpleBody, shadowElevation = 10.dp) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = username.take(1).uppercase(), fontSize = 48.sp, fontWeight = FontWeight.Bold, color = VeryLightPurpleHeader)
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                Text(text = username, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = DarkText)
                Text(text = email, fontSize = 14.sp, color = DarkText.copy(alpha = 0.6f))
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Collection", fontWeight = FontWeight.Bold, color = VeryLightPurpleHeader.copy(alpha = 0.4f), fontSize = 13.sp)
            ProfileMenuRow(icon = Icons.Default.Favorite, label = "My Favorites", iconBg = Color(0xFFE91E63), onClick = { /* Fav Logic */ })

            Spacer(modifier = Modifier.height(4.dp))

            Text("Account Settings", fontWeight = FontWeight.Bold, color = VeryLightPurpleHeader.copy(alpha = 0.4f), fontSize = 13.sp)
            ProfileMenuRow(icon = Icons.Outlined.Person, label = "Change Username", iconBg = Color(0xFF9C27B0), onClick = { showDialogType = "name"; tempValue = username })
            ProfileMenuRow(icon = Icons.Outlined.Email, label = "Change Email", iconBg = Color(0xFF03A9F4), onClick = { showDialogType = "email"; tempValue = email })
            ProfileMenuRow(icon = Icons.Outlined.Lock, label = "Change Password", iconBg = Color(0xFFFF9800), onClick = { showDialogType = "password"; tempValue = "" })

            Spacer(modifier = Modifier.height(4.dp))

            Text("Danger Zone", fontWeight = FontWeight.Bold, color = DangerRed.copy(alpha = 0.8f), fontSize = 13.sp)
            ProfileMenuRow(icon = Icons.AutoMirrored.Filled.Logout, label = "Log out", iconBg = Color(0xFF607D8B), onClick = { showDialogType = "logout" })
            ProfileMenuRow(icon = Icons.Default.DeleteForever, label = "Delete Account", iconBg = Color(0xFFE53935), isDanger = true, onClick = { showDialogType = "delete" })
        }
    }
}

@Composable
fun ProfileMenuRow(icon: ImageVector, label: String, iconBg: Color, isDanger: Boolean = false, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF2D243F),
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = if(isDanger) Color(0xFFFF5252) else Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
        }
    }
}