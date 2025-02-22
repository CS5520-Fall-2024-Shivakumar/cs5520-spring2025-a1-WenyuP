package com.example.numad25sp_wenyupan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.numad25sp_wenyupan.ui.theme.NUMAD25Sp_WenyuPanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NUMAD25Sp_WenyuPanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hello World!")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(context, AboutMeActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("About Me")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(context, CalculatorActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Quick Calc")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // New Button for "Contacts Collector"
        Button(onClick = {
            val intent = Intent(context, ContactsCollectorActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Contacts Collector")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NUMAD25Sp_WenyuPanTheme {
        Greeting(name = "World")
    }
}
