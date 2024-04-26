package com.alienventures.tasks

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.alienventures.tasks.Subtitle.InputSubtitle
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTaskManagementScreen(activity: Activity,listName: String, subtaskName: String) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }  // This is MutableState<Uri?>
    val isTaskCompleted = remember { mutableStateOf(false) }
    val isTaskHighlighted = remember { mutableStateOf(false)}
// Now pass imageUri (which is MutableState) to the function
    val (cameraLauncher, launchCamera) = rememberCameraResultLauncher(context, imageUri) { success, uri ->
        if (success) {
            imageUri.value = uri
        }
    }

    val galleryLauncher = rememberImagePickerResultLauncher { uri ->
        imageUri.value = uri
    }

    var title by remember { mutableStateOf(TextFieldValue("Task Name")) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { mutableFloatStateOf(0f) }
    var dueDate by remember { mutableStateOf<Date?>(null) }
    var notes by remember { mutableStateOf("") }
    val isDatePickerDialogOpen = remember { mutableStateOf(false) }
    var isNoteFieldFocused by remember { mutableStateOf(false) }

    var subtaskText by remember { mutableStateOf("") }
    var isSubtaskFieldFocused by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf<String?>(null) } // No tag selected by default



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    var filledText by remember {
                        mutableStateOf("")
                    }
                    TextField(
                        value = filledText,
                        onValueChange = { newText ->
                            filledText = newText },
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = Color(0xFFFFFFFF)
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent, // No background
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFFFFFFFF), // Magenta underline when focused
                            unfocusedIndicatorColor = Color(0xFFFFFFFF), // Magenta underline when not focused
                        ),
                        label = {
                            Text(text = "Task Name",
                                color = Color.LightGray)
                        },
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Blue
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Create Task object from UI state
//                    val newTask = Task(
//                        // ... other fields ...
//                         val imageUrl = imageUri.toString(),  // Convert the URI to a string to store in the database
//                        // ... other fields ...
//                    )
//                    // Insert the new task into the database
//                    taskViewModel.insert(newTask)
                },
                modifier = Modifier
                    .padding(16.dp) // Add padding if needed
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check_save),
                    contentDescription = "Save",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End, // Position the FAB to the end (right side)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn {
                item {

                    ListItem(
                        icon = painterResource(id = R.drawable.notes),
                        title = "Notes",
                        // Pass the current `notes` as the subtitle text and a lambda to handle changes
                        subtitle = InputSubtitle(notes, onTextChange = { newText ->
                            notes = newText
                        }),
                        label = "All Day",
                        isNoteFieldFocused = isNoteFieldFocused,
                        onTextFieldFocused = {
                            isNoteFieldFocused = true
                        }
                    )
                    ListItemWithAttachment(
                        onCameraClick = { launchCamera(imageUri.value ?: return@ListItemWithAttachment) },
                        onGalleryClick = { galleryLauncher.launch("image/*") },
                        imageUri = imageUri.value  // Use `value` to get the actual Uri? from MutableState<Uri?>
                    )
                    ListItem(
                        icon = painterResource(id = R.drawable.list),
                        title = "List",
                        subtitle = Subtitle.TextSubtitle(listName),
                        label = "All Day", hasSwitch = true
                        )
                    ListItem(
                        icon = painterResource(id = R.drawable.subtasks),
                        title = "Sub Tasks",
                        subtitle = InputSubtitle(subtaskText , onTextChange = { newText ->
                            subtaskText = newText
                        }),
                        label = "All Day",
                        isNoteFieldFocused = isSubtaskFieldFocused,
                        onTextFieldFocused = {
                            isSubtaskFieldFocused = true
                        }
                    )
                    ListItem(
                        icon = painterResource(id = R.drawable.tags),
                        title = "Tags",
                        subtitle = if (selectedTag != null) {
                            Subtitle.TextSubtitle(selectedTag!!)
                        } else {
                            Subtitle.ClickableTextSubtitle(
                                text = "No tags selected",
                                defaultText = "",
                                onClick = {
                                    selectedTag = if (selectedTag == "Solid") null else "Solid"
                                }
                            )
                        },
                        label = "All Day",
                    )

                    ListItem(
                        icon = painterResource(id = R.drawable.ic_date),
                        title = "Due Date", subtitle = Subtitle.TextSubtitle("No reminder set"),
                        label = "All Day", hasSwitch = true
                    )
                    ListItem(
                        icon = painterResource(id = R.drawable.notifications_off),
                        title = "Reminder",
                        subtitle = Subtitle.TextSubtitle("Remind me when due"),
                        label = "All Day",

                        )
                    ListItem(
                        icon = painterResource(id = R.drawable.ic_priority),
                        title = "Priority",
                        subtitle = Subtitle.ComposableSubtitle { PrioritySection2() },
                        label = "All Day",

                        )
                    ListItem(
                        icon = painterResource(id = R.drawable.highlight),
                        title = "HighLight",
                        subtitle = Subtitle.TextSubtitle("Make this task stand out in the list view"), // Pass the subtitle as a composable lambda
                        label = "All Day",
                        isHighlighted = isTaskHighlighted,
                        onHighlightClick = {
                            isTaskHighlighted.value = !isTaskHighlighted.value  // Toggle the highlight state
                        }
                    )
                    ListItem(
                        icon = painterResource(id = R.drawable.radiobutton),
                        title = "Completed",
                        label = "All Day" ,
                        isCompleted = isTaskCompleted,
                        onCompletedClick = {
                            isTaskCompleted.value = !isTaskCompleted.value  // Toggle the completed state
                        }
                    )
                    ListItem(
                        icon = painterResource(id = R.drawable.date),
                        title = "Created",
                        subtitle = Subtitle.TextSubtitle(
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(Date())
                        ),
                        label = "All Day"
                    )

                }
            }

        }


    }

    // Scroll state interaction
    LaunchedEffect(scrollState.isScrollInProgress) {
        scrollState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    // Handle start of drag if needed
                }

                is DragInteraction.Stop -> {
                    coroutineScope.launch {
                        offsetY.floatValue = -scrollState.value.toFloat()
                    }
                }
            }
        }
    }
}



