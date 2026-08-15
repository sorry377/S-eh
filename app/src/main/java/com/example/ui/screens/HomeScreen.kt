package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorderGlow
import com.example.ui.theme.DarkObsidianBg
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GlowingEmerald
import com.example.ui.theme.LaserPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.NovaViewModel

@Composable
fun HomeScreen(viewModel: NovaViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()

    Scaffold(
        topBar = {
            TopBrandBar(
                onClearChat = { viewModel.clearChatHistory() }
            )
        },
        bottomBar = {
            NovaBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectedTab.value = it }
            )
        },
        containerColor = DarkObsidianBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ChatTab(viewModel)
                1 -> CodeStudioTab(viewModel)
                2 -> MediaStudioTab(viewModel)
                3 -> FileVaultTab(viewModel)
            }
        }
    }
}

@Composable
fun TopBrandBar(onClearChat: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceGlass)
            .border(1.dp, DarkBorderGlow)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(NeonCyan, LaserPink))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "Nova Core",
                tint = TextWhite,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Nova AI",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GlowingEmerald.copy(alpha = 0.2f))
                        .border(1.dp, GlowingEmerald, RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "FAST PRECISE",
                        color = GlowingEmerald,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = "ถามตอบ • โค้ด • รูป • วีดีโอ • สั่งด้วยเสียง",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        IconButton(
            onClick = onClearChat,
            modifier = Modifier.testTag("clear_chat_button")
        ) {
            Icon(
                imageVector = Icons.Default.DeleteSweep,
                contentDescription = "Clear Chat",
                tint = LaserPink
            )
        }
    }
}

@Composable
fun NovaBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = DarkSurfaceGlass,
        contentColor = TextWhite,
        tonalElevation = 8.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(1.dp, DarkBorderGlow)
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(imageVector = Icons.Default.Chat, contentDescription = "Chat") },
            label = { Text("ถามตอบ", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonCyan,
                selectedTextColor = NeonCyan,
                indicatorColor = NeonCyan.copy(alpha = 0.2f),
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_chat")
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(imageVector = Icons.Default.Code, contentDescription = "Code") },
            label = { Text("เขียนโค้ด", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GlowingEmerald,
                selectedTextColor = GlowingEmerald,
                indicatorColor = GlowingEmerald.copy(alpha = 0.2f),
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_code")
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(imageVector = Icons.Default.Palette, contentDescription = "Media") },
            label = { Text("รูป&วีดีโอ", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LaserPink,
                selectedTextColor = LaserPink,
                indicatorColor = LaserPink.copy(alpha = 0.2f),
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_media")
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(imageVector = Icons.Default.Folder, contentDescription = "Files") },
            label = { Text("คลังไฟล์", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricPurple,
                selectedTextColor = ElectricPurple,
                indicatorColor = ElectricPurple.copy(alpha = 0.2f),
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_files")
        )
    }
}
