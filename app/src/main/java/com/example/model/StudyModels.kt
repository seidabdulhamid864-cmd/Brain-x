package com.example.model

enum class LearnerType(val label: String, val description: String) {
    CONCEPTUAL("Conceptual / Big Picture", "Understands through deep principles, connections, and mental models"),
    VISUAL("Visual / Spatial", "Thrives with diagrams, mind maps, color coding, and visual outlines"),
    ACTIVE("Active / Kinesthetic", "Learns best by doing, self-testing, deliberate practice, and teaching"),
    AUDITORY("Auditory / Verbal", "Excels with say-out-loud repetition, lectures, and group discussion")
}

enum class StudyGoal(val title: String, val description: String, val iconName: String) {
    EXAM_PREP("Exam & Test Mastery", "High-retention exam prep using active recall, spaced repetition, and practice exams", "School"),
    STEM_DEEP("STEM & Deep Problem Solving", "Master complex math, science, and coding concepts from first principles", "Psychology"),
    LANGUAGE_MEM("Language & High-Volume Memorization", "Rapid vocabulary and terminology acquisition via mnemonics and Leitner boxes", "Translate"),
    SKILL_BLITZ("Rapid Skill Acquisition", "Accelerated learning curves using deliberate practice and intense feedback loops", "Bolt"),
    BALANCED_HABITS("Sustainable Study Habits", "Build distraction-free routines, calm focus, energy management, and consistency", "SelfImprovement")
}

enum class StrategyCategory(val label: String) {
    ALL("All 16"),
    MEMORY("Memory & Recall"),
    COMPREHENSION("Deep Comprehension"),
    FOCUS_WELLNESS("Focus & Energy"),
    PRACTICE("Practice & Synthesis")
}

data class Strategy(
    val id: Int,
    val title: String,
    val subtitle: String,
    val category: StrategyCategory,
    val whyItWorks: String,
    val actionableSteps: List<String>,
    val proTip: String,
    val isMastered: Boolean = false,
    val practiceCount: Int = 0
)

data class StudyHabit(
    val id: Int,
    val title: String,
    val subtitle: String,
    val actionGuide: String,
    val checklistItems: List<String>,
    val isCompletedToday: Boolean = false,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
)

data class LearningPath(
    val id: String,
    val title: String,
    val description: String,
    val goal: StudyGoal,
    val dailyTargetMinutes: Int,
    val keyStrategyIds: List<Int>,
    val coreHabitIds: List<Int>,
    val morningRoutine: String,
    val studySessionRoutine: String,
    val eveningReviewRoutine: String,
    val isCustom: Boolean = false
)

data class UserProfile(
    val userName: String = "Scholar",
    val learnerType: LearnerType = LearnerType.CONCEPTUAL,
    val primaryGoal: StudyGoal = StudyGoal.EXAM_PREP,
    val dailyMinutesTarget: Int = 90,
    val activePathId: String = "exam_prep",
    val mainStruggle: String = "Forgetting concepts quickly",
    val totalXp: Int = 150,
    val level: Int = 1,
    val currentStreak: Int = 3,
    val totalMinutesStudied: Int = 135
)

data class FlashcardItem(
    val id: String,
    val question: String,
    val answer: String,
    val category: String,
    val box: Int = 1, // Leitner Box 1 to 5
    val nextReviewDayOffset: Int = 1,
    val lastReviewedTimestamp: Long = 0L
)

data class CornellNoteItem(
    val id: String,
    val title: String,
    val cues: String,
    val notes: String,
    val summary: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class MindMapNode(
    val id: String,
    val text: String,
    val parentId: String? = null,
    val colorIndex: Int = 0
)

data class DailyHealthLog(
    val dateString: String,
    val sleepHours: Float = 7.5f,
    val waterGlasses: Int = 6,
    val didExercise: Boolean = true,
    val healthyNutrition: Boolean = true,
    val readinessScore: Int = 85
)

data class StudySessionLog(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val strategyId: Int?,
    val timestamp: Long = System.currentTimeMillis()
)
