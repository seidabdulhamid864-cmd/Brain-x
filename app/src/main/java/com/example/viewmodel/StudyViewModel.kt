package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StudyRepository
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class TimerMode(val defaultSeconds: Int, val label: String) {
    POMODORO_25(25 * 60, "25m Focus Sprint"),
    POMODORO_50(50 * 60, "50m Deep Flow"),
    SHORT_BREAK(5 * 60, "5m Mindful Break"),
    LONG_BREAK(15 * 60, "15m Restorative Rest")
}

data class TimerUiState(
    val mode: TimerMode = TimerMode.POMODORO_25,
    val remainingSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val completedSessions: Int = 0,
    val associatedStrategyId: Int? = 6
)

enum class BreathPhase(val label: String, val instruction: String, val seconds: Int) {
    INHALE("Inhale", "Fill lungs deeply through the nose", 4),
    HOLD_FULL("Hold", "Retain air, relax your shoulders", 4),
    EXHALE("Exhale", "Release smoothly through the mouth", 4),
    HOLD_EMPTY("Rest", "Stillness, clear the mind", 4)
}

data class MeditationUiState(
    val isActive: Boolean = false,
    val currentPhase: BreathPhase = BreathPhase.INHALE,
    val phaseRemainingSeconds: Int = 4,
    val cyclesCompleted: Int = 0
)

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StudyRepository(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val strategies: StateFlow<List<Strategy>> = repository.strategies
    val habits: StateFlow<List<StudyHabit>> = repository.habits
    val learningPaths: StateFlow<List<LearningPath>> = repository.learningPaths
    val flashcards: StateFlow<List<FlashcardItem>> = repository.flashcards
    val cornellNotes: StateFlow<List<CornellNoteItem>> = repository.cornellNotes
    val mindMapNodes: StateFlow<List<MindMapNode>> = repository.mindMapNodes
    val dailyHealth: StateFlow<DailyHealthLog> = repository.dailyHealth
    val sessionLogs: StateFlow<List<StudySessionLog>> = repository.sessionLogs

    // Active learning path derived state
    val activePath: StateFlow<LearningPath?> = combine(userProfile, learningPaths) { profile, paths ->
        paths.find { it.id == profile.activePathId } ?: paths.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Focus Timer state
    private val _timerState = MutableStateFlow(TimerUiState())
    val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()
    private var timerJob: Job? = null

    // Meditation / Box Breathing state
    private val _meditationState = MutableStateFlow(MeditationUiState())
    val meditationState: StateFlow<MeditationUiState> = _meditationState.asStateFlow()
    private var meditationJob: Job? = null

    // Feynman 60s Out Loud Timer state
    private val _feynmanSecondsLeft = MutableStateFlow(60)
    val feynmanSecondsLeft: StateFlow<Int> = _feynmanSecondsLeft.asStateFlow()
    private val _isFeynmanRunning = MutableStateFlow(false)
    val isFeynmanRunning: StateFlow<Boolean> = _isFeynmanRunning.asStateFlow()
    private var feynmanJob: Job? = null

    // Vibrator for tactile feedback
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun triggerHaptic(durationMs: Long = 60) {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        }
    }

    // --- Personalization Service ---
    fun customizeLearningPath(
        title: String,
        description: String,
        goal: StudyGoal,
        learnerType: LearnerType,
        dailyHoursMinutes: Int,
        selectedStrategyIds: List<Int>,
        selectedHabitIds: List<Int>,
        morningRoutine: String,
        studyRoutine: String,
        eveningRoutine: String,
        struggle: String
    ) {
        val current = userProfile.value
        val updatedProfile = current.copy(
            learnerType = learnerType,
            primaryGoal = goal,
            dailyMinutesTarget = dailyHoursMinutes,
            mainStruggle = struggle
        )
        repository.updateProfile(updatedProfile)

        val newPath = repository.applyCustomLearningPath(
            pathTitle = title,
            description = description,
            dailyMinutes = dailyHoursMinutes,
            selectedStrategyIds = selectedStrategyIds,
            selectedHabitIds = selectedHabitIds,
            morningRoutine = morningRoutine,
            studyRoutine = studyRoutine,
            eveningRoutine = eveningRoutine
        )
        repository.setActivePath(newPath.id)
        triggerHaptic(100)
    }

    fun selectPath(pathId: String) {
        repository.setActivePath(pathId)
        triggerHaptic(50)
    }

    // --- Timer Actions ---
    fun setTimerMode(mode: TimerMode, associatedStratId: Int? = null) {
        pauseTimer()
        _timerState.value = _timerState.value.copy(
            mode = mode,
            remainingSeconds = mode.defaultSeconds,
            isRunning = false,
            associatedStrategyId = associatedStratId ?: _timerState.value.associatedStrategyId
        )
    }

    fun startTimer() {
        if (_timerState.value.isRunning) return
        _timerState.value = _timerState.value.copy(isRunning = true)
        triggerHaptic(50)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000)
                val newRemain = _timerState.value.remainingSeconds - 1
                _timerState.value = _timerState.value.copy(remainingSeconds = newRemain)
            }
            if (_timerState.value.remainingSeconds <= 0) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(isRunning = false)
        triggerHaptic(40)
    }

    fun resetTimer() {
        pauseTimer()
        _timerState.value = _timerState.value.copy(
            remainingSeconds = _timerState.value.mode.defaultSeconds
        )
    }

    private fun onTimerFinished() {
        triggerHaptic(300)
        val mode = _timerState.value.mode
        val completed = _timerState.value.completedSessions + 1
        _timerState.value = _timerState.value.copy(
            isRunning = false,
            completedSessions = completed
        )
        // Log study session if focus mode
        if (mode == TimerMode.POMODORO_25 || mode == TimerMode.POMODORO_50) {
            val minutes = mode.defaultSeconds / 60
            repository.logStudySession(
                title = mode.label,
                durationMinutes = minutes,
                strategyId = _timerState.value.associatedStrategyId ?: 6
            )
        }
    }

    // --- Meditation (Box Breathing) ---
    fun toggleMeditation() {
        if (_meditationState.value.isActive) {
            stopMeditation()
        } else {
            startMeditation()
        }
    }

    private fun startMeditation() {
        _meditationState.value = MeditationUiState(isActive = true, currentPhase = BreathPhase.INHALE, phaseRemainingSeconds = 4)
        triggerHaptic(80)

        meditationJob?.cancel()
        meditationJob = viewModelScope.launch {
            while (_meditationState.value.isActive) {
                delay(1000)
                val current = _meditationState.value
                if (current.phaseRemainingSeconds > 1) {
                    _meditationState.value = current.copy(phaseRemainingSeconds = current.phaseRemainingSeconds - 1)
                } else {
                    // Next phase
                    val (nextPhase, completedIncrement) = when (current.currentPhase) {
                        BreathPhase.INHALE -> BreathPhase.HOLD_FULL to 0
                        BreathPhase.HOLD_FULL -> BreathPhase.EXHALE to 0
                        BreathPhase.EXHALE -> BreathPhase.HOLD_EMPTY to 0
                        BreathPhase.HOLD_EMPTY -> BreathPhase.INHALE to 1
                    }
                    triggerHaptic(40)
                    _meditationState.value = current.copy(
                        currentPhase = nextPhase,
                        phaseRemainingSeconds = nextPhase.seconds,
                        cyclesCompleted = current.cyclesCompleted + completedIncrement
                    )
                    if (completedIncrement > 0 && current.cyclesCompleted + 1 >= 4) {
                        // 4 cycles done = 1 min
                        repository.recordStrategyPractice(12, 20)
                    }
                }
            }
        }
    }

    fun stopMeditation() {
        meditationJob?.cancel()
        _meditationState.value = MeditationUiState(isActive = false)
        triggerHaptic(40)
    }

    // --- Feynman Vocal Timer ---
    fun startFeynmanTimer() {
        _feynmanSecondsLeft.value = 60
        _isFeynmanRunning.value = true
        triggerHaptic(60)

        feynmanJob?.cancel()
        feynmanJob = viewModelScope.launch {
            while (_feynmanSecondsLeft.value > 0 && _isFeynmanRunning.value) {
                delay(1000)
                _feynmanSecondsLeft.value = _feynmanSecondsLeft.value - 1
            }
            if (_feynmanSecondsLeft.value == 0) {
                triggerHaptic(200)
                repository.recordStrategyPractice(8, 25)
            }
            _isFeynmanRunning.value = false
        }
    }

    fun resetFeynmanTimer() {
        feynmanJob?.cancel()
        _isFeynmanRunning.value = false
        _feynmanSecondsLeft.value = 60
    }

    // --- Strategy Customization ---
    fun updateStrategy(updated: Strategy) {
        repository.updateStrategy(updated)
        triggerHaptic(50)
    }

    fun resetStrategy(strategyId: Int) {
        repository.resetStrategy(strategyId)
        triggerHaptic(50)
    }

    // --- Habits ---
    fun toggleHabit(habitId: Int) {
        repository.toggleHabit(habitId)
        triggerHaptic(40)
    }

    fun updateHabit(updated: StudyHabit) {
        repository.updateHabit(updated)
        triggerHaptic(50)
    }

    fun addNewHabit(title: String, subtitle: String, guide: String, checklist: List<String>) {
        repository.addNewHabit(title, subtitle, guide, checklist)
        triggerHaptic(60)
    }

    fun deleteHabit(habitId: Int) {
        repository.deleteHabit(habitId)
        triggerHaptic(50)
    }

    // --- Flashcards ---
    fun addFlashcard(question: String, answer: String, category: String) {
        repository.addFlashcard(question, answer, category)
        triggerHaptic(50)
    }

    fun updateFlashcard(card: FlashcardItem) {
        repository.updateFlashcard(card)
        triggerHaptic(50)
    }

    fun deleteFlashcard(cardId: String) {
        repository.deleteFlashcard(cardId)
        triggerHaptic(50)
    }

    fun reviewFlashcard(cardId: String, remembered: Boolean) {
        repository.reviewFlashcard(cardId, remembered)
        triggerHaptic(40)
    }

    // --- Cornell Notes ---
    fun saveCornellNote(title: String, cues: String, notes: String, summary: String) {
        repository.saveCornellNote(title, cues, notes, summary)
        triggerHaptic(60)
    }

    // --- Mind Map ---
    fun addMindMapNode(text: String, parentId: String?) {
        repository.addMindMapNode(text, parentId)
        triggerHaptic(40)
    }

    fun clearMindMap() {
        repository.clearMindMap()
        triggerHaptic(60)
    }

    // --- Daily Health Log ---
    fun updateDailyHealth(sleep: Float, water: Int, exercise: Boolean, nutrition: Boolean) {
        repository.updateHealthLog(sleep, water, exercise, nutrition)
        triggerHaptic(50)
    }

    // --- Manual Session Log ---
    fun logStudySession(title: String, durationMinutes: Int, strategyId: Int?) {
        repository.logStudySession(title, durationMinutes, strategyId)
        triggerHaptic(50)
    }

    fun recordStrategyPractice(strategyId: Int, xpEarned: Int = 20) {
        repository.recordStrategyPractice(strategyId, xpEarned)
        triggerHaptic(40)
    }
}
