package com.alienventures.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alienventures.tasks.Navigator.AppNavigation


@Composable
fun MainScreen() {
    // This is be the root composable of your app.
    AppNavigation()
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {

                MainScreen()
//                MainTaskManagementScreen(activity = this)
            }
        }
    }
}
