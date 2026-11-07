package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomizationServiceDialog(
    initialGoal: StudyGoal,
    initialLearnerType: LearnerType,
    initialDailyMinutes: Int,
    initialStruggle: String,
    onDismiss: () -> Unit,
    onSaveCustomPath: (
        title: String,
        description: String,
        goal: StudyGoal,
        learnerType: LearnerType,
        dailyMinutes: Int,
        selectedStrategies: List<Int>,
        selectedHabits: List<Int>,
        morning: String,
        session: String,
        evening: String,
        struggle: String
    ) -> Unit
) {
    var step by remember { mutableStateOf(1) } // 1: Goal & Style, 2: Routine & Minutes, 3: Strategy & Habit Stack

    var selectedGoal by remember { mutableStateOf(initialGoal) }
    var selectedType by remember { mutableStateOf(initialLearnerType) }
    var selectedMinutes by remember { mutableStateOf(initialDailyMinutes) }
    var selectedStruggle by remember { mutableStateOf(initialStruggle) }

    // Strategies selected (defaults depending on goal)
    var selectedStrategies by remember {
        mutableStateOf(
            when (initialGoal) {
                StudyGoal.EXAM_PREP -> listOf(4, 5, 10, 16)
                StudyGoal.STEM_DEEP -> listOf(16, 14, 15, 11)
                StudyGoal.LANGUAGE_MEM -> listOf(1, 8, 5, 4)
                StudyGoal.SKILL_BLITZ -> listOf(15, 8, 3, 11)
                StudyGoal.BALANCED_HABITS -> listOf(12, 13, 9, 6)
            }
        )
    }

    // Core habits selected
    var selectedHabits by remember {
        mutableStateOf(listOf(1, 2, 4, 5))
    }

    var customTitle by remember { mutableStateOf("My Personalized ${selectedGoal.title}") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Learning Path Customizer",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Step $step of 3 • Tailor-made for your study rhythm",
                            style = MaterialTheme.typography.bodySmall,
                            color = ElectricIndigo
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Step content
                Box(modifier = Modifier.weight(1f)) {
                    when (step) {
                        1 -> StepOneGoalAndStyle(
                            selectedGoal = selectedGoal,
                            onGoalSelect = {
                                selectedGoal = it
                                customTitle = "My Personalized ${it.title}"
                                selectedStrategies = when (it) {
                                    StudyGoal.EXAM_PREP -> listOf(4, 5, 10, 16)
                                    StudyGoal.STEM_DEEP -> listOf(16, 14, 15, 11)
                                    StudyGoal.LANGUAGE_MEM -> listOf(1, 8, 5, 4)
                                    StudyGoal.SKILL_BLITZ -> listOf(15, 8, 3, 11)
                                    StudyGoal.BALANCED_HABITS -> listOf(12, 13, 9, 6)
                                }
                            },
                            selectedType = selectedType,
                            onTypeSelect = { selectedType = it },
                            selectedStruggle = selectedStruggle,
                            onStruggleSelect = { selectedStruggle = it }
                        )

                        2 -> StepTwoTimingAndRoutine(
                            selectedMinutes = selectedMinutes,
                            onMinutesSelect = { selectedMinutes = it },
                            customTitle = customTitle,
                            onTitleChange = { customTitle = it },
                            selectedGoal = selectedGoal
                        )

                        3 -> StepThreeStrategyAndHabitStack(
                            selectedStrategies = selectedStrategies,
                            onToggleStrategy = { id ->
                                selectedStrategies = if (selectedStrategies.contains(id)) {
                                    if (selectedStrategies.size > 1) selectedStrategies - id else selectedStrategies
                                } else {
                                    selectedStrategies + id
                                }
                            },
                            selectedHabits = selectedHabits,
                            onToggleHabit = { id ->
                                selectedHabits = if (selectedHabits.contains(id)) {
                                    if (selectedHabits.size > 1) selectedHabits - id else selectedHabits
                                } else {
                                    selectedHabits + id
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { step -= 1 },
                            modifier = Modifier.testTag("customizer_back")
                        ) {
                            Text("Back")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (step < 3) {
                        Button(
                            onClick = { step += 1 },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                            modifier = Modifier.testTag("customizer_next")
                        ) {
                            Text("Continue")
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = {
                                val morning = when (selectedGoal) {
                                    StudyGoal.EXAM_PREP -> "15-min Spaced Repetition queue review & formula warm-up."
                                    StudyGoal.STEM_DEEP -> "Deep conceptual proof breakdown during morning peak energy."
                                    StudyGoal.LANGUAGE_MEM -> "Vocal Say-Out-Loud 30-word vocabulary recitation."
                                    StudyGoal.SKILL_BLITZ -> "Micro-drill on single weakest sub-skill."
                                    StudyGoal.BALANCED_HABITS -> "5-min Box Breathing meditation & sanctuary setup."
                                }
                                val session = when (selectedGoal) {
                                    StudyGoal.EXAM_PREP -> "50-min active recall problem sets with timed mock testing."
                                    StudyGoal.STEM_DEEP -> "First-principles deconstruction and radial Mind Mapping."
                                    StudyGoal.LANGUAGE_MEM -> "Mnemonic peg palaces & reciprocal flashcard testing."
                                    StudyGoal.SKILL_BLITZ -> "High-speed deliberate practice with immediate mistake logging."
                                    StudyGoal.BALANCED_HABITS -> "Two 25-minute mindful focus blocks with zero notifications."
                                }
                                val evening = when (selectedGoal) {
                                    StudyGoal.EXAM_PREP -> "Error autopsy on missed questions & Cornell note summary."
                                    StudyGoal.STEM_DEEP -> "Cross-linking synthesis and boundary test reflections."
                                    StudyGoal.LANGUAGE_MEM -> "10-min Leitner Box review before sleep consolidation."
                                    StudyGoal.SKILL_BLITZ -> "Peer audio note exchange & self-rating."
                                    StudyGoal.BALANCED_HABITS -> "Celebrate completed session goal with personal reward."
                                }

                                onSaveCustomPath(
                                    customTitle,
                                    "Custom path designed for ${selectedType.label} learners focusing on ${selectedGoal.title} with $selectedMinutes min daily target.",
                                    selectedGoal,
                                    selectedType,
                                    selectedMinutes,
                                    selectedStrategies,
                                    selectedHabits,
                                    morning,
                                    session,
                                    evening,
                                    selectedStruggle
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            modifier = Modifier.testTag("customizer_activate")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Activate Custom Path")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepOneGoalAndStyle(
    selectedGoal: StudyGoal,
    onGoalSelect: (StudyGoal) -> Unit,
    selectedType: LearnerType,
    onTypeSelect: (LearnerType) -> Unit,
    selectedStruggle: String,
    onStruggleSelect: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "1. What is your primary learning objective?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StudyGoal.values().forEach { goal ->
                    val isSelected = goal == selectedGoal
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) ElectricIndigo.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricIndigo) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onGoalSelect(goal) }
                            .testTag("goal_${goal.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onGoalSelect(goal) },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricIndigo)
                            )
                            Column {
                                Text(
                                    text = goal.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = goal.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "2. What is your dominant learning style?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LearnerType.values().forEach { type ->
                    val isSelected = type == selectedType
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) LuminousCyan.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, LuminousCyan) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTypeSelect(type) }
                            .testTag("learner_${type.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onTypeSelect(type) },
                                colors = RadioButtonDefaults.colors(selectedColor = LuminousCyan)
                            )
                            Column {
                                Text(
                                    text = type.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) LuminousCyan else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = type.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "3. What is your biggest study obstacle right now?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            val struggles = listOf(
                "Forgetting concepts quickly (Retention gap)",
                "Phone notifications & digital distractions",
                "Procrastination & trouble starting",
                "Overwhelmed by high syllabus volume",
                "Test anxiety & panic under pressure"
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                struggles.forEach { item ->
                    val isSelected = item == selectedStruggle
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) GoldenAmber.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, GoldenAmber) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStruggleSelect(item) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) GoldenAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepTwoTimingAndRoutine(
    selectedMinutes: Int,
    onMinutesSelect: (Int) -> Unit,
    customTitle: String,
    onTitleChange: (String) -> Unit,
    selectedGoal: StudyGoal
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Name your custom study track:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customTitle,
                onValueChange = onTitleChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("custom_path_title_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricIndigo,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        item {
            Text(
                text = "Target Daily Study Commitment:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Consistency outperforms marathon cramming. Choose a realistic daily target.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            val timeOptions = listOf(30, 45, 60, 90, 120, 180)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timeOptions.take(3).forEach { mins ->
                    FilterChip(
                        selected = mins == selectedMinutes,
                        onClick = { onMinutesSelect(mins) },
                        label = { Text("$mins min") },
                        modifier = Modifier.weight(1f).testTag("time_chip_$mins")
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timeOptions.drop(3).forEach { mins ->
                    val label = if (mins >= 60) "${mins / 60}h ${if (mins % 60 != 0) "${mins % 60}m" else ""}".trim() else "$mins min"
                    FilterChip(
                        selected = mins == selectedMinutes,
                        onClick = { onMinutesSelect(mins) },
                        label = { Text(label) },
                        modifier = Modifier.weight(1f).testTag("time_chip_$mins")
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateDark800.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldenAmber)
                        Text(
                            text = "Generated 3-Phase Daily Blueprint",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = GoldenAmber
                        )
                    }
                    Text(
                        text = "• Morning (10-15m): Quick priming & active retrieval of yesterday's hardest points.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "• Main Session (${selectedMinutes - 20}m): Deep uninterrupted Pomodoro blocks applying target strategies.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "• Evening (10m): Mistake ledger review and sleep-consolidation check.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun StepThreeStrategyAndHabitStack(
    selectedStrategies: List<Int>,
    onToggleStrategy: (Int) -> Unit,
    selectedHabits: List<Int>,
    onToggleHabit: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Key Strategies to Emphasize (Pick 3 - 6):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Selected: ${selectedStrategies.size} of 16 strategies",
                style = MaterialTheme.typography.bodySmall,
                color = ElectricIndigo
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                com.example.data.DefaultStudyData.allStrategies.forEach { strat ->
                    val isChecked = selectedStrategies.contains(strat.id)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isChecked) ElectricIndigo.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = if (isChecked) androidx.compose.foundation.BorderStroke(1.dp, ElectricIndigo) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleStrategy(strat.id) }
                            .testTag("custom_strat_${strat.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggleStrategy(strat.id) },
                                colors = CheckboxDefaults.colors(checkedColor = ElectricIndigo)
                            )
                            Column {
                                Text(
                                    text = "${strat.id}. ${strat.title}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChecked) ElectricIndigo else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = strat.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Core Habits to Track Daily (Pick 2 - 5):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Selected: ${selectedHabits.size} of 8 core habits",
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldGreen
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                com.example.data.DefaultStudyData.allHabits.forEach { habit ->
                    val isChecked = selectedHabits.contains(habit.id)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isChecked) EmeraldGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = if (isChecked) androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleHabit(habit.id) }
                            .testTag("custom_habit_${habit.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggleHabit(habit.id) },
                                colors = CheckboxDefaults.colors(checkedColor = EmeraldGreen)
                            )
                            Column {
                                Text(
                                    text = habit.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChecked) EmeraldGreen else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = habit.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
