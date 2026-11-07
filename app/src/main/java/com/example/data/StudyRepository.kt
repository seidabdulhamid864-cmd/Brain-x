package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class StudyRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("studyflow_prefs", Context.MODE_PRIVATE)

    // In-memory observable flows backed by persistent SharedPreferences
    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _strategies = MutableStateFlow(loadStrategies())
    val strategies: StateFlow<List<Strategy>> = _strategies.asStateFlow()

    private val _habits = MutableStateFlow(loadHabits())
    val habits: StateFlow<List<StudyHabit>> = _habits.asStateFlow()

    private val _learningPaths = MutableStateFlow(loadLearningPaths())
    val learningPaths: StateFlow<List<LearningPath>> = _learningPaths.asStateFlow()

    private val _flashcards = MutableStateFlow(loadFlashcards())
    val flashcards: StateFlow<List<FlashcardItem>> = _flashcards.asStateFlow()

    private val _cornellNotes = MutableStateFlow(loadCornellNotes())
    val cornellNotes: StateFlow<List<CornellNoteItem>> = _cornellNotes.asStateFlow()

    private val _mindMapNodes = MutableStateFlow(loadMindMapNodes())
    val mindMapNodes: StateFlow<List<MindMapNode>> = _mindMapNodes.asStateFlow()

    private val _dailyHealth = MutableStateFlow(loadHealthLog())
    val dailyHealth: StateFlow<DailyHealthLog> = _dailyHealth.asStateFlow()

    private val _sessionLogs = MutableStateFlow(loadSessionLogs())
    val sessionLogs: StateFlow<List<StudySessionLog>> = _sessionLogs.asStateFlow()

    // --- Profile & Customization Service ---
    fun updateProfile(newProfile: UserProfile) {
        _userProfile.value = newProfile
        prefs.edit().apply {
            putString("user_name", newProfile.userName)
            putString("learner_type", newProfile.learnerType.name)
            putString("primary_goal", newProfile.primaryGoal.name)
            putInt("daily_target_min", newProfile.dailyMinutesTarget)
            putString("active_path_id", newProfile.activePathId)
            putString("main_struggle", newProfile.mainStruggle)
            putInt("total_xp", newProfile.totalXp)
            putInt("level", newProfile.level)
            putInt("current_streak", newProfile.currentStreak)
            putInt("total_minutes", newProfile.totalMinutesStudied)
            apply()
        }
    }

    fun applyCustomLearningPath(
        pathTitle: String,
        description: String,
        dailyMinutes: Int,
        selectedStrategyIds: List<Int>,
        selectedHabitIds: List<Int>,
        morningRoutine: String,
        studyRoutine: String,
        eveningRoutine: String
    ): LearningPath {
        val customId = "custom_${System.currentTimeMillis()}"
        val newPath = LearningPath(
            id = customId,
            title = pathTitle,
            description = description,
            goal = _userProfile.value.primaryGoal,
            dailyTargetMinutes = dailyMinutes,
            keyStrategyIds = selectedStrategyIds,
            coreHabitIds = selectedHabitIds,
            morningRoutine = morningRoutine,
            studySessionRoutine = studyRoutine,
            eveningReviewRoutine = eveningRoutine,
            isCustom = true
        )
        val updated = _learningPaths.value.toMutableList().apply { add(0, newPath) }
        _learningPaths.value = updated
        saveLearningPaths(updated)

        // Set as active path
        val updatedProfile = _userProfile.value.copy(
            activePathId = customId,
            dailyMinutesTarget = dailyMinutes
        )
        updateProfile(updatedProfile)
        return newPath
    }

    fun setActivePath(pathId: String) {
        val targetPath = _learningPaths.value.find { it.id == pathId }
        val updatedTargetMin = targetPath?.dailyTargetMinutes ?: _userProfile.value.dailyMinutesTarget
        val updatedProfile = _userProfile.value.copy(
            activePathId = pathId,
            dailyMinutesTarget = updatedTargetMin
        )
        updateProfile(updatedProfile)
    }

    fun addXpAndMinutes(xpToAdd: Int, minutesToAdd: Int) {
        val current = _userProfile.value
        val newXp = current.totalXp + xpToAdd
        val newLevel = (newXp / 100) + 1
        val newMinutes = current.totalMinutesStudied + minutesToAdd
        updateProfile(current.copy(totalXp = newXp, level = newLevel, totalMinutesStudied = newMinutes))
    }

    // --- Strategy Editing & Tracking ---
    fun updateStrategy(updated: Strategy) {
        val list = _strategies.value.map {
            if (it.id == updated.id) updated else it
        }
        _strategies.value = list
        saveStrategies(list)
    }

    fun resetStrategy(strategyId: Int) {
        val defaultStrat = DefaultStudyData.allStrategies.find { it.id == strategyId } ?: return
        val current = _strategies.value.find { it.id == strategyId }
        val restored = defaultStrat.copy(
            practiceCount = current?.practiceCount ?: 0,
            isMastered = current?.isMastered ?: false
        )
        val list = _strategies.value.map {
            if (it.id == strategyId) restored else it
        }
        _strategies.value = list
        saveStrategies(list)
    }

    fun recordStrategyPractice(strategyId: Int, xpEarned: Int = 20) {
        val list = _strategies.value.map {
            if (it.id == strategyId) {
                it.copy(practiceCount = it.practiceCount + 1, isMastered = it.practiceCount + 1 >= 3)
            } else it
        }
        _strategies.value = list
        saveStrategies(list)
        addXpAndMinutes(xpEarned, 5)
    }

    // --- Habits Tracking ---
    fun toggleHabit(habitId: Int) {
        val list = _habits.value.map { habit ->
            if (habit.id == habitId) {
                val newCompleted = !habit.isCompletedToday
                val newStreak = if (newCompleted) habit.currentStreak + 1 else maxOf(0, habit.currentStreak - 1)
                val newBest = maxOf(habit.bestStreak, newStreak)
                habit.copy(isCompletedToday = newCompleted, currentStreak = newStreak, bestStreak = newBest)
            } else habit
        }
        _habits.value = list
        saveHabits(list)
        addXpAndMinutes(if (list.find { it.id == habitId }?.isCompletedToday == true) 15 else -15, 0)
    }

    fun updateHabit(updated: StudyHabit) {
        val list = _habits.value.map {
            if (it.id == updated.id) updated else it
        }
        _habits.value = list
        saveHabits(list)
    }

    fun addNewHabit(title: String, subtitle: String, guide: String, checklist: List<String>): StudyHabit {
        val nextId = (_habits.value.maxOfOrNull { it.id } ?: 8) + 1
        val newHabit = StudyHabit(
            id = nextId,
            title = title,
            subtitle = subtitle,
            actionGuide = guide,
            checklistItems = checklist,
            isCompletedToday = false,
            currentStreak = 0,
            bestStreak = 0
        )
        val updated = _habits.value + newHabit
        _habits.value = updated
        saveHabits(updated)
        addXpAndMinutes(15, 0)
        return newHabit
    }

    fun deleteHabit(habitId: Int) {
        val updated = _habits.value.filterNot { it.id == habitId }
        _habits.value = updated
        saveHabits(updated)
    }

    // --- Flashcards (Active Recall & Spaced Repetition) ---
    fun addFlashcard(question: String, answer: String, category: String) {
        val newCard = FlashcardItem(
            id = "fc_${System.currentTimeMillis()}",
            question = question,
            answer = answer,
            category = category,
            box = 1,
            nextReviewDayOffset = 1
        )
        val updated = _flashcards.value + newCard
        _flashcards.value = updated
        saveFlashcards(updated)
        addXpAndMinutes(10, 2)
    }

    fun reviewFlashcard(cardId: String, remembered: Boolean) {
        val updated = _flashcards.value.map { card ->
            if (card.id == cardId) {
                val newBox = if (remembered) minOf(5, card.box + 1) else 1
                val offsetDays = when (newBox) {
                    1 -> 1
                    2 -> 3
                    3 -> 7
                    4 -> 14
                    else -> 30
                }
                card.copy(
                    box = newBox,
                    nextReviewDayOffset = offsetDays,
                    lastReviewedTimestamp = System.currentTimeMillis()
                )
            } else card
        }
        _flashcards.value = updated
        saveFlashcards(updated)
        addXpAndMinutes(if (remembered) 15 else 5, 2)
    }

    fun updateFlashcard(card: FlashcardItem) {
        val updated = _flashcards.value.map {
            if (it.id == card.id) card else it
        }
        _flashcards.value = updated
        saveFlashcards(updated)
    }

    fun deleteFlashcard(cardId: String) {
        val updated = _flashcards.value.filterNot { it.id == cardId }
        _flashcards.value = updated
        saveFlashcards(updated)
    }

    // --- Cornell Notes ---
    fun saveCornellNote(title: String, cues: String, notes: String, summary: String): CornellNoteItem {
        val note = CornellNoteItem(
            id = "note_${System.currentTimeMillis()}",
            title = title,
            cues = cues,
            notes = notes,
            summary = summary
        )
        val updated = listOf(note) + _cornellNotes.value
        _cornellNotes.value = updated
        saveCornellNotes(updated)
        recordStrategyPractice(2, 25)
        return note
    }

    // --- Mind Map ---
    fun addMindMapNode(text: String, parentId: String?) {
        val node = MindMapNode(
            id = "node_${System.currentTimeMillis()}",
            text = text,
            parentId = parentId,
            colorIndex = ((_mindMapNodes.value.size % 5))
        )
        val updated = _mindMapNodes.value + node
        _mindMapNodes.value = updated
        saveMindMapNodes(updated)
        recordStrategyPractice(14, 15)
    }

    fun clearMindMap() {
        val defaultNode = listOf(MindMapNode("root", "Core Central Concept", null, 0))
        _mindMapNodes.value = defaultNode
        saveMindMapNodes(defaultNode)
    }

    // --- Health & Energy ---
    fun updateHealthLog(sleep: Float, water: Int, exercise: Boolean, nutrition: Boolean) {
        var score = 50
        if (sleep in 7.0f..9.0f) score += 20 else if (sleep >= 6.0f) score += 10
        score += (water * 3).coerceAtMost(15)
        if (exercise) score += 10
        if (nutrition) score += 10
        val log = DailyHealthLog(
            dateString = "Today",
            sleepHours = sleep,
            waterGlasses = water,
            didExercise = exercise,
            healthyNutrition = nutrition,
            readinessScore = score.coerceAtMost(100)
        )
        _dailyHealth.value = log
        saveHealthLog(log)
        recordStrategyPractice(7, 20)
    }

    // --- Study Session Logs ---
    fun logStudySession(title: String, durationMinutes: Int, strategyId: Int?) {
        val log = StudySessionLog(
            id = "sess_${System.currentTimeMillis()}",
            title = title,
            durationMinutes = durationMinutes,
            strategyId = strategyId
        )
        val updated = listOf(log) + _sessionLogs.value
        _sessionLogs.value = updated
        saveSessionLogs(updated)
        addXpAndMinutes(durationMinutes * 2, durationMinutes)
        if (strategyId != null) {
            recordStrategyPractice(strategyId, 10)
        }
    }

    // --- Persistence Implementations ---
    private fun loadUserProfile(): UserProfile {
        val name = prefs.getString("user_name", "Alex") ?: "Alex"
        val lTypeStr = prefs.getString("learner_type", LearnerType.CONCEPTUAL.name)
        val lType = runCatching { LearnerType.valueOf(lTypeStr ?: "") }.getOrDefault(LearnerType.CONCEPTUAL)
        val goalStr = prefs.getString("primary_goal", StudyGoal.EXAM_PREP.name)
        val goal = runCatching { StudyGoal.valueOf(goalStr ?: "") }.getOrDefault(StudyGoal.EXAM_PREP)
        val targetMin = prefs.getInt("daily_target_min", 90)
        val activePath = prefs.getString("active_path_id", "exam_prep") ?: "exam_prep"
        val struggle = prefs.getString("main_struggle", "Forgetting concepts quickly") ?: "Forgetting concepts quickly"
        val xp = prefs.getInt("total_xp", 180)
        val level = prefs.getInt("level", 2)
        val streak = prefs.getInt("current_streak", 4)
        val totalMinutes = prefs.getInt("total_minutes", 145)

        return UserProfile(
            userName = name,
            learnerType = lType,
            primaryGoal = goal,
            dailyMinutesTarget = targetMin,
            activePathId = activePath,
            mainStruggle = struggle,
            totalXp = xp,
            level = level,
            currentStreak = streak,
            totalMinutesStudied = totalMinutes
        )
    }

    private fun loadStrategies(): List<Strategy> {
        val raw = prefs.getString("strategies_data", null)
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                val list = mutableListOf<Strategy>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val id = obj.getInt("id")
                    val defaultStrat = DefaultStudyData.allStrategies.find { it.id == id }
                    if (defaultStrat != null) {
                        val steps = if (obj.has("actionableSteps")) {
                            val arr = obj.getJSONArray("actionableSteps")
                            (0 until arr.length()).map { idx -> arr.getString(idx) }
                        } else defaultStrat.actionableSteps

                        list.add(
                            defaultStrat.copy(
                                title = obj.optString("title", defaultStrat.title),
                                subtitle = obj.optString("subtitle", defaultStrat.subtitle),
                                whyItWorks = obj.optString("whyItWorks", defaultStrat.whyItWorks),
                                proTip = obj.optString("proTip", defaultStrat.proTip),
                                actionableSteps = steps,
                                practiceCount = obj.optInt("practiceCount", 0),
                                isMastered = obj.optBoolean("isMastered", false)
                            )
                        )
                    }
                }
                if (list.size == 16) return list
            } catch (_: Exception) {}
        }
        return DefaultStudyData.allStrategies
    }

    private fun saveStrategies(list: List<Strategy>) {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("title", it.title)
            obj.put("subtitle", it.subtitle)
            obj.put("category", it.category.name)
            obj.put("whyItWorks", it.whyItWorks)
            obj.put("proTip", it.proTip)
            val stepsArr = JSONArray()
            it.actionableSteps.forEach { s -> stepsArr.put(s) }
            obj.put("actionableSteps", stepsArr)
            obj.put("practiceCount", it.practiceCount)
            obj.put("isMastered", it.isMastered)
            array.put(obj)
        }
        prefs.edit().putString("strategies_data", array.toString()).apply()
    }

    private fun loadHabits(): List<StudyHabit> {
        val raw = prefs.getString("habits_data", null)
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                val list = mutableListOf<StudyHabit>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val id = obj.getInt("id")
                    val defaultHabit = DefaultStudyData.allHabits.find { it.id == id }
                    val checkItems = if (obj.has("checklistItems")) {
                        val arr = obj.getJSONArray("checklistItems")
                        (0 until arr.length()).map { idx -> arr.getString(idx) }
                    } else defaultHabit?.checklistItems ?: emptyList()

                    list.add(
                        StudyHabit(
                            id = id,
                            title = obj.optString("title", defaultHabit?.title ?: "Habit $id"),
                            subtitle = obj.optString("subtitle", defaultHabit?.subtitle ?: ""),
                            actionGuide = obj.optString("actionGuide", defaultHabit?.actionGuide ?: ""),
                            checklistItems = checkItems,
                            isCompletedToday = obj.optBoolean("isCompletedToday", false),
                            currentStreak = obj.optInt("currentStreak", 0),
                            bestStreak = obj.optInt("bestStreak", 0)
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        return DefaultStudyData.allHabits
    }

    private fun saveHabits(list: List<StudyHabit>) {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("title", it.title)
            obj.put("subtitle", it.subtitle)
            obj.put("actionGuide", it.actionGuide)
            val itemsArr = JSONArray()
            it.checklistItems.forEach { item -> itemsArr.put(item) }
            obj.put("checklistItems", itemsArr)
            obj.put("isCompletedToday", it.isCompletedToday)
            obj.put("currentStreak", it.currentStreak)
            obj.put("bestStreak", it.bestStreak)
            array.put(obj)
        }
        prefs.edit().putString("habits_data", array.toString()).apply()
    }

    private fun loadLearningPaths(): List<LearningPath> {
        val raw = prefs.getString("learning_paths_custom", null)
        val defaultList = DefaultStudyData.allLearningPaths.toMutableList()
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val keyStrat = mutableListOf<Int>()
                    val stratArr = obj.getJSONArray("keyStrategyIds")
                    for (s in 0 until stratArr.length()) keyStrat.add(stratArr.getInt(s))

                    val coreHabits = mutableListOf<Int>()
                    val habitArr = obj.getJSONArray("coreHabitIds")
                    for (h in 0 until habitArr.length()) coreHabits.add(habitArr.getInt(h))

                    defaultList.add(
                        0,
                        LearningPath(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            description = obj.getString("description"),
                            goal = StudyGoal.valueOf(obj.getString("goal")),
                            dailyTargetMinutes = obj.getInt("dailyTargetMinutes"),
                            keyStrategyIds = keyStrat,
                            coreHabitIds = coreHabits,
                            morningRoutine = obj.getString("morningRoutine"),
                            studySessionRoutine = obj.getString("studySessionRoutine"),
                            eveningReviewRoutine = obj.getString("eveningReviewRoutine"),
                            isCustom = true
                        )
                    )
                }
            } catch (_: Exception) {}
        }
        return defaultList
    }

    private fun saveLearningPaths(list: List<LearningPath>) {
        val customOnly = list.filter { it.isCustom }
        val array = JSONArray()
        customOnly.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("title", it.title)
            obj.put("description", it.description)
            obj.put("goal", it.goal.name)
            obj.put("dailyTargetMinutes", it.dailyTargetMinutes)
            obj.put("morningRoutine", it.morningRoutine)
            obj.put("studySessionRoutine", it.studySessionRoutine)
            obj.put("eveningReviewRoutine", it.eveningReviewRoutine)

            val sArr = JSONArray()
            it.keyStrategyIds.forEach { s -> sArr.put(s) }
            obj.put("keyStrategyIds", sArr)

            val hArr = JSONArray()
            it.coreHabitIds.forEach { h -> hArr.put(h) }
            obj.put("coreHabitIds", hArr)

            array.put(obj)
        }
        prefs.edit().putString("learning_paths_custom", array.toString()).apply()
    }

    private fun loadFlashcards(): List<FlashcardItem> {
        val raw = prefs.getString("flashcards_data", null)
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                val list = mutableListOf<FlashcardItem>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        FlashcardItem(
                            id = obj.getString("id"),
                            question = obj.getString("question"),
                            answer = obj.getString("answer"),
                            category = obj.getString("category"),
                            box = obj.getInt("box"),
                            nextReviewDayOffset = obj.getInt("nextReviewDayOffset"),
                            lastReviewedTimestamp = obj.getLong("lastReviewedTimestamp")
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        return DefaultStudyData.initialFlashcards
    }

    private fun saveFlashcards(list: List<FlashcardItem>) {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("question", it.question)
            obj.put("answer", it.answer)
            obj.put("category", it.category)
            obj.put("box", it.box)
            obj.put("nextReviewDayOffset", it.nextReviewDayOffset)
            obj.put("lastReviewedTimestamp", it.lastReviewedTimestamp)
            array.put(obj)
        }
        prefs.edit().putString("flashcards_data", array.toString()).apply()
    }

    private fun loadCornellNotes(): List<CornellNoteItem> {
        val raw = prefs.getString("cornell_notes_data", null)
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                val list = mutableListOf<CornellNoteItem>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        CornellNoteItem(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            cues = obj.getString("cues"),
                            notes = obj.getString("notes"),
                            summary = obj.getString("summary"),
                            timestamp = obj.getLong("timestamp")
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        return DefaultStudyData.initialNotes
    }

    private fun saveCornellNotes(list: List<CornellNoteItem>) {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("title", it.title)
            obj.put("cues", it.cues)
            obj.put("notes", it.notes)
            obj.put("summary", it.summary)
            obj.put("timestamp", it.timestamp)
            array.put(obj)
        }
        prefs.edit().putString("cornell_notes_data", array.toString()).apply()
    }

    private fun loadMindMapNodes(): List<MindMapNode> {
        val raw = prefs.getString("mindmap_data", null)
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                val list = mutableListOf<MindMapNode>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        MindMapNode(
                            id = obj.getString("id"),
                            text = obj.getString("text"),
                            parentId = if (obj.has("parentId") && !obj.isNull("parentId")) obj.getString("parentId") else null,
                            colorIndex = obj.getInt("colorIndex")
                        )
                    )
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }
        return DefaultStudyData.initialMindMap
    }

    private fun saveMindMapNodes(list: List<MindMapNode>) {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("text", it.text)
            if (it.parentId != null) obj.put("parentId", it.parentId) else obj.put("parentId", JSONObject.NULL)
            obj.put("colorIndex", it.colorIndex)
            array.put(obj)
        }
        prefs.edit().putString("mindmap_data", array.toString()).apply()
    }

    private fun loadHealthLog(): DailyHealthLog {
        val sleep = prefs.getFloat("health_sleep", 7.5f)
        val water = prefs.getInt("health_water", 6)
        val ex = prefs.getBoolean("health_ex", true)
        val nut = prefs.getBoolean("health_nut", true)
        val score = prefs.getInt("health_score", 85)
        return DailyHealthLog("Today", sleep, water, ex, nut, score)
    }

    private fun saveHealthLog(log: DailyHealthLog) {
        prefs.edit().apply {
            putFloat("health_sleep", log.sleepHours)
            putInt("health_water", log.waterGlasses)
            putBoolean("health_ex", log.didExercise)
            putBoolean("health_nut", log.healthyNutrition)
            putInt("health_score", log.readinessScore)
            apply()
        }
    }

    private fun loadSessionLogs(): List<StudySessionLog> {
        val raw = prefs.getString("session_logs", null)
        if (raw != null) {
            try {
                val array = JSONArray(raw)
                val list = mutableListOf<StudySessionLog>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        StudySessionLog(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            durationMinutes = obj.getInt("durationMinutes"),
                            strategyId = if (obj.has("strategyId") && !obj.isNull("strategyId")) obj.getInt("strategyId") else null,
                            timestamp = obj.getLong("timestamp")
                        )
                    )
                }
                return list
            } catch (_: Exception) {}
        }
        return DefaultStudyData.initialSessionLogs
    }

    private fun saveSessionLogs(list: List<StudySessionLog>) {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("title", it.title)
            obj.put("durationMinutes", it.durationMinutes)
            if (it.strategyId != null) obj.put("strategyId", it.strategyId) else obj.put("strategyId", JSONObject.NULL)
            obj.put("timestamp", it.timestamp)
            array.put(obj)
        }
        prefs.edit().putString("session_logs", array.toString()).apply()
    }
}
