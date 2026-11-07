package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import com.example.model.Strategy
import com.example.model.StrategyCategory
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.StudyViewModel

@Composable
fun StrategiesScreen(
    viewModel: StudyViewModel,
    initialExpandedStrategyId: Int? = null,
    modifier: Modifier = Modifier
) {
    val strategies by viewModel.strategies.collectAsState()
    var selectedCategory by remember { mutableStateOf(StrategyCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedStrategyId by remember { mutableStateOf(initialExpandedStrategyId) }

    val filteredStrategies = remember(strategies, selectedCategory, searchQuery) {
        strategies.filter { strat ->
            val matchesCategory = selectedCategory == StrategyCategory.ALL || strat.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                strat.title.contains(searchQuery, ignoreCase = true) ||
                strat.subtitle.contains(searchQuery, ignoreCase = true) ||
                strat.whyItWorks.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Title Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "16 Smart Strategies on How to Learn Faster",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Cognitive neuroscience techniques for 300% faster acquisition and permanent retention.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search 16 strategies, techniques, tips...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("strategy_search_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(StrategyCategory.values()) { cat ->
                    FilterChip(
                        selected = cat == selectedCategory,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.label) },
                        modifier = Modifier.testTag("cat_chip_${cat.name.lowercase()}")
                    )
                }
            }
        }

        // List of 16 Strategies
        items(filteredStrategies, key = { it.id }) { strategy ->
            val isExpanded = expandedStrategyId == strategy.id
            StrategyCardItem(
                strategy = strategy,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedStrategyId = if (isExpanded) null else strategy.id
                },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun StrategyCardItem(
    strategy: Strategy,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    viewModel: StudyViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isExpanded) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricIndigo) else null,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("strategy_card_${strategy.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Number Badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (strategy.isMastered) GoldenAmber.copy(alpha = 0.2f) else ElectricIndigo.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${strategy.id}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (strategy.isMastered) GoldenAmber else ElectricIndigo
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = strategy.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (strategy.isMastered) {
                                Icon(Icons.Default.Star, contentDescription = "Mastered", tint = GoldenAmber, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text(
                            text = strategy.category.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricIndigo
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("edit_strategy_${strategy.id}")
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Strategy",
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
            }

            Text(
                text = strategy.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Expanded view with Deep Dive and Interactive Lab
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Why It Works
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SlateDark800.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = LuminousCyan, modifier = Modifier.size(16.dp))
                                Text("Why It Works (Cognitive Science)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = LuminousCyan)
                            }
                            Text(strategy.whyItWorks, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    // 3 Actionable Steps
                    Text("Actionable Implementation Protocol:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    strategy.actionableSteps.forEach { step ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                            Text(step, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    // Pro-Tip
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GoldenAmber.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = GoldenAmber, modifier = Modifier.size(18.dp))
                            Column {
                                Text("Pro Tip:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = GoldenAmber)
                                Text(strategy.proTip, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    // Strategy Lab
                    StrategyLabRouter(
                        strategyId = strategy.id,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        EditStrategyDialog(
            strategy = strategy,
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                viewModel.updateStrategy(updated)
                showEditDialog = false
            },
            onReset = {
                viewModel.resetStrategy(strategy.id)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditStrategyDialog(
    strategy: Strategy,
    onDismiss: () -> Unit,
    onSave: (Strategy) -> Unit,
    onReset: () -> Unit
) {
    var title by remember { mutableStateOf(strategy.title) }
    var subtitle by remember { mutableStateOf(strategy.subtitle) }
    var whyItWorks by remember { mutableStateOf(strategy.whyItWorks) }
    var proTip by remember { mutableStateOf(strategy.proTip) }
    var stepsText by remember { mutableStateOf(strategy.actionableSteps.joinToString("\n")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Strategy #${strategy.id}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Strategy Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text("Subtitle / Essence") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = whyItWorks,
                        onValueChange = { whyItWorks = it },
                        label = { Text("Why It Works (Cognitive Science)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
                item {
                    OutlinedTextField(
                        value = stepsText,
                        onValueChange = { stepsText = it },
                        label = { Text("Actionable Steps (1 per line)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
                item {
                    OutlinedTextField(
                        value = proTip,
                        onValueChange = { proTip = it },
                        label = { Text("Pro Tip") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val stepsList = stepsText.split("\n")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    onSave(
                        strategy.copy(
                            title = title.ifBlank { strategy.title },
                            subtitle = subtitle,
                            whyItWorks = whyItWorks,
                            proTip = proTip,
                            actionableSteps = if (stepsList.isNotEmpty()) stepsList else strategy.actionableSteps
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
            ) {
                Text("Save Edits")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onReset) {
                    Text("Reset", color = GoldenAmber)
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
