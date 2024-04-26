package com.alienventures.tasks



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import  com.alienventures.tasks.ListItem as ListItem1

@Composable
fun TasksScreen(navController: NavController) {
    var showSidebar by remember { mutableStateOf(false) }
    var showNewListNameScreen by remember { mutableStateOf(false) }

    // Inside TasksScreen
    if (showNewListNameScreen) {
        // Assuming navController is available in the scope of TasksScreen
        ListNameScreen(
            navController = navController, // This should be the actual NavController instance
            onDismiss = { showNewListNameScreen = false },
            onSave = { newName ->
                // Handle saving the new list name here
                showNewListNameScreen = false
            }
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .height(48.dp)
    ) {
        // Menu icon on the left
        IconButton(
            onClick = { showSidebar = !showSidebar },
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu"
            )
        }

        // Add Spacer to push the text to center
        Spacer(modifier = Modifier.weight(1f))

        // Top section with "Tasks" text
        Box(
            modifier = Modifier
                .background(Color(0xFF303F9F)) // Set the desired background color here
                .fillMaxWidth(), // This will make the Box take up the full width
            contentAlignment = Alignment.Center//
        ) {
            TextComponent(
                text = "Tasks",
               fontSize = 36.sp,
                contentColor =  Color.White,
                modifier = Modifier.padding(4.dp).background(Color(0xFF303F9F)),
            )
        }

        // Add Spacer to ensure the text stays in the center
        Spacer(modifier = Modifier.weight(1f))
    }

    // Show sidebar if the state is true
    if (showSidebar) {
        Sidebar(onAddNewListClicked = { showNewListNameScreen = true })
    }
}

@Composable
fun Sidebar(
    onAddNewListClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp) // Adjusted for better spacing
            .background(Color.White)
    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF303F9F))
                .padding(8.dp)
                .height(48.dp)
        ) {

            TextComponent(
                text = "Tasks",
                fontSize = 36.sp,
                contentColor = Color.White,
                
            )


        }
        // Add New List
        ListItem1(icon = Icons.Default.Add, text = "Add New List") {
            onAddNewListClicked()
        }

        // Divider between "Add New List" and "Upgrade to Pro"
        Divider(color = Color.Gray, thickness = 2.dp)

        // Upgrade to Pro
        ListItem1(icon = Icons.Default.Star, text = "Upgrade to Pro") {
            // Handle "Upgrade to Pro" click event
            // TODO: Add your logic here
        }

        // Donate
        ListItem1(icon = Icons.Default.Favorite, text = "Donate") {
            // Handle "Donate" click event
            // TODO: Add your logic here
        }

        // Help
        ListItem1(icon = Icons.Default.Build, text = "Help") {
            // Handle "Help" click event
            // TODO: Add your logic here
        }

        // Settings
        ListItem1(icon = Icons.Default.Settings, text = "Settings") {
            // Handle "Settings" click event
            // TODO: Add your logic here
        }
    }
}

typealias ListItemClickAction = () -> Unit


@Composable
fun ListItem(icon: ImageVector, text: String, onClick: ListItemClickAction) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextComponent(
            text = text,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SideBarPreview(){
    Sidebar(onAddNewListClicked = {})
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskScreenPreview() {

    val context = LocalContext.current
    val testNavController = remember { TestNavHostController(context) }
    TasksScreen( navController = testNavController)
}