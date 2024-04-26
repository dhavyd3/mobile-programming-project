package com.alienventures.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.runtime.rememberCoroutineScope



//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtaskScreen2(onCancel: () -> Unit, onSave: (String) -> Unit,  listName: String,
                  label: String = "",
                  completedTaskDestinationLabel: String = "Where should completed tasks go?") {
    val (subtaskName, setSubtaskName) = remember { mutableStateOf("") }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val drawableResId = R.drawable.list // Replace "list" with the actual name of your XML drawable
    val iconPainter: Painter = painterResource(id = drawableResId)

    Column {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF303F9F)),
        ){
            TextComponent(
                text = listName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color(0xFF303F9F)), // Magenta background color
                fontWeight = FontWeight.Bold,
                contentColor = Color.White,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            TextField(
                value = label,
                onValueChange = setSubtaskName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 15.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFFFFFFFF)
                ), // Magenta text color
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // No background
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFFFFF), // Magenta underline when focused
                    unfocusedIndicatorColor = Color(0xFFFFFFFF), // Magenta underline when not focused
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = {
                    Text(text = "Let's give it a name",
                        color = Color.LightGray)
                },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextComponent(
            text = completedTaskDestinationLabel,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 16.sp
        )
        // Implement the DropdownMenu with icon here
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { setExpanded(true) })
                    .background(Color(0xFFF5F5F5)) // Light gray background
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = iconPainter,
                    contentDescription = "Your Trailing Icon Description",
                    modifier = Modifier.padding(end = 8.dp) // Adjust padding as needed
                )
                Text("This list", color = Color.Black) // Placeholder for selected option
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) }
            ) {
                // Define your DropdownMenuItems here
                DropdownMenuItem(
                    text = { Text("This list") },
                    onClick = {
                        // Handle item click
                        setExpanded(false)
                    }
                )
                // Add more DropdownMenuItem elements if needed
            }
        }

        // Buttons for CANCEL and SAVE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text("CANCEL", color = Color.Gray)
            }
            Button(
                onClick = { onSave(subtaskName) }, // Handle save
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF303F9F))
            ) {
                Text("SAVE", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubtaskPreview() {
    SubtaskScreen2(
        onCancel = { /* Implement onCancel action */ },
        onSave = { /* Implement onSave action */ },
        listName = "Sub List Name",
        completedTaskDestinationLabel = "Sample Destination Label"
    )
}