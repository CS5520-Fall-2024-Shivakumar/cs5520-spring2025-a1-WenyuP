package com.example.numad25sp_wenyupan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.numad25sp_wenyupan.ui.theme.NUMAD25Sp_WenyuPanTheme

class AboutMeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NUMAD25Sp_WenyuPanTheme {
                AboutMeScreen()
            }
        }
    }
}

@Composable
fun AboutMeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Wenyu Pan", modifier = Modifier.padding(8.dp))
        Text(text = "pan.we@northeastern.edu", modifier = Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMePreview() {
    NUMAD25Sp_WenyuPanTheme {
        AboutMeScreen()
    }
}