@Composable
fun ListItem(
    icon: Painter,
    title: String,
    subtitle: Subtitle? = null,
    hasSwitch: Boolean = false,
    label: String,
    isNoteFieldFocused: Boolean = false,
    onTextFieldFocused: () -> Unit = {},
    trailingIcon: Painter? = null,
    additionalIcons: List<Painter>? = null,
    onIconClick: @Composable ((Int) -> Unit)? = null,
    onTextClick: (() -> Unit)? = null,
    isCompleted: MutableState<Boolean>? = null,
    isHighlighted: MutableState<Boolean>? = null,
    onHighlightClick: (() -> Unit)? = null,
    onCompletedClick: (() -> Unit)? = null,

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        if (isCompleted?.value == true) {
            Icon(
                painter = painterResource(id = R.drawable.completed),
                contentDescription = "Completed",
                tint = Color.Yellow,
                modifier = Modifier
                    .clickable {
                        isCompleted.value = false // Toggle off 'completed'
                        onCompletedClick?.invoke()
                    }
            )
            Spacer(Modifier.width(8.dp))
        }

        // Conditional display for 'highlighted' icon
        if (isHighlighted?.value == true) {
            Icon(
                painter = painterResource(id = R.drawable.hightlight2),
                contentDescription = "Highlight",
                tint = Color.Green,
                modifier = Modifier
                    .clickable {
                        isHighlighted.value = false // Toggle off 'highlight'
                        onHighlightClick?.invoke()
                    }
            )
        }

//        Icon(painter = icon, contentDescription = title)
        Column(modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f)
            .clickable {
                val isCurrentlyCompleted = isCompleted?.value ?: false
                val isCurrentlyHighlighted = isHighlighted?.value ?: false

                when {
                    isCurrentlyCompleted -> {
                        // If currently completed, unmark and possibly mark as highlighted
                        isCompleted?.value = false
                        if (!isCurrentlyHighlighted) {
                            isHighlighted?.value = true
                        }
                    }
                    isCurrentlyHighlighted -> {
                        // If currently highlighted, just remove the highlight
                        if (isHighlighted != null) {
                            isHighlighted.value = false
                        }
                    }
                    else -> {
                        // If neither, mark as completed
                        isCompleted?.value = true
                    }
                }

                onTextClick?.invoke()
            }
        ) {
            TextComponent(text = title)
            when (subtitle) {
                is Subtitle.TextSubtitle -> {
                    TextComponent(text = subtitle.text)
                }
                is InputSubtitle -> {
                    TextField(
                        value = subtitle.text,
                        onValueChange = subtitle.onTextChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTextFieldFocused() },
                        placeholder = {
                            // Only show the placeholder text if the field is not focused and the text is empty
                            if (!isNoteFieldFocused && subtitle.text.isEmpty()) {
                                Text(text = "Tap to add notes")
                            }
                        }
                    )
                }
                is Subtitle.ClickableTextSubtitle -> {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { subtitle.onClick.invoke() }
                        .padding(vertical = 8.dp)
                    ) {
                        TextComponent(text = if (subtitle.text.isNotEmpty()) subtitle.text else subtitle.defaultText)
                    }
                }
                is Subtitle.ComposableSubtitle -> {
                    subtitle.content()
                }
                null -> {}

            }
        }
        additionalIcons?.forEachIndexed { index, painter ->
            Icon(
                painter = painter,
                contentDescription = null,  // You can provide appropriate content descriptions for accessibility
                modifier = Modifier
                    .size(24.dp)
//                    .clickable { onIconClick?.invoke(index) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        if (hasSwitch) {
            TextComponent(text = label)
            Switch(checked = false, onCheckedChange = {})
        }
        trailingIcon?.let {
            Spacer(Modifier.width(16.dp)) // Add space between text and trailing icon if present
            Icon(painter = it, contentDescription = "Trailing icon")
        }
    }
}


