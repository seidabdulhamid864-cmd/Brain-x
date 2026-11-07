package com.example

import com.example.model.LearnerType
import com.example.model.StudyGoal
import com.example.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun testUserProfileDefaultsAndProgression() {
        val profile = UserProfile(
            userName = "Scholar",
            learnerType = LearnerType.CONCEPTUAL,
            primaryGoal = StudyGoal.EXAM_PREP,
            dailyMinutesTarget = 90,
            totalXp = 150,
            level = 2,
            currentStreak = 3
        )
        assertEquals("Scholar", profile.userName)
        assertEquals(90, profile.dailyMinutesTarget)
        assertEquals(2, profile.level)
        assertTrue(profile.currentStreak >= 1)
    }
}
