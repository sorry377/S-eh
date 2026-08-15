package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkBorderGlow
import com.example.ui.theme.DarkObsidianBg
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GlowingEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.NovaViewModel

@Composable
fun CodeStudioTab(viewModel: NovaViewModel) {
    val codePrompt by viewModel.codePrompt.collectAsState()
    val codeLanguage by viewModel.codeLanguage.collectAsState()
    val generatedCode by viewModel.generatedCode.collectAsState()
    val isLoading by viewModel.isCodeLoading.collectAsState()

    val context = LocalContext.current
    var showPreviewDialog by remember { mutableStateOf(false) }

    val languages = listOf("Kotlin", "Python", "JavaScript", "HTML", "SQL", "C++", "Rust", "JSON")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkObsidianBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = "Code Studio",
                tint = GlowingEmerald,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "AI Code Generator",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "เขียนโปรแกรมคุณภาพสูงได้ทุกภาษา สะดวกรวดเร็ว",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language Selectors
        Text(
            text = "เลือกภาษาโปรแกรม:",
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(languages) { lang ->
                val isSelected = lang.equals(codeLanguage, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Brush.horizontalGradient(listOf(GlowingEmerald, NeonCyan)) else Brush.horizontalGradient(listOf(DarkSurfaceGlass, DarkSurfaceCard)))
                        .border(1.dp, if (isSelected) GlowingEmerald else DarkBorderGlow, RoundedCornerShape(20.dp))
                        .clickable { viewModel.codeLanguage.value = lang }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = lang,
                        color = if (isSelected) Color(0xFF020617) else TextWhite,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Code Prompt Input
        OutlinedTextField(
            value = codePrompt,
            onValueChange = { viewModel.codePrompt.value = it },
            label = { Text("อธิบายโจทย์หรือระบบที่ต้องการให้เขียนโค้ด...", color = TextMuted) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .testTag("code_prompt_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceCard,
                unfocusedContainerColor = DarkSurfaceGlass,
                focusedBorderColor = GlowingEmerald,
                unfocusedBorderColor = DarkBorderGlow,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Generate Code Button
        Button(
            onClick = { viewModel.generateCode() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_code_button"),
            enabled = !isLoading && codePrompt.isNotBlank(),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GlowingEmerald,
                disabledContainerColor = DarkSurfaceCard
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFF020617),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("กำลังเขียนโค้ดและทดสอบ...", color = Color(0xFF020617), fontWeight = FontWeight.Bold)
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generate",
                    tint = Color(0xFF020617)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("สร้างโค้ด $codeLanguage", color = Color(0xFF020617), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generated Code View
        if (generatedCode.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16)),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlowingEmerald.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ผลลัพธ์โค้ด ($codeLanguage)",
                            color = GlowingEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Row {
                            if (codeLanguage.equals("HTML", ignoreCase = true) || codeLanguage.equals("JavaScript", ignoreCase = true)) {
                                IconButton(
                                    onClick = { showPreviewDialog = true },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Run Preview",
                                        tint = NeonCyan
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Generated Code", generatedCode))
                                    Toast.makeText(context, "คัดลอกโค้ดแล้ว", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = TextWhite
                                )
                            }

                            IconButton(
                                onClick = {
                                    val ext = when(codeLanguage.lowercase()) {
                                        "kotlin" -> "kt"
                                        "python" -> "py"
                                        "javascript" -> "js"
                                        "html" -> "html"
                                        "json" -> "json"
                                        "sql" -> "sql"
                                        else -> "txt"
                                    }
                                    viewModel.saveCustomFile(
                                        fileName = "Code_${System.currentTimeMillis() % 10000}.$ext",
                                        fileExtension = ext,
                                        content = generatedCode
                                    )
                                    Toast.makeText(context, "บันทึกลงศูนย์เก็บไฟล์แล้ว!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Save",
                                    tint = ElectricPurple
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF020617))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = generatedCode,
                            color = Color(0xFF38BDF8),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }

    // HTML / JS Interactive Preview Dialog
    if (showPreviewDialog && generatedCode.isNotBlank()) {
        Dialog(onDismissRequest = { showPreviewDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚡ Live Code Preview",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                loadDataWithBaseURL(null, generatedCode, "text/html", "UTF-8", null)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { showPreviewDialog = false },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
                    ) {
                        Text("ปิดหน้าต่าง")
                    }
                }
            }
        }
    }
}
