package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.BreathPhase
import com.example.viewmodel.StudyViewModel
import com.example.viewmodel.TimerMode

@Composable
fun StrategyLabRouter(
    strategyId: Int,
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.Science, contentDescription = null, tint = ElectricIndigo)
            Text(
                text = "Interactive Practice Lab",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ElectricIndigo
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        when (strategyId) {
            1 -> MnemonicLab(onPractice = { viewModel.recordStrategyPractice(1) })
            2 -> CornellNotesLab(viewModel = viewModel)
            3 -> PeerLearningLab(onPractice = { viewModel.recordStrategyPractice(3) })
            4 -> TestYourselfLab(viewModel = viewModel)
            5 -> SpacedRepetitionLab(viewModel = viewModel)
            6 -> PomodoroLab(viewModel = viewModel)
            7 -> BrainHealthLab(viewModel = viewModel)
            8 -> SayOutLoudLab(viewModel = viewModel)
            9 -> MindfulLearningLab(onPractice = { viewModel.recordStrategyPractice(9) })
            10 -> StudyCycleLab(onPractice = { viewModel.recordStrategyPractice(10) })
            11 -> ActiveLearningLab(onPractice = { viewModel.recordStrategyPractice(11) })
            12 -> MeditationBoxBreathingLab(viewModel = viewModel)
            13 -> EnergyManagerLab(onPractice = { viewModel.recordStrategyPractice(13) })
            14 -> MindMapLab(viewModel = viewModel)
            15 -> DeliberatePracticeLab(onPractice = { viewModel.recordStrategyPractice(15) })
            16 -> ConceptMasteryLab(onPractice = { viewModel.recordStrategyPractice(16) })
            else -> Text("Lab for strategy $strategyId")
        }
    }
}

// 1. Mnemonic Lab
@Composable
fun MnemonicLab(onPractice: () -> Unit) {
    var rawItems by remember { mutableStateOf("Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune") }
    var generatedAcronym by remember { mutableStateOf("MVEMJSUN") }
    var memorySentence by remember { mutableStateOf("My Very Educated Mother Just Served Us Noodles") }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Enter a list of items or words to memorize:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = rawItems,
            onValueChange = {
                rawItems = it
                val tokens = it.split(",", ";", " ").map { t -> t.trim() }.filter { t -> t.isNotEmpty() }
                generatedAcronym = tokens.mapNotNull { t -> t.firstOrNull()?.uppercaseChar() }.joinToString("")
            },
            modifier = Modifier.fillMaxWidth().testTag("mnemonic_input"),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricIndigo)
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ElectricIndigo.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Extracted Acronym:",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = generatedAcronym.ifEmpty { "Enter items above" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = LuminousCyan,
                    letterSpacing = 3.sp
                )
            }
        }

        OutlinedTextField(
            value = memorySentence,
            onValueChange = { memorySentence = it },
            label = { Text("Acrostic / Story sentence") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onPractice,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            modifier = Modifier.fillMaxWidth().testTag("mnemonic_save_btn")
        ) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save Mnemonic to Memory (+20 XP)")
        }
    }
}

// 2. Cornell Notes Lab
@Composable
fun CornellNotesLab(viewModel: StudyViewModel) {
    val notes by viewModel.cornellNotes.collectAsState()
    var title by remember { mutableStateOf("") }
    var cues by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Cornell 3-Section Cognitive Canvas:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Lecture / Book Chapter Title") },
            placeholder = { Text("e.g., Photosynthesis & Light Reactions") },
            modifier = Modifier.fillMaxWidth().testTag("cornell_title_input")
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = cues,
                onValueChange = { cues = it },
                label = { Text("Cues & Keywords (Left)") },
                placeholder = { Text("• ATP\n• Chlorophyll\n• Calvin Cycle") },
                modifier = Modifier.weight(0.4f).testTag("cornell_cues_input"),
                minLines = 4
            )
            OutlinedTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                label = { Text("Notes & Concepts (Right)") },
                placeholder = { Text("Synthesized formulas, diagrams, definitions...") },
                modifier = Modifier.weight(0.6f).testTag("cornell_notes_input"),
                minLines = 4
            )
        }

        OutlinedTextField(
            value = summary,
            onValueChange = { summary = it },
            label = { Text("Bottom Summary (3 Sentences max)") },
            placeholder = { Text("In your own words, what is the core takeaway?") },
            modifier = Modifier.fillMaxWidth().testTag("cornell_summary_input"),
            minLines = 2
        )

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.saveCornellNote(title, cues, noteContent, summary)
                    title = ""
                    cues = ""
                    noteContent = ""
                    summary = ""
                    showSuccess = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            modifier = Modifier.fillMaxWidth().testTag("cornell_save_btn"),
            enabled = title.isNotBlank()
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save Cornell Note (+25 XP)")
        }

        if (notes.isNotEmpty()) {
            Text(
                text = "Saved Cornell Notes (${notes.size}):",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            notes.take(2).forEach { n ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(n.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(n.summary, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                    }
                }
            }
        }
    }
}

