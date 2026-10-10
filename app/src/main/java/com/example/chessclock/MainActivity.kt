package com.example.chessclock

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessclock.ui.theme.ChessClockTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessClockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChessClockScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun ChessClockScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("chess_clock",Context.MODE_PRIVATE) }
    var moves1 by remember { mutableIntStateOf(0) }
    var moves2 by remember { mutableIntStateOf(0) }
    var isPaused by remember { mutableStateOf(true) }
    var activePlayer by remember { mutableIntStateOf(1) }
    var showSettings by remember { mutableStateOf(false) }
    var startTime by remember { mutableIntStateOf(prefs.getInt("start_time", 600)) }
    var soundOn by remember { mutableStateOf(prefs.getBoolean("sound_on",true)) }
    var increment by remember { mutableIntStateOf(prefs.getInt("increment", 5)) }
    var time1 by remember { mutableIntStateOf(startTime) }
    var time2 by remember { mutableIntStateOf(startTime) }
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC,80) }
    DisposableEffect(Unit) {
        onDispose { toneGenerator.release() }
    }
    fun playClick (){
        if(soundOn) toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 80)
    }
    fun resetGame() {
        time1 = startTime
        time2 = startTime
        moves1 = 0
        moves2 = 0
        activePlayer = 1
        isPaused = true
    }

    if (showSettings){
       AlertDialog(
            onDismissRequest = {showSettings = false},
           title = { Text("Réglages")  },
           text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Durée",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.ExtraBold ,
                        fontSize = 20.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
                    {
                        listOf(1 , 3 , 5 , 10).forEach { minutes ->
                            OptionButton(
                                label = "$minutes min",
                                selected = startTime == minutes * 60,
                                onClick = {
                                    startTime = minutes * 60
                                    prefs.edit { putInt("start_time", startTime) }
                                    resetGame()
                                }
                            )
                        }
                    }
                    Text(
                       text ="Incrementation" ,
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.ExtraBold ,
                        fontSize = 20.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
                    {
                        listOf(0 , 2 , 5 , 10 ).forEach { incr ->
                            OptionButton(
                                label = "$incr s",
                                selected = increment == incr,
                                onClick = {
                                    increment = incr
                                    prefs.edit { putInt("increment", increment) }
                                    resetGame()
                                }
                            )

                        }
                    }
                }
           },
           confirmButton = {
               TextButton(onClick = {showSettings = false}) { Text("OK")}
           }

        )
    }


    LaunchedEffect(isPaused , activePlayer) {
        while(!isPaused){
            delay(1000.milliseconds)
            if (activePlayer == 1) time1-- else time2--
            if (time1 <= 0 || time2 <= 0) isPaused = true
        }
    }
       Column(
           modifier = modifier.fillMaxSize(),
       ) {
                        PlayerZone(
                            playerName = "Player1",
                            time = formatTime(time1),
                            moves = moves1,
                            isPaused = isPaused,
                            isActive = activePlayer == 1 && !isPaused,
                            color = Color(0xFF2C2C2E),
                            isTimeUp = time1 <=0,
                            increment = increment,
                            onAdjustTime = { delta -> time1 = (time1 + delta).coerceAtLeast(0) },
                            onTape = {
                                if (!isPaused && activePlayer == 1){
                                    moves1++
                                    activePlayer = 2
                                    time1 += increment
                                    playClick()
                                }
                                     },

                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .rotate(180f)
                        )
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .height(64.dp)
                   .padding(2.dp)
                   .clip(RoundedCornerShape(7.dp))
                   .background(Color(0xFF1C1C1E))
                   ,
                verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceEvenly
           ) {
             ControleButton(icon = Icons.Default.PlayArrow, description = "play") { if(time1 > 0 && time2 > 0) isPaused = false}
              ControleButton(icon = Icons.Default.Pause , description = "Pause") { isPaused = true}
              ControleButton(icon = Icons.Default.Replay , description = "Replay") { resetGame()}
               ControleButton(icon = Icons.Default.Settings , description = "Settings") { showSettings = true
               isPaused = true}
               ControleButton( icon = if (soundOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff, description = "Volume")
               {
                   soundOn = !soundOn
                   prefs.edit { putBoolean("sound_on",soundOn) }
               }
               }
               PlayerZone(
                   playerName = "Player2",
                   time = formatTime(time2),
                   moves = moves2,
                   isPaused = isPaused,
                   isActive = activePlayer == 2 && !isPaused,
                   color =  Color(0xFF2C2C2E),
                   isTimeUp = time2 <=0,
                   increment = increment,
                   onAdjustTime = { delta -> time2 = (time2 + delta).coerceAtLeast(0) },
                   onTape = {
                       if (!isPaused && activePlayer == 2){
                           moves2++
                           activePlayer = 1
                           time2 += increment
                           playClick()
                       }
                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .weight(1f)
               )
       }
    }

@Composable
fun PlayerZone(
                playerName : String ,
               time : String ,
                moves : Int,
                isPaused : Boolean,
                isActive : Boolean,
                isTimeUp : Boolean,
                color : Color ,
               modifier: Modifier = Modifier,
                increment : Int,
                onAdjustTime : (Int) -> Unit,
                onTape:() -> Unit
              ){
        Box(
            modifier = modifier
                .background(
                    when {
                        isTimeUp -> Color(0xFFB71C1C)
                        isActive -> Color(0xFF1B5E5A)
                        else -> color
                    }
                )
                .clickable { onTape() },
        ) {
            if (isTimeUp) {
                Text(
                    text = "YOU LOSE",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 80.sp,
                )
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(if (isPaused) 0.4f else 1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = time,
                        fontSize = 96.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Fischer · +${increment}s/move",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                if (isPaused) {
                    Text(
                        text = "PAUSED",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "Moves: $moves",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = playerName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 25.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TimeChip("-30s") { onAdjustTime(-30) }
                    TimeChip("-15s") { onAdjustTime(-15) }
                    TimeChip("+15s") { onAdjustTime(+15) }
                    TimeChip("+30s") { onAdjustTime(+30) }

                }
            }

        }
}
@Composable
fun ControleButton (icon : ImageVector , description : String , onClick : () -> Unit){
    IconButton( onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color.White
        )
    }
}
@Composable
fun TimeChip (label: String, onClick: () -> Unit){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(50))
        .background(Color.White.copy(alpha = 0.15f))
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ){
    Text(text = label , color = Color.White)
    }
}
@Composable
fun OptionButton(label: String, selected: Boolean, onClick: () -> Unit) {
   TextButton(
       onClick = onClick,
       colors = ButtonDefaults.textButtonColors(
           containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer
           else Color.Transparent
       )
   ) {Text(label) }
}

fun formatTime(totalSeconds : Int): String{
    val s = totalSeconds.coerceAtLeast(0)
    return "%02d:%02d".format(s / 60, s % 60)
}