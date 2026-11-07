package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.model.StrategyCategory
import com.example.ui.components.GradientCard
import com.example.ui.components.MetricBadge
import com.example.ui.components.ProgressBarWithLabel
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.StudyViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnalyticsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val strategies by viewModel.strategies.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val sessionLogs by viewModel.sessionLogs.collectAsState()
    val flashcards by viewModel.flashcards.collectAsState()

    val masteredStrategiesCount = strategies.count { it.isMastered }
    val totalStrategyPractices = strategies.sumOf { it.practiceCount }
    val box5Cards = flashcards.count { it.box >= 4 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Progress & Study Analytics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Quantifiable cognitive growth and retention metrics.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldGreen.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Universal APK Ready • Signed for all Android phones (SDK 24+)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                }
            }
        }

        // Scholar Status Card
        item {
            GradientCard(
                gradientBrush = Brush.linearGradient(
                    colors = listOf(SlateDark800, Color(0xFF1E293B), ElectricIndigo)
                ),
                modifier = Modifier.fillMaxWidth().testTag("analytics_scholar_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("SCHOLAR LEVEL", style = MaterialTheme.typography.labelSmall, color = GoldenAmber, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("Level ${profile.level}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("${profile.totalXp} Lifetime XP Earned", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                    }
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(GoldenAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldenAmber, modifier = Modifier.size(32.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                ProgressBarWithLabel(
                    progress = (profile.totalXp % 100) / 100f,
                    label = "Progress to Level ${profile.level + 1}",
                    valueText = "${profile.totalXp % 100} / 100 XP",
                    color = GoldenAmber
                )
            }
        }

        // Key Quant Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricBadge(
                    icon = Icons.Default.AccessTime,
                    label = "Focus Minutes",
                    value = "${profile.totalMinutesStudied}m",
                    color = ElectricIndigo,
                    modifier = Modifier.weight(1f)
                )
                MetricBadge(
                    icon = Icons.Default.School,
                    label = "Mastered",
                    value = "$masteredStrategiesCount / 16",
                    color = EmeraldGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricBadge(
                    icon = Icons.Default.Inventory2,
                    label = "Spaced Cards",
                    value = "${flashcards.size} total",
                    color = LuminousCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 16 Strategies Mastery Breakdown by Category
        item {
            SectionHeader(
                title = "Strategy Mastery Distribution",
                subtitle = "Cognitive methods deployed across study sessions"
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val categories = listOf(
                        StrategyCategory.MEMORY to ElectricIndigo,
                        StrategyCategory.COMPREHENSION to LuminousCyan,
                        StrategyCategory.FOCUS_WELLNESS to EmeraldGreen,
                        StrategyCategory.PRACTICE to GoldenAmber
                    )

                    categories.forEach { (cat, color) ->
                        val catStrats = strategies.filter { it.category == cat }
                        val masteredInCat = catStrats.count { it.isMastered }
                        val fraction = if (catStrats.isNotEmpty()) masteredInCat / catStrats.size.toFloat() else 0f

                        ProgressBarWithLabel(
                            progress = fraction,
                            label = "${cat.label} (${catStrats.size} Strategies)",
                            valueText = "$masteredInCat / ${catStrats.size} Mastered",
                            color = color
                        )
                    }
                }
            }
        }

        // Recent Study Activity History
        item {
            SectionHeader(
                title = "Recent Study Activity",
                subtitle = "${sessionLogs.size} recorded sessions"
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (sessionLogs.isEmpty()) {
                    Text(
                        text = "No study sessions logged yet. Complete a Pomodoro sprint or interactive lab to begin logging!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    sessionLogs.take(5).forEach { log ->
                        val strat = strategies.find { it.id == log.strategyId }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(ElectricIndigo.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = ElectricIndigo, modifier = Modifier.size(18.dp))
                                    }
                                    Column {
                                        Text(log.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        if (strat != null) {
                                            Text("Strategy: ${strat.title}", style = MaterialTheme.typography.labelSmall, color = ElectricIndigo)
                                        }
                                    }
                                }
                                Surface(shape = RoundedCornerShape(6.dp), color = SlateDark800) {
                                    Text(
                                        text = "+${log.durationMinutes}m",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = EmeraldGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
