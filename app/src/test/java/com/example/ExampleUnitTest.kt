package com.example

import com.example.data.DefaultStudyData
import com.example.model.LearnerType
import com.example.model.StudyGoal
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testAll16StrategiesPresentAndNumbered() {
        val strategies = DefaultStudyData.allStrategies
        assertEquals(16, strategies.size)

        for (i in 1..16) {
            val s = strategies.find { it.id == i }
            assertNotNull("Strategy $i must exist", s)
            assertTrue("Strategy $i must have a non-empty title", s!!.title.isNotBlank())
            assertTrue("Strategy $i must have actionable steps", s.actionableSteps.isNotEmpty())
            assertTrue("Strategy $i must have a whyItWorks explanation", s.whyItWorks.isNotBlank())
        }

        // Verify specific strategy titles matching user's images
        assertEquals("Mnemonic Techniques", strategies[0].title)
        assertEquals("Take Handwritten Notes", strategies[1].title)
        assertEquals("Peer Learning", strategies[2].title)
        assertEquals("Test Yourself", strategies[3].title)
        assertEquals("Spaced Repetition", strategies[4].title)
        assertEquals("Take Short Breaks", strategies[5].title)
        assertEquals("Say Out Loud", strategies[7].title)
        assertEquals("Mindful Learning", strategies[8].title)
        assertEquals("Understand the Study Cycle", strategies[9].title)
        assertEquals("Active Learning", strategies[10].title)
        assertEquals("Calm Your Mind with Meditation", strategies[11].title)
        assertEquals("Manage Your Energy", strategies[12].title)
        assertEquals("Building Skills with Mind Maps", strategies[13].title)
        assertEquals("Deliberate Practice", strategies[14].title)
        assertEquals("Focus on Concepts", strategies[15].title)
    }

    @Test
    fun testAll8HabitsPresent() {
        val habits = DefaultStudyData.allHabits
        assertEquals(8, habits.size)

        val expectedTitles = listOf(
            "Find a good place to study",
            "Minimize distractions",
            "Determine best amount of study time",
            "Space out your studying",
            "Set study goals for each session",
            "Reward yourself",
            "Study with a group",
            "Take practice tests"
        )

        for (i in 0 until 8) {
            assertTrue("Habit ${habits[i].title} matches expectation", habits[i].title.contains(expectedTitles[i]))
            assertTrue("Habit must have checklist items", habits[i].checklistItems.isNotEmpty())
        }
    }

    @Test
    fun testPresetLearningPaths() {
        val paths = DefaultStudyData.allLearningPaths
        assertTrue("Must have at least 5 preset learning paths", paths.size >= 5)

        val examPath = paths.find { it.goal == StudyGoal.EXAM_PREP }
        assertNotNull(examPath)
        assertTrue(examPath!!.keyStrategyIds.contains(4)) // Test Yourself
        assertTrue(examPath.keyStrategyIds.contains(5)) // Spaced Repetition
    }
}
