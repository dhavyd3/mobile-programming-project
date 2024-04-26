package com.alienventures.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController


@Composable
fun ListNameScreen(navController: NavController, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val (subtaskName, setSubtaskName) = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss, // Use onDismiss to handle dialog dismissal
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                //.height(IntrinsicSize.Min)
                .padding(8.dp)
                .background(Color(0xFF303F9F)), // Use a default color or remove if not needed
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextComponent(
                text = "List Name",
                fontSize = 24.sp,
                contentColor = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp) // Example of adding a modifier
            )


            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),


                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    // Handle the keyboard 'Done' action
                }),
                label = {
                    Text(text = "let's give it a name")
                },

                )

            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextComponent(
                    "Cancel",
                    modifier = Modifier.clickable { onDismiss() },
                    contentColor = Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextComponent(
                    "Save",
                    modifier = Modifier.clickable {
                        onSave(text)
                        navController.navigate("taskAppInterface/$text") // Add navigation call here
                    },
                    contentColor = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListNameScreenPreview(){
    val context = LocalContext.current
    val testNavController = remember { TestNavHostController(context) }
    // Set up the TestNavHostController with a graph if needed for previews
    // testNavController.setGraph(R.navigation.nav_graph)

    ListNameScreen(
        navController = testNavController,
        onDismiss = { /* No-op for preview */ },
        onSave = { /* No-op for preview */ }
    )
}
