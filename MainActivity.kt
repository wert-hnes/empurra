package com.empurra.editor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.empurra.editor.core.EditorProject
import com.empurra.editor.core.Layer
import com.empurra.editor.core.LayerType
import com.empurra.editor.core.Keyframe
import com.empurra.editor.core.AnimatedProperty
import com.empurra.editor.core.Easing
import com.empurra.editor.core.BezierCurve

private val Bg = Color(0xFF101116)
private val Panel = Color(0xFF1A1C24)
private val Accent = Color(0xFFB58BFF)
private val Muted = Color(0xFF85899A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EmpurraApp() }
    }
}

@Composable
fun EmpurraApp() {
    var project by remember { mutableStateOf(EditorProject.demo()) }
    var selectedLayer by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var playhead by remember { mutableFloatStateOf(2.4f) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg, surface = Panel, primary = Accent,
            onBackground = Color.White, onSurface = Color.White
        )
    ) {
        Column(
            Modifier.fillMaxSize().background(Bg).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("EMPURRA", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Text("MOTION STUDIO  •  PROJECT 001", color = Muted, fontSize = 10.sp, letterSpacing = 1.3.sp)
                }
                IconButton(onClick = { project = project.copy(layers = project.layers + Layer.video("Video ${project.layers.size + 1}")) }) {
                    Icon(Icons.Default.Add, "Add layer", tint = Accent)
                }
                IconButton(onClick = { }) { Icon(Icons.Default.Tune, "Tools", tint = Color.White) }
            }

            // Preview monitor placeholder; real compositing/render surface is a later milestone.
            Box(
                Modifier.fillMaxWidth().weight(1f).background(Color(0xFF08090D), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF2C2F3B), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("EMPURRA", color = Color(0xFF343746), fontSize = 27.sp, fontWeight = FontWeight.Black, letterSpacing = 5.sp)
                    Text("Preview monitor", color = Muted, fontSize = 12.sp)
                }
                Text("00:00:${(playhead * 10).toInt().toString().padStart(2, '0')}", Modifier.align(Alignment.BottomEnd).padding(12.dp), color = Color.White, fontSize = 11.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("TIMELINE", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp, modifier = Modifier.weight(1f))
                Text("00:00 / 00:15", color = Muted, fontSize = 11.sp)
                IconButton(onClick = { isPlaying = !isPlaying }) {
                    Icon(Icons.Default.PlayArrow, "Play", tint = Accent)
                }
                IconButton(onClick = { project = project.copy(layers = project.layers + Layer.audio("Audio ${project.layers.size + 1}")) }) {
                    Icon(Icons.Default.MusicNote, "Add audio", tint = Accent)
                }
            }

            // Timeline ruler
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                (0..15).forEach { second ->
                    Box(Modifier.width(56.dp).height(22.dp), contentAlignment = Alignment.TopStart) {
                        Text("00:${second.toString().padStart(2, '0')}", color = Muted, fontSize = 9.sp)
                    }
                }
            }

            Column(Modifier.weight(0.85f).fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(8.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                project.layers.forEachIndexed { index, layer ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(layer.name, color = if (selectedLayer == index) Accent else Color.White, fontSize = 11.sp, modifier = Modifier.width(72.dp))
                        Box(
                            Modifier.weight(1f).height(35.dp)
                                .background(layer.color, RoundedCornerShape(6.dp))
                                .border(if (selectedLayer == index) 1.dp else 0.dp, if (selectedLayer == index) Accent else Color.Transparent, RoundedCornerShape(6.dp))
                        ) {
                            Row(Modifier.fillMaxSize().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(layer.type.name, color = Color.White.copy(alpha = 0.85f), fontSize = 9.sp)
                                Spacer(Modifier.weight(1f))
                                if (layer.keyframes.isNotEmpty()) Text("◆  ◆", color = Color.White, fontSize = 10.sp)
                            }
                        }
                        TextButton(onClick = { selectedLayer = index }) { Text("EDIT", color = Accent, fontSize = 9.sp) }
                    }
                }
            }

            val active = project.layers.getOrNull(selectedLayer)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ToolChip("KEYFRAME", Modifier.weight(1f)) {
                    if (active != null) {
                        val updated = active.copy(keyframes = active.keyframes + Keyframe(playhead.toDouble(), 1.0))
                        project = project.copy(layers = project.layers.toMutableList().also { it[selectedLayer] = updated })
                    }
                }
                ToolChip("BEZIER", Modifier.weight(1f)) { }
                ToolChip("SPEED", Modifier.weight(1f)) { }
                ToolChip("FX", Modifier.weight(1f)) { }
            }
            Text("Selected: ${active?.name ?: "none"}  •  ${active?.keyframes?.size ?: 0} keyframes", color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ToolChip(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(42.dp), contentPadding = PaddingValues(horizontal = 4.dp)) {
        Text(label, fontSize = 9.sp, letterSpacing = 0.5.sp)
    }
}
