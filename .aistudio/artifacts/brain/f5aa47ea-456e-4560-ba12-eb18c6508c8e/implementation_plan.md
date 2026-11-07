# StudyFlow: Universal Installation Readiness & In-App Content Editing

Comprehensive plan to resolve all build and installation bottlenecks for universal Android device deployment, and empower users with full in-app editing capabilities across all 16 learning strategies, 8 study habits, and spaced repetition flashcards.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following directions have been confirmed and will guide the execution:
> - **Confirmed**: Full in-app editing enabled across all strategies, habits, and flashcards.
> - **Confirmed**: Universal signed APK configuration verified for direct sideloading and installation on any Android phone (Android 7.0 / SDK 24 through Android 15 / SDK 36).

---

## 1. Overview & Core Concept

* **Universal Installation Guarantee**: Ensure the APK compiles and signs cleanly with standard Android keystore configurations, zero missing-manifest permissions, correct launcher activity export directives, and robust offline execution so that any Android device can install and run the app without errors.
* **Full In-App Customization & Editing**: Elevate StudyFlow from a static reference into a dynamic, user-owned knowledge OS where users can:
  * Edit and customize every strategy's title, focus points, and actionable tips.
  * Edit, customize, and add new personal study habits and checklist items.
  * Create, edit, update, and delete active recall flashcards with Leitner box assignment.
  * Edit daily targets, routines, and learner profiles.

---

## 2. User Experience & Visual Design

```
┌────────────────────────────────────────────────────────┐
│                   StudyFlow Main App                   │
├───────────────┬─────────────────┬──────────────────────┤
│ 16 Strategies │ 8 Core Habits   │ Flashcards & Paths   │
├───────────────┼─────────────────┼──────────────────────┤
│ [Edit Action] │ [Edit Action]   │ [Add / Edit / Delete]│
│ Custom Steps  │ Custom Audits   │ Question / Answer    │
│ Pro Tips      │ Custom Guides   │ Leitner Box Adjust   │
│ Reset Defaults│ Add New Habit   │ Daily Target Minutes │
└───────────────┴─────────────────┴──────────────────────┘
```

* **Inline & Modal Editing Workflows**:
  * **Edit Strategy Sheet**: Accessible via an edit icon on each strategy card. Allows editing the subtitle, pro tip, and actionable steps with instant persistent saving. Includes a "Reset to Standard" option.
  * **Edit Habit Sheet**: Accessible via an edit icon on each habit. Allows modifying the title, behavioral guide, and individual audit checklist items, as well as adding brand-new personal study habits.
  * **Flashcard Management**: Tap any flashcard in the active recall lab to edit question text, answer, and category, or delete outdated cards.
* **Installation Feedback & Version Badge**: Clean display of app version (`v1.0 - Universal APK`) in the Analytics tab for verification.

---

## 3. Key Product Decisions & Trade-Offs

* **Decision 1: Persistence for User Edits**
  * *Chosen Approach*: SharedPreferences backed by clean JSON serialization in `StudyRepository`.
  * *Why*: Extremely fast, 100% offline-compatible, and guarantees user customizations persist across app restarts without heavyweight database migrations.
* **Decision 2: Universal Signing & APK Installation Config**
  * *Chosen Approach*: Configure both `debug` and `release` signing configs to use valid signing keys, ensuring APKs generated via assemble tasks or AI Studio settings are pre-signed and installable without "App not installed / Corrupt package" errors.

---

## 4. Technical Architecture & Implementation Steps

```
┌────────────────────────────────────────────────────────┐
│                   UI Layer (Compose)                   │
│  - StrategiesScreen: StrategyEditDialog                │
│  - HabitsScreen: HabitEditDialog & AddHabitDialog      │
│  - StrategyLabs: FlashcardEditDialog & CRUD Controls   │
├────────────────────────────────────────────────────────┤
│                  ViewModel Layer                       │
│  - StudyViewModel: updateStrategy(), updateHabit(),    │
│    addNewHabit(), deleteHabit(), updateFlashcard(),    │
│    deleteFlashcard(), resetStrategyToDefault()         │
├────────────────────────────────────────────────────────┤
│                 Data / Repository                      │
│  - StudyRepository: persistent storage for customized  │
│    strategies, dynamic habit list, and flashcard CRUD  │
├────────────────────────────────────────────────────────┤
│               Build & Packaging Layer                  │
│  - app/build.gradle.kts: universal signingConfigs,     │
│    clean dependencies, zero missing resource errors    │
└────────────────────────────────────────────────────────┘
```

### Planned Code Tasks:
1. **Repository & Data Layer Enhancements (`StudyRepository.kt`)**:
   - Add `updateStrategy(updatedStrategy: Strategy)`.
   - Add `resetStrategy(strategyId: Int)`.
   - Add `updateHabit(updatedHabit: StudyHabit)`.
   - Add `addNewHabit(title: String, subtitle: String, guide: String, checklist: List<String>)`.
   - Add `deleteHabit(habitId: Int)`.
   - Add `updateFlashcard(card: FlashcardItem)`.
   - Add `deleteFlashcard(cardId: String)`.
2. **ViewModel Layer Methods (`StudyViewModel.kt`)**:
   - Expose corresponding editing functions with haptic feedback.
3. **UI Layer Updates (`StrategiesScreen.kt`, `HabitsScreen.kt`, `StrategyLabs.kt`)**:
   - Add "Edit" buttons and responsive dialogs for strategies and habits.
   - Add Edit and Delete controls inside the Flashcard / Retrieval practice lab.
4. **Build Verification**:
   - Ensure clean build via `compile_applet` and test execution.
