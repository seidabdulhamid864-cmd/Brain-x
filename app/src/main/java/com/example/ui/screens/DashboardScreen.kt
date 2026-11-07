package com.example.ui.screens

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
import com.example.model.Strategy
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StudyViewModel
import com.example.viewmodel.TimerMode

@Composable
fun DashboardScreen(
    viewModel: StudyViewModel,
    onNavigateToPaths: () -> Unit,
    onNavigateToStrategies: () -> Unit,
    onOpenStrategyLab: (Strategy) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val activePath by viewModel.activePath.collectAsState()
    val strategies by viewModel.strategies.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    val completedHabitsCount = habits.count { it.isCompletedToday }
    val xpForNextLevel = 100
    val currentLevelXp = profile.totalXp % xpForNextLevel
    val levelProgress = currentLevelXp / xpForNextLevel.toFloat()

    // Today's suggested strategy based on active path
    val recommendedStrategyId = activePath?.keyStrategyIds?.firstOrNull() ?: 4
    val spotlightStrategy = strategies.find { it.id == recommendedStrategyId } ?: strategies.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Hero Active Path Card
        item {
            GradientCard(
                gradientBrush = Brush.linearGradient(
                    colors = listOf(ElectricIndigo, SlateDark800)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_hero_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen)
                            )
                            Text(
                                text = "ACTIVE LEARNING PATH",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = LuminousCyan,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activePath?.title ?: "High-Retention Exam Accelerator",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activePath?.description ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 2
                        )
                    }
                    IconButton(
                        onClick = onNavigateToPaths,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .testTag("dashboard_edit_path_btn")
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Customize Path", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Daily Commitment", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                        Text("${activePath?.dailyTargetMinutes ?: 90} min/day", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Column {
                        Text("Core Routine", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                        Text("${activePath?.keyStrategyIds?.size ?: 4} Key Strategies", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = GoldenAmber)
                    }
                    Column {
                        Text("Learning Style", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                        Text(profile.learnerType.label.split(" / ").first(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = LuminousCyan)
                    }
                }
            }
        }

        // Gamification Metrics Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricBadge(
                    icon = Icons.Default.LocalFireDepartment,
                    label = "Day Streak",
                    value = "${profile.currentStreak} Days",
                    color = CoralRose,
                    modifier = Modifier.weight(1f)
                )
                MetricBadge(
                    icon = Icons.Default.MilitaryTech,
                    label = "Scholar Level",
                    value = "Lv. ${profile.level}",
                    color = GoldenAmber,
                    modifier = Modifier.weight(1f)
                )
                MetricBadge(
                    icon = Icons.Default.Timer,
                    label = "Total Study",
                    value = "${profile.totalMinutesStudied}m",
                    color = LuminousCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Level & XP Progress
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProgressBarWithLabel(
                        progress = levelProgress,
                        label = "Level ${profile.level} Progress (${profile.totalXp} Total XP)",
                        valueText = "$currentLevelXp / $xpForNextLevel XP to Level ${profile.level + 1}",
                        color = ElectricIndigo
                    )
                }
            }
        }

        // Strategy Spotlight
        item {
            SectionHeader(
                title = "Today's Strategy Spotlight",
                subtitle = "Strategy #${spotlightStrategy.id} of 16 Smart Learning Methods",
                actionText = "All 16",
                onActionClick = onNavigateToStrategies
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenStrategyLab(spotlightStrategy) }
                    .testTag("dashboard_spotlight_card")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ElectricIndigo.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = spotlightStrategy.category.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ElectricIndigo,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = if (spotlightStrategy.isMastered) "★ Mastered" else "${spotlightStrategy.practiceCount} reps",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (spotlightStrategy.isMastered) GoldenAmber else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "${spotlightStrategy.id}. ${spotlightStrategy.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = spotlightStrategy.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = { onOpenStrategyLab(spotlightStrategy) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Interactive Lab")
                    }
                }
            }
        }

        // Focus Timer Quick Card
        item {
            SectionHeader(
                title = "Study Sprint Timer",
                subtitle = "Strategy #6: Short Breaks & Pomodoro Cycles"
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val minutes = timerState.remainingSeconds / 60
                        val seconds = timerState.remainingSeconds % 60
                        Text(
                            text = "%02d:%02d".format(minutes, seconds),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (timerState.mode == TimerMode.SHORT_BREAK) EmeraldGreen else ElectricIndigo
                        )
                        Text(
                            text = timerState.mode.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                if (timerState.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (timerState.isRunning) CoralRose else ElectricIndigo
                            ),
                            modifier = Modifier.testTag("dashboard_timer_play_btn")
                        ) {
                            Icon(if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (timerState.isRunning) "Pause" else "Focus")
                        }
                    }
                }
            }
        }

        // Core Study Habits Quick Checklist
        item {
            SectionHeader(
                title = "Today's Study Habits",
                subtitle = "$completedHabitsCount of 8 habits completed today"
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                habits.forEach { habit ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (habit.isCompletedToday) EmeraldGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                        border = if (habit.isCompletedToday) androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleHabit(habit.id) }
                            .testTag("dashboard_habit_${habit.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Checkbox(
                                checked = habit.isCompletedToday,
                                onCheckedChange = { viewModel.toggleHabit(habit.id) },
                                colors = CheckboxDefaults.colors(checkedColor = EmeraldGreen)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = habit.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (habit.isCompletedToday) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (habit.isCompletedToday) EmeraldGreen else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = habit.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                            if (habit.currentStreak > 0) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = CoralRose, modifier = Modifier.size(16.dp))
                                    Text("${habit.currentStreak}d", style = MaterialTheme.typography.labelSmall, color = CoralRose, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
