package com.alienventures.tasks



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController


@Composable
fun TaskAppInterface(listName: String, listNames: List<String>, navController: NavController) {
    var showsubSidebar by remember { mutableStateOf(false) }
    var showSubtaskScreen by remember { mutableStateOf(false) }
    var selectedSubtaskName by remember { mutableStateOf("") } // State to hold the selected subtask name


    Scaffold(
        topBar = {
            CombinedTopBar(
                listName,
                onMenuClicked = { showsubSidebar = !showsubSidebar },

                onSubtaskIconClick = { showSubtaskScreen = true } // This will show the SubtaskScreen
            )
        },
        floatingActionButton = {
            TaskFloatingActionButton {
                // Navigate to the MainTaskManagementScreen passing the listName
                navController.navigate("mainTaskScreen/$listName")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            TaskInputBar(onSendClicked = {
                if (selectedSubtaskName.isNotEmpty()) {
                    // Navigate to the MainTaskManagementScreen passing both listName and subtaskName
                    navController.navigate("mainTaskScreen/$listName/$selectedSubtaskName")
                }
            })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                ContentTitle(listName, onSubtaskIconClick = { showSubtaskScreen = true })
                TaskContent(Modifier.padding(innerPadding))
            }
            // Place the SubSidebar here to make it overlap
            if (showsubSidebar) {
                // The SubSidebar composable will be modified to fill the entire width
                SubSidebar(listNames) { listItemText ->
                    // Handle click event for each list item
                    // Example: navController.navigate(listItemText)
                    showsubSidebar = false
                }
            }

            // Center the SubtaskScreen in the middle of the screen
            if (showSubtaskScreen) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    SubtaskScreen(
                        onCancel = { showSubtaskScreen = false },
                        onSave = { subtaskName ->
                            // Here you save the subtask name and close the SubtaskScreen
                            selectedSubtaskName = subtaskName
                            showSubtaskScreen = false
                            // Pass the subtaskName to the MainTaskManagementScreen (not shown here)
                            // You might use a ViewModel or other state-holding mechanism
                        },
                        listName = listName
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopAppBar(title: String, onMenuClicked: () -> Unit) {
    TopAppBar(
        title = {
            TextComponent(
                text = title,
                contentColor = Color.White
            )
        },
        navigationIcon = {
                IconButton(onClick = onMenuClicked) {

                    // onMenuClicked will handle the logic to show/hide sidebar
                    Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Share, contentDescription = null, tint = Color.White)
            }

            IconButton(onClick = {}) {
                Icon(Icons.Filled.MoreVert, contentDescription = null, tint = Color.White)
            }
        },
        colors = topAppBarColors(
            containerColor = Color(0xFF303F9F) // Example purple-pink color

        )
    )
}
//.background(Color(0xFF303F9F))

@Composable
fun TaskFloatingActionButton(onFabClicked: () -> Unit) {
    FloatingActionButton(
        onClick = onFabClicked,
        containerColor = Color(0xFFFFA500)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
    }
}//

// und(Color(0xFFFFA500)) // Orange color
@Composable
fun TaskInputBar(onSendClicked: () -> Unit) {
    var text by remember { mutableStateOf("") }
    val placeholderText = "Quick add task..."

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF303F9F))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .background(Color(0xFF303F9F), RoundedCornerShape(50)) // Use a blue accent color
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .height(30.dp)
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                modifier = Modifier.widthIn(max = 200.dp),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        TextComponent(placeholderText, contentColor = Color.White, textAlign = TextAlign.Center)
                    }
                    innerTextField()
                }
            )
        }
        IconButton(onClick = onSendClicked) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Send", tint = Color.White) // Set the icon tint to white
        }
    }
}
@Composable
fun CombinedTopBar(title: String, onMenuClicked: () -> Unit, onSubtaskIconClick: () -> Unit) {
    Column(modifier = Modifier.background(Color(0xFF303F9F)))  {
        TaskTopAppBar(title, onMenuClicked = onMenuClicked)
        ContentTitle(title, onSubtaskIconClick = onSubtaskIconClick)
    }
}


@Composable
fun ContentTitle(listName: String, onSubtaskIconClick: () -> Unit)  {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF303F9F))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextComponent(
            text = listName,
            fontWeight = FontWeight.Bold,
            contentColor = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            fontSize = 20.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.subtask),
            contentDescription = "Subtasks Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onSubtaskIconClick),
            tint = Color.White
        )
    }
}


@Composable
fun TaskContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.Edit, contentDescription = "Edit", Modifier.size(24.dp)) // Edit icon
        Spacer(modifier = Modifier.height(8.dp)) // Spacer for padding between icon and text
        TextComponent(text = "A fresh list, let's get started.")
    }
}

@Composable
fun SubSidebar(listNames: List<String>, onListItemClicked: (String) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val maxWidth = maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
//                .padding(start = maxWidth * 0.125f, end = maxWidth * 0.125f)
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
                    contentColor = Color.White
                )
            }

            listNames.forEach { listName ->
                SubListItem(painter = painterResource(id = R.drawable.list), text = listName) {
                    onListItemClicked(listName)
                }
            }
            // Add New List
            SubListItem(painter = painterResource(id = R.drawable.subtask), text = "New List") {
                onListItemClicked("New List")
            }

            // New Filtered List
            SubListItem(
                painter = painterResource(id = R.drawable.filter),
                text = "New Filtered List"
            ) {
                onListItemClicked("New Filtered List")
            }

            SubListItem(
                painter = painterResource(id = R.drawable.shared),
                text = "New Filtered List"
            ) {
                onListItemClicked("New Shared List")
            }
            SubListItem(
                painter = painterResource(id = R.drawable.reorder),
                text = "New Filtered List"
            ) {
                onListItemClicked("Reorder Task  List")
            }
            SubListItem(
                painter = painterResource(id = R.drawable.edit),
                text = "New Filtered List"
            ) {
                onListItemClicked("Edit List")
            }

            Divider(color = Color.Gray, thickness = 2.dp)

            SubListItem(
                painter = painterResource(id = R.drawable.tags),
                text = "New Filtered List"
            ) {
                onListItemClicked("Edit List")
            }
            SubListItem(
                painter = painterResource(id = R.drawable.delete),
                text = "New Filtered List"
            ) {
                onListItemClicked(" Deleted Items")
            }

            Divider(color = Color.Gray, thickness = 2.dp)

            SubListItem(
                painter = painterResource(id = R.drawable.upgrade),
                text = "New Filtered List"
            ) {
                onListItemClicked(" Upgrade to upgrade")
            }

            SubListItem(
                painter = painterResource(id = R.drawable.donate),
                text = "New Filtered List"
            ) {
                onListItemClicked(" Donate")
            }

            SubListItem(
                painter = painterResource(id = R.drawable.help),
                text = "New Filtered List"
            ) {
                onListItemClicked(" Help")
            }

            SubListItem(
                painter = painterResource(id = R.drawable.settings),
                text = "New Filtered List"
            ) {
                onListItemClicked(" Settings")
            }

        }

    }
}

@Composable
fun SubtaskScreen(onCancel: () -> Unit, onSave: (String) -> Unit,  listName: String,
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
                    text = " Sub List Name",
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
                    Text(listName, color = Color.Black) // Placeholder for selected option
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

@Composable
fun SubListItem(painter: Painter, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {
        Icon(
            painter = painter,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 18.sp
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val testNavController = remember { TestNavHostController(context) }
    TaskAppInterface("my task list", listOf("List 1", "List 2", "List 3"), navController = testNavController )
}
