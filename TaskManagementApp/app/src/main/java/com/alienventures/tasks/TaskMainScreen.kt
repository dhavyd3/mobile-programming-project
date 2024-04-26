//package com.alienventures.tasks
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.DatePickerDialog
//import android.content.Context
//import android.content.Intent
//import android.provider.MediaStore
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.DragInteraction
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FabPosition
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
////import androidx.compose.material3.NavigationRailDefaults.ContainerColor
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
////import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
////import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.alienventures.tasks.database.Task
//import com.alienventures.tasks.database.TaskViewModel
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Date
//import java.util.Locale
//
//
//
//
//enum class Priority(val label: String) {
//    NONE("None"), LOW("Low"), MEDIUM("Medium"), HIGH("High")
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainTaskManagementScreen(activity: Activity, context: Context, taskViewModel: TaskViewModel = viewModel()) {
//
//   // var title by remember { mutableStateOf(TextFieldValue("Task Name")) }
//    val scrollState = rememberScrollState()
//    val coroutineScope = rememberCoroutineScope()
//    val offsetY = remember { mutableFloatStateOf(0f) }
//    var dueDate by remember { mutableStateOf<Date?>(null) }
////    var notes by remember { mutableStateOf("") }
////    val isDatePickerDialogOpen = remember { mutableStateOf(false) }
//    var priority by remember { mutableStateOf(Priority.NONE) }
//    var reminder by remember { mutableStateOf<String?>(null) }
//
//    val isTaskCompleted = remember { mutableStateOf(false) }
//    val isTaskHighlighted = remember { mutableStateOf(false)}
//
//
//
//        Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    var filledText by remember {
//                        mutableStateOf("")
//                    }
//                    TextField(
//                        value = filledText,
//                        onValueChange = { newText ->
//                            filledText = newText },
//                        modifier = Modifier.padding(top = 20.dp),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Text,
//                            imeAction = ImeAction.Done
//                        ),
//                        label = {
//                            Text(text = "Task Name")
//                        },
//                    )
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
////                    // Create Task object from UI state
////                    val newTask = Task(
////                        listName = "Default List", // Replace with actual list name if needed
//////                        taskName = title.text,
////                        attachment = null, // Replace with actual attachment path if added
////                        subtasks = "", // Replace with actual subtasks if added
//////                        tags = tags,
////                        dueDate = dueDate,
////                        reminder = reminder?.let { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(it) },
////                        priority = priority.ordinal, // Save enum ordinal or alternatively the name
////                        highlight = isTaskHighlighted.value,
////                        completed = isTaskCompleted.value,
////                        createdAt = Date()
////                    )
////                    taskViewModel.insert(newTask)
//                },
//                modifier = Modifier
//                    .padding(16.dp) // Add padding if needed
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.check_save),
//                    contentDescription = "Save",
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        },
//        floatingActionButtonPosition = FabPosition.End, // Position the FAB to the end (right side)
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//        ) {
//            LazyColumn {
//                item {
//
//                    ListItem(
//                        icon = painterResource(id = R.drawable.notes),
//                        title = "Notes",
//                        subtitle = Subtitle.TextSubtitle("Tap to add notes"),
//                        label = "All Day",
//                    )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.camera),
//                        title = "Attachment",
//                        label = "All Day",
//                        additionalIcons = listOf(
//                            painterResource(id = R.drawable.ic_camera),
//                            painterResource(id = R.drawable.add_photo)
//                        ),
//                        onIconClick = { index ->
//                            when (index) {
//                                0 -> onCameraClick(activity)  // Camera icon click
//                                1 -> onGalleryClick(activity) // Gallery icon click
//                            }
//                        }
//                    )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.subtasks),
//                        title = "Sub Tasks",
//                        label = "All Day",
//                        additionalIcons = listOf(painterResource(id = R.drawable.subtask)),
//                        onIconClick = { /* Handle subtask icon click */ }
//                    )
//
//                    ListItem(
//                        icon = painterResource(id = R.drawable.tags),
//                        title = "Tags",
//                        subtitle = Subtitle.TextSubtitle("No tags selected"),
//                        label = "All Day",
//                        additionalIcons = listOf(painterResource(id = R.drawable.subtask)),
//                        onIconClick = { /* Handle subtask icon click */ }
//                    )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.tags),
//                        title = "Tags",
//                        subtitle = Subtitle.TextSubtitle("No tags selected"),
//                        label = "All Day",
//
//                        )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.ic_date),
//                        title = "Due Date",
//                        subtitle = Subtitle.TextSubtitle(
//                            dueDate?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) }
//                                ?: "Set due date"
//                        ),
//                        label = "All Day",
//                        onIconClick = {
//                            showDatePicker(context) { selectedDate ->
//                                dueDate = selectedDate
//                            }
//                        }
//                    )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.notifications_off),
//                        title = "Reminder",
//                        subtitle = Subtitle.TextSubtitle(reminder ?: "Set reminder"),
//                        label = "All Day",
//                        onIconClick = {
//                            showReminderOptions(context) { selectedReminder ->
//                                reminder = selectedReminder
//                            }
//                        }
//                    )
//
//
//                    ListItem(
//                        icon = painterResource(id = R.drawable.ic_priority),
//                        title = "Priority",
//                        additionalText = "Priority: ${priority.label}",
//                        subtitle = Subtitle.ComposableSubtitle {
//                            PrioritySection2 { newPriority ->
//                                priority = newPriority // Set the new priority
//                            }
//                        },
//                        label = "All Day",
//
//                        )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.highlight),
//                        title = "HighLight",
//                        subtitle = Subtitle.TextSubtitle("Make this task stand out in the list view"), // Pass the subtitle as a composable lambda
//                        label = "All Day",
//                        isHighlighted = isTaskHighlighted,
//                        trailingIcon = painterResource(id = R.drawable.color_lens),
//                        onHighlightClick = {
//                            isTaskHighlighted.value = !isTaskHighlighted.value  // Toggle the highlight state
//                        }
//
//
//
//                        )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.radiobutton),
//                        title = "Completed",
//                        label = "All Day" ,
//                        isCompleted = isTaskCompleted,
//                        onCompletedClick = {
//                            isTaskCompleted.value = !isTaskCompleted.value  // Toggle the completed state
//                        },
//                    )
//                    ListItem(
//                        icon = painterResource(id = R.drawable.date),
//                        title = "Created",
//                        subtitle = Subtitle.TextSubtitle(
//                            SimpleDateFormat(
//                                "yyyy-MM-dd",
//                                Locale.getDefault()
//                            ).format(Date())
//                        ),
//                        label = "All Day"
//                    )
//
//                }
//            }
//
//        }
//
//
//    }
//
//    // Scroll state interaction
//    LaunchedEffect(scrollState.isScrollInProgress) {
//        scrollState.interactionSource.interactions.collect { interaction ->
//            when (interaction) {
//                is DragInteraction.Start -> {
//                    // Handle start of drag if needed
//                }
//
//                is DragInteraction.Stop -> {
//                    coroutineScope.launch {
//                        offsetY.floatValue = -scrollState.value.toFloat()
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ListItem(
//    icon: Painter,
//    title: String,
//    subtitle: Subtitle? = null,
//    hasSwitch: Boolean = false,
//    label: String,
//    additionalText: String? = null, // Optional additional text parameter
//    trailingIcon: Painter? = null,
//    additionalIcons: List<Painter>? = null,
//    onIconClick: ((Int) -> Unit)? = null,
//    isCompleted: MutableState<Boolean>? = null,
//    isHighlighted: MutableState<Boolean>? = null,
//    onCompletedClick: (() -> Unit)? = null,
//    onHighlightClick: (() -> Unit)? = null,
//
//
//    ) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//
//        isCompleted?.let { completed ->
//            val tickIcon = if (completed.value) R.drawable.completed else R.drawable.circle
//            val tickColor = if (completed.value) Color.Yellow else Color.Gray
//            Icon(
//                painter = painterResource(id = tickIcon),
//                contentDescription = "Completed",
//                tint = tickColor,
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable {
//                        onCompletedClick?.invoke()
//                    }
//            )
//            Spacer(Modifier.width(8.dp))
//        }
//
//        isHighlighted?.let { highlighted ->
//            val highlightColor = if (highlighted.value) Color.Green else Color.Gray
//            Icon(
//                painter = painterResource(id = R.drawable.highlight),
//                contentDescription = "Highlight",
//                tint = highlightColor,
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable {
//                        onHighlightClick?.invoke()
//                    }
//            )
//        }
//
//        Icon(painter = icon, contentDescription = title)
//        Column(modifier = Modifier
//            .padding(start = 16.dp)
//            .weight(1f)) {
//            TextComponent(text = title)
//            if (additionalText != null) {
//                // Display the additional text if provided
//                TextComponent(text = additionalText, style = TextStyle(fontWeight = FontWeight.Normal))
//            }
//            when (subtitle) {
//                is Subtitle.TextSubtitle -> {
//                    TextComponent(text = subtitle.text)
//                }
//                is Subtitle.InputSubtitle -> {
//                    TextField(
//                        value = subtitle.text,
//                        onValueChange = subtitle.onTextChange,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//                is Subtitle.ComposableSubtitle -> {
//                    subtitle.content()
//                }
//                null -> {}
//
////                Modifier.clickable { onIconClick?.invoke() }
//            }
//        }
//        additionalIcons?.forEachIndexed { index, painter ->
//            Icon(
//                painter = painter,
//                contentDescription = null,  // You can provide appropriate content descriptions for accessibility
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable { onIconClick?.invoke(index) }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//        }
//        if (hasSwitch) {
//            TextComponent(text = label)
//            Switch(checked = false, onCheckedChange = {})
//        }
//        trailingIcon?.let {
//            Spacer(Modifier.width(16.dp)) // Add space between text and trailing icon if present
//            Icon(painter = it, contentDescription = "Trailing icon")
//        }
//    }
//}
//
//
//@Composable
//fun PrioritySection2(onPriorityChange: (Priority) -> Unit) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        PriorityIndicator2(color = Color.LightGray, description = "None") { onPriorityChange(Priority.NONE) }
//        PriorityIndicator2(color = Color.Yellow, description = "Low") { onPriorityChange(Priority.LOW) }
//        PriorityIndicator2(color = Color.Green, description = "Medium") { onPriorityChange(Priority.MEDIUM) }
//        PriorityIndicator2(color = Color.Red, description = "High") { onPriorityChange(Priority.HIGH) }
//    }
//}
//
//
//@Composable
//fun TextComponent(
//    text: String,
//    modifier: Modifier = Modifier,
//    style: TextStyle = TextStyle.Default,
//    contentColor: Color = Color.Unspecified,
//    backgroundColor: Color = Color.Transparent, // Added background color with a default transparent value
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontWeight: FontWeight? = null,
//    textAlign: TextAlign? = null,
//    maxLines: Int = Int.MAX_VALUE,
//    overflow: TextOverflow = TextOverflow.Clip
//) {
//    Box(modifier = modifier.background(backgroundColor)) { // Apply background color to the Box wrapping Text
//        Text(
//            text = text,
//            style = style.copy(
//                color = contentColor, // Changed to contentColor
//                fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
//                fontWeight = fontWeight ?: style.fontWeight,
//                textAlign = textAlign ?: style.textAlign
//            ),
//            maxLines = maxLines,
//            overflow = overflow
//        )
//    }
//}
//
//
//@Composable
//fun PriorityIndicator2(
//    color: Color,
//    description: String,
//    onClick: () -> Unit // Add this parameter
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .padding(end = 8.dp)
//            .clickable { onClick() }
//    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.priority), // Your priority icon
//            contentDescription = "Priority Icon",
//            tint = color,
//            modifier = Modifier.size(24.dp) // Adjust size as needed
//        )
//        Spacer(modifier = Modifier.width(4.dp))
//        Icon(
//            painter = painterResource(id = R.drawable.ic_priority), // Your priority indicator icon
//            contentDescription = description,
//            tint = color,
//            modifier = Modifier.size(24.dp) // Adjust size as needed
//        )
//    }
//}
//const val REQUEST_IMAGE_CAPTURE = 1
//const val REQUEST_GALLERY = 2
//
//@SuppressLint("QueryPermissionsNeeded")
//fun onCameraClick(activity: Activity) {
//    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//        takePictureIntent.resolveActivity(activity.packageManager)?.also {
//            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        }
//    }
//}
//
//fun onGalleryClick(activity: Activity) {
//    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { pickPhotoIntent ->
//        activity.startActivityForResult(pickPhotoIntent, REQUEST_GALLERY)
//    }
//}
//
//fun showDatePicker(context: Context, setDate: (Date) -> Unit) {
//    val calendar = Calendar.getInstance()
//    DatePickerDialog(context, { _, year, month, dayOfMonth ->
//        calendar.set(year, month, dayOfMonth)
//        setDate(calendar.time)
//    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//}
//
//fun showReminderOptions(context: Context, setReminder: (String) -> Unit) {
//    val options = arrayOf("Don't remind me", "Remind me when due", "Remind me in advance")
//    AlertDialog.Builder(context)
//        .setTitle("Choose Reminder")
//        .setItems(options) { _, which ->
//            setReminder(options[which])
//        }
//        .show()
//}
//
//
//sealed class Subtitle {
//    data class TextSubtitle(val text: String) : Subtitle()
//    class ComposableSubtitle(val content: @Composable () -> Unit) : Subtitle()
//    class InputSubtitle(var text: String, val onTextChange: (String) -> Unit) : Subtitle()
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MainTaskManagementScreenPreview() {
//    val activity = LocalContext.current as Activity // Get the current Activity instance
//    val context = LocalContext.current // Get the current Context instance
//  MainTaskManagementScreen(activity = activity, context = context)
//}
//
////  MainTaskManagementScreen(activity = this, context = applicationContext, taskViewModel = viewModel())
