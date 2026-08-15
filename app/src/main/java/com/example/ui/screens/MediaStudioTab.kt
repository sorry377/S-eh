package com.example.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayCircleFilled
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GeneratedMedia
import com.example.ui.theme.DarkBorderGlow
import com.example.ui.theme.DarkObsidianBg
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.LaserPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.NovaViewModel

@Composable
fun MediaStudioTab(viewModel: NovaViewModel) {
    val mediaPrompt by viewModel.mediaPrompt.collectAsState()
    val mediaType by viewModel.mediaType.collectAsState()
    val mediaStyle by viewModel.mediaStyle.collectAsState()
    val mediaList by viewModel.generatedMediaList.collectAsState()
    val isLoading by viewModel.isMediaLoading.collectAsState()

    var selectedMediaForView by remember { mutableStateOf<GeneratedMedia?>(null) }

    val styles = listOf("Cyberpunk", "Anime", "Photorealistic 3D", "Cinematic", "Minimalist", "Fantasy Art")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkObsidianBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = "Media Studio",
                tint = LaserPink,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "AI Visual & Motion Studio",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "เนรมิตรูปภาพและสคริปต์วีดีโอ AI สุดเท่จากคำอธิบาย",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Type Switcher (IMAGE vs VIDEO)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(DarkSurfaceGlass)
                .border(1.dp, DarkBorderGlow, RoundedCornerShape(24.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (mediaType == "IMAGE") Brush.horizontalGradient(listOf(LaserPink, ElectricPurple)) else Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)))
                    .clickable { viewModel.mediaType.value = "IMAGE" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = "Image", tint = TextWhite, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("สร้างรูปภาพ AI", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (mediaType == "VIDEO") Brush.horizontalGradient(listOf(NeonCyan, ElectricPurple)) else Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)))
                    .clickable { viewModel.mediaType.value = "VIDEO" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Movie, contentDescription = "Video", tint = TextWhite, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("สร้างวีดีโอ AI (Veo)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Style selector
        if (mediaType == "IMAGE") {
            Text("เลือกสไตล์ภาพศิลปะ:", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(styles) { style ->
                    val isSelected = style == mediaStyle
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Brush.horizontalGradient(listOf(LaserPink, NeonCyan)) else Brush.horizontalGradient(listOf(DarkSurfaceGlass, DarkSurfaceCard)))
                            .border(1.dp, if (isSelected) LaserPink else DarkBorderGlow, RoundedCornerShape(20.dp))
                            .clickable { viewModel.mediaStyle.value = style }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = style,
                            color = if (isSelected) Color(0xFF020617) else TextWhite,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Prompt Input
        OutlinedTextField(
            value = mediaPrompt,
            onValueChange = { viewModel.mediaPrompt.value = it },
            label = { Text(if (mediaType == "IMAGE") "อธิบายรูปภาพที่ต้องการ เช่น แมวอวกาศแสงนีออน..." else "อธิบายฉากวีดีโอ เช่น หุ่นยนต์แข่งรถลอยฟ้า...", color = TextMuted) },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .testTag("media_prompt_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceCard,
                unfocusedContainerColor = DarkSurfaceGlass,
                focusedBorderColor = if (mediaType == "IMAGE") LaserPink else NeonCyan,
                unfocusedBorderColor = DarkBorderGlow,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Generate Button
        Button(
            onClick = { viewModel.generateMedia() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("generate_media_button"),
            enabled = !isLoading && mediaPrompt.isNotBlank(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (mediaType == "IMAGE") LaserPink else NeonCyan,
                disabledContainerColor = DarkSurfaceCard
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color(0xFF020617),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("กำลังสร้างมีเดียประมวลผลเร็ว...", color = Color(0xFF020617), fontWeight = FontWeight.Bold)
            } else {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Generate", tint = Color(0xFF020617))
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (mediaType == "IMAGE") "เนรมิตรูปภาพ AI" else "สร้างสคริปต์ & วีดีโอ AI", color = Color(0xFF020617), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Gallery Header
        Text("คลังผลงานมีเดียที่สร้างไว้ (${mediaList.size}):", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (mediaList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceGlass)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("ยังไม่มีรูปภาพหรือวีดีโอ ให้พิมพ์คำอธิบายแล้วกดเนรมิตได้เลย!", color = TextMuted, fontSize = 13.sp)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(mediaList) { item ->
                    MediaCardItem(
                        media = item,
                        onClick = { selectedMediaForView = item },
                        onDelete = { viewModel.deleteMedia(item.id) }
                    )
                }
            }
        }
    }

    // Full Screen Media Preview Dialog
    selectedMediaForView?.let { media ->
        Dialog(onDismissRequest = { selectedMediaForView = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurfaceCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (media.mediaType == "IMAGE") "🖼️ AI Generated Image" else "🎬 AI Video Storyboard",
                            color = LaserPink,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        IconButton(onClick = { selectedMediaForView = null }) {
                            Text("✕", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Base64 image render
                    val bitmap = remember(media.mediaUrl) {
                        try {
                            val cleanBase64 = media.mediaUrl.substringAfter("base64,")
                            val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Generated Media",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "รายละเอียดคำสั่ง:",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = media.prompt,
                        color = TextWhite,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MediaCardItem(
    media: GeneratedMedia,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val bitmap = remember(media.mediaUrl) {
        try {
            val cleanBase64 = media.mediaUrl.substringAfter("base64,")
            val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceGlass),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGlow)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = media.prompt,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Top Type badge
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = if (media.mediaType == "IMAGE") "IMAGE" else "VIDEO",
                    color = if (media.mediaType == "IMAGE") LaserPink else NeonCyan,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Play overlay for video
            if (media.mediaType == "VIDEO") {
                Icon(
                    imageVector = Icons.Default.PlayCircleFilled,
                    contentDescription = "Play",
                    tint = TextWhite.copy(alpha = 0.9f),
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                )
            }

            // Bottom delete action
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .padding(4.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = LaserPink,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