@Composable
fun PrioritySection2() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        PriorityIndicator2(color = Color.LightGray, description = "None")
        PriorityIndicator2(color = Color.Yellow, description = "Low")
        PriorityIndicator2(color = Color.Green, description = "Medium")
        PriorityIndicator2(color = Color.Red, description = "High")
    }
}

@Composable
fun TextComponent(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    contentColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Transparent, // Added background color with a default transparent value
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Box(modifier = modifier.background(backgroundColor)) { // Apply background color to the Box wrapping Text
        Text(
            text = text,
            style = style.copy(
                color = contentColor, // Changed to contentColor
                fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
                fontWeight = fontWeight ?: style.fontWeight,
                textAlign = textAlign ?: style.textAlign
            ),
            maxLines = maxLines,
            overflow = overflow
        )
    }
}


@Composable
fun PriorityIndicator2(color: Color, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.priority), // Your priority icon
            contentDescription = "Priority Icon",
            tint = color,
            modifier = Modifier.size(24.dp) // Adjust size as needed
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_priority), // Your priority indicator icon
            contentDescription = description,
            tint = color,
            modifier = Modifier.size(24.dp) // Adjust size as needed
        )
    }
}

const val REQUEST_IMAGE_CAPTURE = 1
const val REQUEST_GALLERY = 2



@Composable
fun rememberImagePickerResultLauncher(
    onImagePicked: (Uri?) -> Unit
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = onImagePicked
    )
}

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

@Composable
fun rememberCameraResultLauncher(
    context: Context,
    imageUri: MutableState<Uri?>,  // Correctly accept MutableState<Uri?> as a parameter
    onImageCaptured: (Boolean, Uri?) -> Unit
): Pair<ActivityResultLauncher<Uri>, (Uri) -> Unit> {
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture(),
        onResult = { success ->
            onImageCaptured(success, imageUri.value)  // This is correct; imageUri is a MutableState
        }
    )

    val launchCamera = { uri: Uri ->
        imageUri.value = uri  // This is correct; set the value of the MutableState
        cameraLauncher.launch(uri)
    }

    return Pair(cameraLauncher, launchCamera)
}

@Composable
fun ListItemWithAttachment(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    imageUri: Uri?
) {
    ListItem(
        icon = painterResource(id = R.drawable.camera),
        title = "Attachment",
        label = "All Day",
        additionalIcons = listOf(
            painterResource(id = R.drawable.ic_camera),
            painterResource(id = R.drawable.add_photo)
        ),
        onIconClick = { index ->
            when (index) {
                0 -> onCameraClick()
                1 -> onGalleryClick()
            }
        }
    )
}



sealed class Subtitle {
    data class TextSubtitle(val text: String) : Subtitle()
    class ComposableSubtitle(val content: @Composable () -> Unit) : Subtitle()
    class InputSubtitle(var text: String, val onTextChange: (String) -> Unit) : Subtitle()
    class ClickableTextSubtitle(val text: String, val defaultText: String, val onClick: () -> Unit) : Subtitle()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultTaskPreview() {
       val activity = LocalContext.current as Activity // Get the current Activity instance
    val context = LocalContext.current // Get the current Context instance
    MainTaskManagementScreen(activity = activity, listName = "Sample List", subtaskName = "Sample Subtask")
}

