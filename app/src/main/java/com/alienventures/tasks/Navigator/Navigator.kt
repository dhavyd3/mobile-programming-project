package com.alienventures.tasks.Navigator

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alienventures.tasks.ListNameScreen
import com.alienventures.tasks.MainTaskManagementScreen
import com.alienventures.tasks.MyScreen
import com.alienventures.tasks.SubtaskScreen
import com.alienventures.tasks.TaskAppInterface
import com.alienventures.tasks.TasksScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listNames = listOf("Personal", "Work", "Shopping", "Wishlist") // Define your list names here

    NavHost(navController = navController, startDestination = "myScreen") {
        composable("myScreen") { MyScreen(navController) }
        composable("taskScreen") { TasksScreen(navController) }

        composable("subtaskScreen/{listName}") { backStackEntry ->
            val listName = backStackEntry.arguments?.getString("listName") ?: "Default List"
            SubtaskScreen(
                listName = listName,
                onSave = { subtaskName ->
                    // Assume we navigate back to a screen that can handle this subtaskName.
                    navController.previousBackStackEntry?.savedStateHandle?.set("subtaskName", subtaskName)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }


        composable("mainTaskScreen/{listName}/{subtaskName}") { backStackEntry ->
            // Use LocalContext to obtain the Activity within the composable function scope
            val activity = LocalContext.current as Activity

            val listName = backStackEntry.arguments?.getString("listName") ?: "Default List"
            val subtaskName = backStackEntry.arguments?.getString("subtaskName") ?: "Default Subtask"

            MainTaskManagementScreen(
                activity = activity, // Pass the obtained Activity instance
                listName = listName,
                subtaskName = subtaskName
            )
        }

    }
}

