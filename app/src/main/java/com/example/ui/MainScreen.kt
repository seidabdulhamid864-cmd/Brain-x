package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Strategy
import com.example.ui.screens.*
import com.example.ui.theme.ElectricIndigo
import com.example.viewmodel.StudyViewModel

enum class NavigationTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD("Today", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "nav_tab_dashboard"),
    PATHS("Paths", Icons.Filled.AltRoute, Icons.Outlined.AltRoute, "nav_tab_paths"),
    STRATEGIES("16 Strategies", Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb, "nav_tab_strategies"),
    HABITS("8 Habits", Icons.Filled.TaskAlt, Icons.Outlined.TaskAlt, "nav_tab_habits"),
    ANALYTICS("Progress", Icons.Filled.QueryStats, Icons.Outlined.QueryStats, "nav_tab_analytics")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: StudyViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(NavigationTab.DASHBOARD) }
    var targetStrategyToOpenId by remember { mutableStateOf<Int?>(null) }

    // Handle back button when on sub-tabs
    BackHandler(enabled = currentTab != NavigationTab.DASHBOARD) {
        currentTab = NavigationTab.DASHBOARD
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "StudyFlow",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = when (currentTab) {
                                    NavigationTab.DASHBOARD -> "Personalized Study Dashboard"
                                    NavigationTab.PATHS -> "Customization Service & Tracks"
                                    NavigationTab.STRATEGIES -> "16 Cognitive Strategies"
                                    NavigationTab.HABITS -> "8 Core Study Habits"
                                    NavigationTab.ANALYTICS -> "Mastery & Progress Metrics"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricIndigo
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                windowInsets = WindowInsets.navigationBars,
                tonalElevation = 8.dp
            ) {
                NavigationTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ElectricIndigo,
                            selectedTextColor = ElectricIndigo,
                            indicatorColor = ElectricIndigo.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavigationTab.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToPaths = { currentTab = NavigationTab.PATHS },
                    onNavigateToStrategies = { currentTab = NavigationTab.STRATEGIES },
                    onOpenStrategyLab = { strat ->
                        targetStrategyToOpenId = strat.id
                        currentTab = NavigationTab.STRATEGIES
                    }
                )

                NavigationTab.PATHS -> PersonalizedPathsScreen(
                    viewModel = viewModel,
                    onOpenStrategy = { strat ->
                        targetStrategyToOpenId = strat.id
                        currentTab = NavigationTab.STRATEGIES
                    }
                )

                NavigationTab.STRATEGIES -> StrategiesScreen(
                    viewModel = viewModel,
                    initialExpandedStrategyId = targetStrategyToOpenId
                )

                NavigationTab.HABITS -> HabitsScreen(
                    viewModel = viewModel
                )

                NavigationTab.ANALYTICS -> AnalyticsScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