// 3. Peer Learning Lab
@Composable
fun PeerLearningLab(onPractice: () -> Unit) {
    var topic by remember { mutableStateOf("Quantum Entanglement / Derivation of Bell's Theorem") }
    var selectedRole by remember { mutableStateOf("The Teacher (Explains from intuition)") }

    val roles = listOf(
        "The Teacher (Explains from intuition without notes)",
        "The Devil's Advocate (Finds edge cases & challenges assumptions)",
        "The Synthesizer (Connects topic to previous 3 chapters)",
        "The Quizmaster (Asks 5 sudden active recall questions)"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Study Group Agenda & Role Roulette:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Topic to co-solve with peer") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Select your role for this 20-min session:", style = MaterialTheme.typography.bodySmall)
        roles.forEach { role ->
            val isSelected = role == selectedRole
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) LuminousCyan.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, LuminousCyan) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedRole = role }
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isSelected, onClick = { selectedRole = role }, colors = RadioButtonDefaults.colors(selectedColor = LuminousCyan))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(role, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Button(
            onClick = onPractice,
            colors = ButtonDefaults.buttonColors(containerColor = LuminousCyan),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Peer Learning Sprint (+20 XP)", color = SlateDark950, fontWeight = FontWeight.Bold)
        }
    }
}

// 4. Test Yourself Lab
@Composable
fun TestYourselfLab(viewModel: StudyViewModel) {
    val cards by viewModel.flashcards.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var isRevealed by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditCardDialog by remember { mutableStateOf(false) }

    if (cards.isEmpty()) {
        Text("No test questions yet. Add one below!")
        return
    }

    val currentCard = cards[currentIndex % cards.size]

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${currentIndex + 1} of ${cards.size}",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricIndigo,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showEditCardDialog = true },
                    modifier = Modifier.testTag("edit_flashcard_btn")
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Card",
                        tint = LuminousCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.deleteFlashcard(currentCard.id)
                        isRevealed = false
                        if (cards.size > 1) {
                            currentIndex = currentIndex % (cards.size - 1)
                        }
                    },
                    modifier = Modifier.testTag("delete_flashcard_btn")
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Card",
                        tint = CoralRose,
                        modifier = Modifier.size(18.dp)
                    )
                }
                TextButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.testTag("add_flashcard_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add")
                }
            }
        }

        // Card flip container
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isRevealed = !isRevealed }
                .testTag("flashcard_flip_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = currentCard.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminousCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = currentCard.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                if (isRevealed) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = currentCard.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tap to Reveal Answer",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricIndigo
                    )
                }
            }
        }

        if (isRevealed) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.reviewFlashcard(currentCard.id, false)
                        isRevealed = false
                        currentIndex = (currentIndex + 1) % cards.size
                    },
                    modifier = Modifier.weight(1f).testTag("flashcard_retry_btn")
                ) {
                    Text("Need Review")
                }
                Button(
                    onClick = {
                        viewModel.reviewFlashcard(currentCard.id, true)
                        isRevealed = false
                        currentIndex = (currentIndex + 1) % cards.size
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier.weight(1f).testTag("flashcard_mastered_btn")
                ) {
                    Text("Mastered! (+15 XP)")
                }
            }
        }
    }

    if (showAddDialog) {
        var newQ by remember { mutableStateOf("") }
        var newA by remember { mutableStateOf("") }
        var newCat by remember { mutableStateOf("Study Concept") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Create Retrieval Flashcard") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newQ, onValueChange = { newQ = it }, label = { Text("Question / Concept prompt") })
                    OutlinedTextField(value = newA, onValueChange = { newA = it }, label = { Text("Answer / First Principles Explanation") })
                    OutlinedTextField(value = newCat, onValueChange = { newCat = it }, label = { Text("Subject / Category") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newQ.isNotBlank() && newA.isNotBlank()) {
                            viewModel.addFlashcard(newQ, newA, newCat)
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save Card")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showEditCardDialog) {
        var editQ by remember(currentCard) { mutableStateOf(currentCard.question) }
        var editA by remember(currentCard) { mutableStateOf(currentCard.answer) }
        var editCat by remember(currentCard) { mutableStateOf(currentCard.category) }
        var editBox by remember(currentCard) { mutableStateOf(currentCard.box) }

        AlertDialog(
            onDismissRequest = { showEditCardDialog = false },
            title = { Text("Edit Active Recall Card") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editQ,
                        onValueChange = { editQ = it },
                        label = { Text("Question / Concept prompt") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editA,
                        onValueChange = { editA = it },
                        label = { Text("Answer / First Principles Explanation") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    OutlinedTextField(
                        value = editCat,
                        onValueChange = { editCat = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Leitner Repetition Box: $editBox (Box 1 to 5)", style = MaterialTheme.typography.bodySmall, color = ElectricIndigo)
                    Slider(
                        value = editBox.toFloat(),
                        onValueChange = { editBox = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editQ.isNotBlank() && editA.isNotBlank()) {
                            viewModel.updateFlashcard(
                                currentCard.copy(
                                    question = editQ,
                                    answer = editA,
                                    category = editCat,
                                    box = editBox
                                )
                            )
                            showEditCardDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditCardDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// 5. Spaced Repetition Lab
@Composable
fun SpacedRepetitionLab(viewModel: StudyViewModel) {
    val cards by viewModel.flashcards.collectAsState()

    val boxCounts = remember(cards) {
        (1..5).map { boxNum -> cards.count { it.box == boxNum } }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Leitner 5-Box Interval Distribution:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val intervals = listOf("1 Day", "3 Days", "7 Days", "14 Days", "30 Days")
            boxCounts.forEachIndexed { index, count ->
                val boxNum = index + 1
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricIndigo.copy(alpha = 0.5f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Box $boxNum", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text("$count", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = ElectricIndigo)
                        Text(intervals[index], style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SlateDark800.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = GoldenAmber)
                Text(
                    text = "Cards graduated to Box 5 enter permanent semantic memory.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Button(
            onClick = { viewModel.recordStrategyPractice(5, 20) },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Review Today's Spaced Queue (+20 XP)")
        }
    }
}

// 6. Pomodoro Focus & Short Breaks Lab
@Composable
fun PomodoroLab(viewModel: StudyViewModel) {
    val timerState by viewModel.timerState.collectAsState()
    val minutes = timerState.remainingSeconds / 60
    val seconds = timerState.remainingSeconds % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Mode selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TimerMode.values().forEach { mode ->
                FilterChip(
                    selected = timerState.mode == mode,
                    onClick = { viewModel.setTimerMode(mode, 6) },
                    label = { Text(mode.label.split(" ").first()) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if (timerState.mode == TimerMode.SHORT_BREAK || timerState.mode == TimerMode.LONG_BREAK) EmeraldGreen else ElectricIndigo
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    if (timerState.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (timerState.isRunning) CoralRose else ElectricIndigo
                ),
                modifier = Modifier.testTag("pomodoro_toggle_btn")
            ) {
                Icon(if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (timerState.isRunning) "Pause" else "Start Sprint")
            }

            OutlinedButton(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier.testTag("pomodoro_reset_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset")
            }
        }

        Text(
            text = "Completed Focus Sprints: ${timerState.completedSessions}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// 7. Brain Health & Energy Lab
@Composable
fun BrainHealthLab(viewModel: StudyViewModel) {
    val health by viewModel.dailyHealth.collectAsState()
    var sleep by remember(health) { mutableStateOf(health.sleepHours) }
    var water by remember(health) { mutableStateOf(health.waterGlasses) }
    var exercise by remember(health) { mutableStateOf(health.didExercise) }
    var nutrition by remember(health) { mutableStateOf(health.healthyNutrition) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Neuro-Readiness Score:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (health.readinessScore >= 80) EmeraldGreen.copy(alpha = 0.2f) else GoldenAmber.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${health.readinessScore}% Optimal",
                    fontWeight = FontWeight.Bold,
                    color = if (health.readinessScore >= 80) EmeraldGreen else GoldenAmber,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Text("Sleep Duration: ${String.format("%.1f", sleep)} hrs", style = MaterialTheme.typography.bodySmall)
        Slider(
            value = sleep,
            onValueChange = {
                sleep = it
                viewModel.updateDailyHealth(sleep, water, exercise, nutrition)
            },
            valueRange = 4f..10f,
            steps = 11
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Hydration: $water glasses (250ml)", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(onClick = {
                    if (water > 0) {
                        water -= 1
                        viewModel.updateDailyHealth(sleep, water, exercise, nutrition)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = null)
                }
                IconButton(onClick = {
                    water += 1
                    viewModel.updateDailyHealth(sleep, water, exercise, nutrition)
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = exercise,
                onClick = {
                    exercise = !exercise
                    viewModel.updateDailyHealth(sleep, water, exercise, nutrition)
                },
                label = { Text("Aerobic Exercise (20m+)") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = nutrition,
                onClick = {
                    nutrition = !nutrition
                    viewModel.updateDailyHealth(sleep, water, exercise, nutrition)
                },
                label = { Text("Clean Brain Fuel") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// 8. Say Out Loud / Feynman Lab
@Composable
fun SayOutLoudLab(viewModel: StudyViewModel) {
    val secondsLeft by viewModel.feynmanSecondsLeft.collectAsState()
    val isRunning by viewModel.isFeynmanRunning.collectAsState()

    var topic by remember { mutableStateOf("Deriving standard deviation / Ohm's Law") }
    var check1 by remember { mutableStateOf(false) }
    var check2 by remember { mutableStateOf(false) }
    var check3 by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "The 60-Second Feynman Verbal Challenge:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Target concept to explain out loud") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${secondsLeft}s",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = if (secondsLeft <= 10) CoralRose else ElectricIndigo
            )

            Button(
                onClick = {
                    if (isRunning) viewModel.resetFeynmanTimer() else viewModel.startFeynmanTimer()
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) CoralRose else ElectricIndigo)
            ) {
                Text(if (isRunning) "Stop" else "Start 60s Vocal Pitch")
            }
        }

        Text("Feynman Clarity Audit:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = check1, onCheckedChange = { check1 = it })
            Text("Used zero unnecessary academic jargon", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = check2, onCheckedChange = { check2 = it })
            Text("Used a tangible real-world analogy", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = check3, onCheckedChange = { check3 = it })
            Text("A 10-year-old would comprehend the intuition", style = MaterialTheme.typography.bodySmall)
        }
    }
}

// 9. Mindful Learning Lab
@Composable
fun MindfulLearningLab(onPractice: () -> Unit) {
    var intention by remember { mutableStateOf("Dedicate 25 minutes to single-task focus on thermodynamics proofs.") }
    var didClearDesk by remember { mutableStateOf(false) }
    var didHidePhone by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Single-Task Focus Intention:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = intention,
            onValueChange = { intention = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Today's explicit intention") }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = didClearDesk, onCheckedChange = { didClearDesk = it })
            Text("Physical desk cleared of visual distractions", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = didHidePhone, onCheckedChange = { didHidePhone = it })
            Text("Phone silenced & placed out of line-of-sight", style = MaterialTheme.typography.bodySmall)
        }

        Button(
            onClick = onPractice,
            enabled = didClearDesk && didHidePhone,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enter Mindful Flow Zone (+20 XP)")
        }
    }
}

// 10. Study Cycle Lab
@Composable
fun StudyCycleLab(onPractice: () -> Unit) {
    var step1 by remember { mutableStateOf(false) }
    var step2 by remember { mutableStateOf(false) }
    var step3 by remember { mutableStateOf(false) }
    var step4 by remember { mutableStateOf(false) }
    var step5 by remember { mutableStateOf(false) }

    val completedCount = listOf(step1, step2, step3, step4, step5).count { it }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Frank Christ 5-Stage Study Cycle: ($completedCount / 5 Complete)",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        val stages = listOf(
            "1. PREVIEW: Skim chapter headings, figures & summary before class" to step1,
            "2. ATTEND: Engage dynamically, capture main cues, ask 1 question" to step2,
            "3. REVIEW: Skim & clarify lecture notes within 15 min of class" to step3,
            "4. STUDY: Deep 30-50 min focused session with active problems" to step4,
            "5. CHECK: Teach concepts without notes to verify true mastery" to step5
        )

        stages.forEachIndexed { idx, pair ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (pair.second) ElectricIndigo.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (idx) {
                            0 -> step1 = !step1
                            1 -> step2 = !step2
                            2 -> step3 = !step3
                            3 -> step4 = !step4
                            4 -> step5 = !step5
                        }
                    }
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = pair.second,
                        onCheckedChange = { checked ->
                            when (idx) {
                                0 -> step1 = checked
                                1 -> step2 = checked
                                2 -> step3 = checked
                                3 -> step4 = checked
                                4 -> step5 = checked
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(pair.first, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        if (completedCount == 5) {
            Button(
                onClick = onPractice,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Complete Full Study Cycle (+35 XP)")
            }
        }
    }
}

// 11. Active Learning Lab
@Composable
fun ActiveLearningLab(onPractice: () -> Unit) {
    var q1 by remember { mutableStateOf("What underlying law causes this behavior?") }
    var q2 by remember { mutableStateOf("How does this relate to what I learned last week?") }
    var q3 by remember { mutableStateOf("What happens if temperature / input doubles?") }
    var synthesis by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("3 Probing Questions Formulation:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = q1, onValueChange = { q1 = it }, label = { Text("Causal Question 1") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = q2, onValueChange = { q2 = it }, label = { Text("Relational Question 2") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = q3, onValueChange = { q3 = it }, label = { Text("Edge Case Question 3") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = synthesis,
            onValueChange = { synthesis = it },
            label = { Text("1-Sentence Original Synthesis") },
            placeholder = { Text("Synthesize without copying the author's words") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onPractice,
            enabled = synthesis.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Active Synthesis (+25 XP)")
        }
    }
}

// 12. Meditation & Box Breathing Lab
@Composable
fun MeditationBoxBreathingLab(viewModel: StudyViewModel) {
    val state by viewModel.meditationState.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val animatedScale by animateFloatAsState(
        targetValue = when (state.currentPhase) {
            BreathPhase.INHALE -> 1.35f
            BreathPhase.HOLD_FULL -> 1.35f
            BreathPhase.EXHALE -> 0.8f
            BreathPhase.HOLD_EMPTY -> 0.8f
        },
        animationSpec = tween(durationMillis = 4000, easing = LinearEasing),
        label = "breathScale"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "4-4-4-4 Parasympathetic Box Breathing",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = (size.minDimension / 2.5f) * if (state.isActive) animatedScale else 1f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(LuminousCyan.copy(alpha = 0.4f), Color.Transparent),
                        center = center,
                        radius = radius * 1.3f
                    ),
                    radius = radius * 1.3f
                )
                drawCircle(
                    color = LuminousCyan,
                    radius = radius,
                    style = Stroke(width = 6.dp.toPx())
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (state.isActive) state.currentPhase.label else "Ready",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = LuminousCyan
                )
                if (state.isActive) {
                    Text(
                        text = "${state.phaseRemainingSeconds}s",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (state.isActive) {
            Text(
                text = state.currentPhase.instruction,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Cycles Completed: ${state.cyclesCompleted}",
                style = MaterialTheme.typography.labelSmall,
                color = GoldenAmber
            )
        }

        Button(
            onClick = { viewModel.toggleMeditation() },
            colors = ButtonDefaults.buttonColors(containerColor = if (state.isActive) CoralRose else ElectricIndigo),
            modifier = Modifier.testTag("meditation_toggle_btn")
        ) {
            Icon(if (state.isActive) Icons.Default.Stop else Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (state.isActive) "End Breathing" else "Start 4-4-4-4 Breathwork")
        }
    }
}

// 13. Energy Manager Lab
@Composable
fun EnergyManagerLab(onPractice: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Circadian Energy Curve & Task Matching:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)

        val slots = listOf(
            Triple("9:00 AM - 12:00 PM", "Peak Analytic Prime Time", "Hardest math proofs, first-principles logic, writing new papers"),
            Triple("1:00 PM - 3:00 PM", "Post-Prandial Circadian Dip", "Light reviews, sorting flashcards, administrative study setup"),
            Triple("5:00 PM - 8:00 PM", "Second Cognitive Wind", "Deliberate practice drills, peer study, practice testing")
        )

        slots.forEach { (time, title, desc) ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = ElectricIndigo)
                        Text(time, style = MaterialTheme.typography.labelSmall, color = GoldenAmber)
                    }
                    Text(desc, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Button(
            onClick = onPractice,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Schedule Sessions by Circadian Peak (+15 XP)")
        }
    }
}

// 14. Mind Map Lab
@Composable
fun MindMapLab(viewModel: StudyViewModel) {
    val nodes by viewModel.mindMapNodes.collectAsState()
    var newNodeText by remember { mutableStateOf("") }
    var selectedParentId by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Radial Associative Graph (${nodes.size} nodes):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            TextButton(onClick = { viewModel.clearMindMap() }) {
                Text("Reset Graph")
            }
        }

        LazyColumn(
            modifier = Modifier.heightIn(max = 160.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(nodes) { node ->
                val isSelected = node.id == selectedParentId
                val colors = listOf(ElectricIndigo, LuminousCyan, GoldenAmber, EmeraldGreen, PurpleAccent)
                val color = colors[node.colorIndex % colors.size]

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = if (isSelected) 0.35f else 0.15f),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, color) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedParentId = if (isSelected) null else node.id }
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (node.parentId != null) "  ↳ ${node.text}" else "● ${node.text}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (node.parentId == null) FontWeight.Bold else FontWeight.Normal,
                            color = color
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = newNodeText,
                onValueChange = { newNodeText = it },
                placeholder = { Text(if (selectedParentId != null) "Branch sub-idea..." else "New core concept...") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newNodeText.isNotBlank()) {
                        viewModel.addMindMapNode(newNodeText, selectedParentId)
                        newNodeText = ""
                    }
                },
                enabled = newNodeText.isNotBlank()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }
}

// 15. Deliberate Practice Lab
@Composable
fun DeliberatePracticeLab(onPractice: () -> Unit) {
    var bottleneck by remember { mutableStateOf("Deriving integration by parts with trigonometric substitutions") }
    var reps by remember { mutableStateOf(3) }
    var mistakeLog by remember { mutableStateOf("Forgot negative sign on cosine antiderivative.") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Targeted Bottleneck Drill:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = bottleneck,
            onValueChange = { bottleneck = it },
            label = { Text("Isolated Weak Sub-skill") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rapid Reps Completed: $reps", style = MaterialTheme.typography.bodySmall)
            Row {
                IconButton(onClick = { if (reps > 1) reps -= 1 }) { Icon(Icons.Default.Remove, contentDescription = null) }
                IconButton(onClick = { reps += 1 }) { Icon(Icons.Default.Add, contentDescription = null) }
            }
        }

        OutlinedTextField(
            value = mistakeLog,
            onValueChange = { mistakeLog = it },
            label = { Text("Mistake Ledger Note (Why did error occur?)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onPractice,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Deliberate Practice Session (+25 XP)")
        }
    }
}

// 16. Concept Mastery Lab
@Composable
fun ConceptMasteryLab(onPractice: () -> Unit) {
    var axiom by remember { mutableStateOf("Conservation of Energy: Energy cannot be created or destroyed.") }
    var analogy by remember { mutableStateOf("Like a water tank with fixed fluid passing through pipes and turbines.") }
    var boundary by remember { mutableStateOf("What happens in quantum vacuum fluctuations or near black hole horizons?") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("First-Principles Decomposition:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = axiom, onValueChange = { axiom = it }, label = { Text("Fundamental Axiom") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = analogy, onValueChange = { analogy = it }, label = { Text("Intuitive Physical Analogy") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = boundary, onValueChange = { boundary = it }, label = { Text("Extreme Boundary Condition Test") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = onPractice,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Master First Principles (+30 XP)")
        }
    }
}
