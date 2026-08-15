package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.SavedFile
import com.example.ui.theme.DarkBorderGlow
import com.example.ui.theme.DarkObsidianBg
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GlowingEmerald
import com.example.ui.theme.LaserPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.NovaViewModel

@Composable
fun FileVaultTab(viewModel: NovaViewModel) {
    val files by viewModel.savedFiles.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<SavedFile?>(null) }
    var showCreateFileDialog by remember { mutableStateOf(false) }

    val filteredFiles = files.filter {
        it.fileName.contains(searchQuery, ignoreCase = true) ||
                it.fileExtension.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkObsidianBg)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "File Vault",
                    tint = ElectricPurple,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "AI File Vault Center",
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ศูนย์จัดเก็บไฟล์ทั้งหมดที่ AI สร้างให้ออโต้",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("ค้นหาชื่อไฟล์ หรือ นามสกุล (.kt, .py, .json)...", color = TextMuted, fontSize = 13.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextMuted) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("file_search_input"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceCard,
                    unfocusedContainerColor = DarkSurfaceGlass,
                    focusedBorderColor = ElectricPurple,
                    unfocusedBorderColor = DarkBorderGlow,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Files Count
            Text(
                text = "ไฟล์ทั้งหมดในคลัง (${filteredFiles.size}):",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredFiles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = "Empty", tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("ยังไม่มีไฟล์ในคลังระบบ", color = TextMuted, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredFiles) { file ->
                        FileCardItem(
                            file = file,
                            onClick = { selectedFile = file },
                            onDelete = { viewModel.deleteFile(file.id) }
                        )
                    }
                }
            }
        }

        // FAB to create manual file
        FloatingActionButton(
            onClick = { showCreateFileDialog = true },
            containerColor = ElectricPurple,
            contentColor = TextWhite,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .testTag("create_file_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "New File")
        }
    }

    // Full File Content View Dialog
    selectedFile?.let { file ->
        Dialog(onDismissRequest = { selectedFile = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📄 ${file.fileName}",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        IconButton(onClick = { selectedFile = null }) {
                            Text("✕", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF020617))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = file.content,
                            color = Color(0xFF38BDF8),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText(file.fileName, file.content))
                                Toast.makeText(context, "คัดลอกเนื้อหาไฟล์สำเร็จ", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("คัดลอกเนื้อหา")
                        }
                    }
                }
            }
        }
    }

    // Create New File Dialog
    if (showCreateFileDialog) {
        var newFileName by remember { mutableStateOf("") }
        var newFileExt by remember { mutableStateOf("json") }
        var newFileContent by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showCreateFileDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📝 สร้างไฟล์ใหม่เข้าคลัง",
                        color = GlowingEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newFileName,
                        onValueChange = { newFileName = it },
                        label = { Text("ชื่อไฟล์ (เช่น config, script)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newFileExt,
                        onValueChange = { newFileExt = it },
                        label = { Text("นามสกุลไฟล์ (เช่น json, kt, py, md, txt)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newFileContent,
                        onValueChange = { newFileContent = it },
                        label = { Text("เนื้อหาในไฟล์...", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showCreateFileDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceGlass)
                        ) {
                            Text("ยกเลิก", color = TextWhite)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newFileName.isNotBlank()) {
                                    val full = if (newFileName.endsWith(".$newFileExt")) newFileName else "$newFileName.$newFileExt"
                                    viewModel.saveCustomFile(full, newFileExt, newFileContent)
                                    showCreateFileDialog = false
                                    Toast.makeText(context, "สร้างไฟล์ใหม่สำเร็จ!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GlowingEmerald)
                        ) {
                            Text("บันทึกไฟล์", color = Color(0xFF020617), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FileCardItem(
    file: SavedFile,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val badgeColor = when (file.fileExtension.lowercase()) {
        "kt", "kotlin" -> NeonCyan
        "py", "python" -> GlowingEmerald
        "json" -> LaserPink
        "js", "javascript" -> Color(0xFFF59E0B)
        "html" -> Color(0xFFEC4899)
        else -> ElectricPurple
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGlow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor.copy(alpha = 0.2f))
                        .border(1.dp, badgeColor, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ".${file.fileExtension.take(4).uppercase()}",
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = file.fileName,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ขนาด: ${file.sizeBytes} Bytes • AI Vault File",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = LaserPink,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
