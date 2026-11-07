package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.LearningPath
import com.example.model.Strategy
import com.example.ui.components.GradientCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.StudyViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalizedPathsScreen(
    viewModel: StudyViewModel,
    onOpenStrategy: (Strategy) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val paths by viewModel.learningPaths.collectAsState()
    val activePath by viewModel.activePath.collectAsState()
    val strategies by viewModel.strategies.collectAsState()

    var showCustomizerDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Customization Service Callout
        item {
            GradientCard(
                gradientBrush = Brush.linearGradient(
                    colors = listOf(SlateDark800, Color(0xFF1E1B4B), ElectricIndigo)
                ),
                modifier = Modifier.fillMaxWidth().testTag("customization_service_banner")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = GoldenAmber, modifier = Modifier.size(18.dp))
                            Text(
                                text = "PERSONALIZATION SERVICE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldenAmber,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tailor Your Learning Track",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Answer 3 quick questions to build an algorithmically tailored roadmap matching your goals, weak spots, and daily hours.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { showCustomizerDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenAmber),
                    modifier = Modifier.fillMaxWidth().testTag("open_customizer_btn")
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = SlateDark950)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Launch Customization Wizard", color = SlateDark950, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Diagnostic Profile Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your Learner Diagnostic Profile",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Learning Style", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(profile.learnerType.label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LuminousCyan)
                        }
                        Column {
                            Text("Daily Target", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${profile.dailyMinutesTarget} min / day", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = ElectricIndigo)
                        }
                    }
                    Text("Identified Obstacle: ${profile.mainStruggle}", style = MaterialTheme.typography.labelSmall, color = GoldenAmber)
                }
            }
        }

        // Active Path Detailed Blueprint
        if (activePath != null) {
            item {
                SectionHeader(
                    title = "Currently Active Blueprint",
                    subtitle = activePath?.title
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = activePath?.title ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldGreen.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = activePath?.description ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // 3-Phase Daily Routine
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SlateDark800.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "3-Phase Daily Routine Architecture:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldenAmber
                                )
                                Text("☀️ Morning: ${activePath?.morningRoutine}", style = MaterialTheme.typography.bodySmall)
                                Text("🎯 Focus Session: ${activePath?.studySessionRoutine}", style = MaterialTheme.typography.bodySmall)
                                Text("🌙 Evening Review: ${activePath?.eveningReviewRoutine}", style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        // Recommended strategies chips
                        Text(
                            text = "Key Strategy Stack (${activePath?.keyStrategyIds?.size} of 16):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            activePath?.keyStrategyIds?.forEach { sId ->
                                val strat = strategies.find { it.id == sId }
                                if (strat != null) {
                                    AssistChip(
                                        onClick = { onOpenStrategy(strat) },
                                        label = { Text("${strat.id}. ${strat.title}") },
                                        leadingIcon = {
                                            Icon(Icons.Default.Bolt, contentDescription = null, tint = ElectricIndigo, modifier = Modifier.size(14.dp))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Learning Paths Library
        item {
            SectionHeader(
                title = "Learning Paths Library",
                subtitle = "Switch or compare learning paths"
            )
        }

        items(paths) { path ->
            val isActive = path.id == activePath?.id
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isActive) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surface
                ),
                border = if (isActive) androidx.compose.foundation.BorderStroke(1.dp, ElectricIndigo) else null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (path.isCustom) {
                                    Surface(shape = RoundedCornerShape(4.dp), color = GoldenAmber.copy(alpha = 0.2f)) {
                                        Text("CUSTOM", color = GoldenAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text(
                                    text = path.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${path.dailyTargetMinutes} min/day • ${path.keyStrategyIds.size} Strategies • ${path.coreHabitIds.size} Habits",
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricIndigo
                            )
                        }
                        if (isActive) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = EmeraldGreen)
                        } else {
                            OutlinedButton(
                                onClick = { viewModel.selectPath(path.id) },
                                modifier = Modifier.testTag("select_path_${path.id}")
                            ) {
                                Text("Activate")
                            }
                        }
                    }

                    Text(
                        text = path.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showCustomizerDialog) {
        CustomizationServiceDialog(
            initialGoal = profile.primaryGoal,
            initialLearnerType = profile.learnerType,
            initialDailyMinutes = profile.dailyMinutesTarget,
            initialStruggle = profile.mainStruggle,
            onDismiss = { showCustomizerDialog = false },
            onSaveCustomPath = { title, desc, goal, learnerType, dailyMin, strats, habits, morning, session, evening, struggle ->
                viewModel.customizeLearningPath(
                    title = title,
                    description = desc,
                    goal = goal,
                    learnerType = learnerType,
                    dailyHoursMinutes = dailyMin,
                    selectedStrategyIds = strats,
                    selectedHabitIds = habits,
                    morningRoutine = morning,
                    studyRoutine = session,
                    eveningRoutine = evening,
                    struggle = struggle
                )
                showCustomizerDialog = false
            }
        )
    }
}
