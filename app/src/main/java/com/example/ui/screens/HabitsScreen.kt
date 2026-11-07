package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StudyHabit
import com.example.ui.components.GradientCard
import com.example.ui.components.ProgressBarWithLabel
import com.example.ui.theme.*
import com.example.viewmodel.StudyViewModel

@Composable
fun HabitsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val habits by viewModel.habits.collectAsState()
    val completedCount = habits.count { it.isCompletedToday }
    val progressFraction = if (habits.isNotEmpty()) completedCount / habits.size.toFloat() else 0f
    var expandedHabitId by remember { mutableStateOf<Int?>(null) }
    var showAddHabitDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Title Header with Add Habit Action
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Good Study Habits to Develop",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Foundational behavioral pillars with custom checklist audits.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { showAddHabitDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier.testTag("add_custom_habit_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Habit")
                }
            }
        }

        // Daily Habit Progress Card
        item {
            GradientCard(
                gradientBrush = Brush.linearGradient(
                    colors = if (completedCount == habits.size && habits.isNotEmpty()) {
                        listOf(EmeraldGreen, SlateDark800)
                    } else {
                        listOf(SlateDark800, Color(0xFF132038))
                    }
                ),
                modifier = Modifier.fillMaxWidth().testTag("habits_progress_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (completedCount == habits.size && habits.isNotEmpty()) "All Habits Mastered Today! 🎉" else "Daily Habit Consistency",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$completedCount of ${habits.size} habits completed today (+${completedCount * 15} XP earned)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (completedCount == habits.size && habits.isNotEmpty()) EmeraldGreen else ElectricIndigo),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${(progressFraction * 100).toInt()}%",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                ProgressBarWithLabel(
                    progress = progressFraction,
                    label = "Today's Checklist Completion",
                    valueText = "$completedCount / ${habits.size}",
                    color = EmeraldGreen
                )
            }
        }

        // List of Habits
        items(habits, key = { it.id }) { habit ->
            val isExpanded = expandedHabitId == habit.id
            HabitItemCard(
                habit = habit,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedHabitId = if (isExpanded) null else habit.id
                },
                onToggleComplete = {
                    viewModel.toggleHabit(habit.id)
                },
                onUpdateHabit = { updated ->
                    viewModel.updateHabit(updated)
                },
                onDeleteHabit = { id ->
                    viewModel.deleteHabit(id)
                }
            )
        }
    }

    if (showAddHabitDialog) {
        AddHabitDialog(
            onDismiss = { showAddHabitDialog = false },
            onAdd = { title, sub, guide, check ->
                viewModel.addNewHabit(title, sub, guide, check)
                showAddHabitDialog = false
            }
        )
    }
}

@Composable
fun HabitItemCard(
    habit: StudyHabit,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleComplete: () -> Unit,
    onUpdateHabit: (StudyHabit) -> Unit,
    onDeleteHabit: (Int) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (habit.isCompletedToday) EmeraldGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = if (habit.isCompletedToday) androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen) else null,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("habit_card_${habit.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(
                    checked = habit.isCompletedToday,
                    onCheckedChange = { onToggleComplete() },
                    colors = CheckboxDefaults.colors(checkedColor = EmeraldGreen),
                    modifier = Modifier.testTag("habit_checkbox_${habit.id}")
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToggleExpand() }
                ) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (habit.isCompletedToday) EmeraldGreen else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = habit.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (habit.currentStreak > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CoralRose.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = CoralRose, modifier = Modifier.size(14.dp))
                            Text("${habit.currentStreak}d", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = CoralRose)
                        }
                    }
                }

                // Edit Habit Icon
                IconButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.testTag("edit_habit_btn_${habit.id}")
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Habit",
                        tint = LuminousCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider()

                    Text(
                        text = "Why this habit changes everything:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldenAmber
                    )
                    Text(
                        text = habit.actionGuide,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SlateDark800.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Audit Checklist:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = LuminousCyan
                            )
                            habit.checklistItems.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = LuminousCyan, modifier = Modifier.size(14.dp))
                                    Text(item, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        EditHabitDialog(
            habit = habit,
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                onUpdateHabit(updated)
                showEditDialog = false
            },
            onDelete = {
                onDeleteHabit(habit.id)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditHabitDialog(
    habit: StudyHabit,
    onDismiss: () -> Unit,
    onSave: (StudyHabit) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(habit.title) }
    var subtitle by remember { mutableStateOf(habit.subtitle) }
    var guide by remember { mutableStateOf(habit.actionGuide) }
    var checkText by remember { mutableStateOf(habit.checklistItems.joinToString("\n")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Study Habit") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Habit Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text("Behavioral Purpose / Subtitle") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = guide,
                        onValueChange = { guide = it },
                        label = { Text("Action Guide & Science") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
                item {
                    OutlinedTextField(
                        value = checkText,
                        onValueChange = { checkText = it },
                        label = { Text("Checklist Audit Items (1 per line)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val list = checkText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                    onSave(
                        habit.copy(
                            title = title.ifBlank { habit.title },
                            subtitle = subtitle,
                            actionGuide = guide,
                            checklistItems = if (list.isNotEmpty()) list else habit.checklistItems
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Save Habit")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Delete", color = CoralRose)
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, subtitle: String, guide: String, checklist: List<String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var guide by remember { mutableStateOf("") }
    var checkText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Study Habit") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Habit Title") },
                        placeholder = { Text("e.g., Daily 10-minute Flashcard Warmup") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text("Subtitle") },
                        placeholder = { Text("Prime memory pathways before lectures") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = guide,
                        onValueChange = { guide = it },
                        label = { Text("Action Guide") },
                        placeholder = { Text("Explain why this habit matters...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
                item {
                    OutlinedTextField(
                        value = checkText,
                        onValueChange = { checkText = it },
                        label = { Text("Checklist Items (1 per line)") },
                        placeholder = { Text("Step 1\nStep 2\nStep 3") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val list = checkText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                        onAdd(title, subtitle, guide, list)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Add Habit (+15 XP)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
