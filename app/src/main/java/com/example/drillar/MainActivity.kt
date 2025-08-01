package com.example.drillar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.drillar.data.Drill
import com.example.drillar.presentation.DrillSelectionScreen
import com.example.drillar.ui.theme.DrillARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Define some dummy drill data
        val dummyDrills = listOf(
            Drill("1", "Drill 1", "A powerful cordless drill for heavy-duty tasks.", "Charge battery fully before use.", null),
            Drill("2", "Drill 2", "Lightweight and compact, ideal for home projects.", "Use appropriate drill bits for material.", null),
            Drill("3", "Drill 3", "High-speed professional drill with hammer function.", "Wear safety glasses during operation.", null)
        )
        setContent {
            DrillARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrillSelectionScreen(
                        drills = dummyDrills,
                        onStartArClick = { selectedDrill ->
                            val intent = Intent(this, ArActivity::class.java).apply {
                                // You can pass selected drill ID or data if needed
                                putExtra("selected_drill_id", selectedDrill.id)
                                putExtra("selected_drill_name", selectedDrill.name)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

