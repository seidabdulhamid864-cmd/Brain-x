package com.example.data

import com.example.model.*

object DefaultStudyData {

    val allStrategies = listOf(
        Strategy(
            id = 1,
            title = "Mnemonic Techniques",
            subtitle = "Supercharge retention using acronyms, acrostics, and peg memory palaces",
            category = StrategyCategory.MEMORY,
            whyItWorks = "The human brain evolved to remember spatial landscapes, vivid stories, and patterns far better than dry, isolated facts. Mnemonics attach foreign information onto existing mental landmarks.",
            actionableSteps = listOf(
                "Create Acronyms: Take the first letter of each item to form a memorable word (e.g., HOMES for Great Lakes).",
                "Craft Acrostics: Build a vivid sentence where each word begins with your target letters.",
                "Method of Loci (Memory Palace): Place bizarre, humorous visual items along a familiar physical path in your home."
            ),
            proTip = "The weirder, more exaggerated, and emotionally charged your mental image is, the harder it is for your brain to forget."
        ),
        Strategy(
            id = 2,
            title = "Take Handwritten Notes",
            subtitle = "Synthesize and rephrase ideas through muscular motor encoding",
            category = StrategyCategory.COMPREHENSION,
            whyItWorks = "Typing encourages mindless verbatim transcription. Handwriting forces your brain into selective cognitive filtering—you must distill, summarize, and map core concepts in real-time.",
            actionableSteps = listOf(
                "Use the Cornell System: Divide page into Cues (left), Notes (right), and 3-sentence Summary (bottom).",
                "Never write full sentences: Use abbreviations, arrows, and diagrammatic relationships.",
                "Color-code definitions vs formulas vs questions with a 3-pen system."
            ),
            proTip = "Review your cues column 24 hours later by covering the notes section and trying to explain each cue from memory."
        ),
        Strategy(
            id = 3,
            title = "Peer Learning",
            subtitle = "Teach, debate, and co-solve problems to uncover cognitive blind spots",
            category = StrategyCategory.PRACTICE,
            whyItWorks = "When preparing to teach someone else, your mind organizes information more hierarchically. Social accountability also dramatically boosts dopamine and persistence.",
            actionableSteps = listOf(
                "Form reciprocal pairs: Assign each member a different sub-topic to master and teach.",
                "Hold Devil's Advocate sessions: Intentionally challenge each other's assumptions and derivations.",
                "Exchange active recall quizzes: Write 5 challenging scenario questions for your study partner."
            ),
            proTip = "If your explanation confuses your partner, you've located a real gap in your mental model. Re-read the source immediately."
        ),
        Strategy(
            id = 4,
            title = "Test Yourself",
            subtitle = "Active retrieval practice beats re-reading notes by over 300%",
            category = StrategyCategory.PRACTICE,
            whyItWorks = "Retrieving an answer from memory strengthens neural synaptic pathways. Passive re-reading creates an illusion of competence without building neural retrieval routes.",
            actionableSteps = listOf(
                "Close the book and do a 'Brain Dump' on blank paper.",
                "Turn headings into test questions before reading any chapter.",
                "Generate flashcards focused on 'Why' and 'How', not just trivia."
            ),
            proTip = "Don't flip flashcards too quickly. Force your mind into a 5-second cognitive struggle before checking the answer."
        ),
        Strategy(
            id = 5,
            title = "Spaced Repetition",
            subtitle = "Conquer the Ebbinghaus forgetting curve with optimized review intervals",
            category = StrategyCategory.MEMORY,
            whyItWorks = "Forgetting is natural. Each time you review material just before you are about to forget it, the slope of the forgetting curve flattens, cementing it into long-term memory.",
            actionableSteps = listOf(
                "Review Interval 1: 24 hours after initial learning session.",
                "Review Interval 2: Day 3 (72 hours).",
                "Review Interval 3: Day 7 (1 week).",
                "Review Interval 4: Day 14 (2 weeks), then Day 30."
            ),
            proTip = "Use the Leitner 5-box system: Items you get right graduate to wider review intervals; missed items reset to Box 1."
        ),
        Strategy(
            id = 6,
            title = "Take Short Breaks",
            subtitle = "Prevent cognitive fatigue and stimulate subconscious diffuse processing",
            category = StrategyCategory.FOCUS_WELLNESS,
            whyItWorks = "The brain has two modes: Focused mode and Diffuse mode. Taking a 5-minute break lets your subconscious mind synthesize disparate concepts into creative breakthroughs.",
            actionableSteps = listOf(
                "Apply the Pomodoro cadence: 25 minutes pure uninterrupted focus, 5 minutes physical break.",
                "Step away from all screens during breaks—look into the distance at green foliage or natural light.",
                "Every 4 focus blocks, take an extended 20-30 minute restorative walking break."
            ),
            proTip = "Physical movement during breaks clears metabolic waste products from brain cells and resets focus neurotransmitters."
        ),
        Strategy(
            id = 7,
            title = "Exercise, Sleep & Eat Healthy",
            subtitle = "Optimize your biological neurochemistry for synaptic plasticity",
            category = StrategyCategory.FOCUS_WELLNESS,
            whyItWorks = "Memory consolidation occurs during slow-wave and REM sleep via the hippocampus. Cardiovascular exercise releases BDNF (Brain-Derived Neurotrophic Factor), stimulating neurogenesis.",
            actionableSteps = listOf(
                "Target 7.5 to 8.5 hours of consistent sleep—especially before and after difficult study sessions.",
                "Engage in 20 minutes of moderate aerobic exercise (brisk walk, jog) to elevate alertness.",
                "Hydrate with 500ml water prior to study; fuel brain with low-glycemic fats, berries, and clean proteins."
            ),
            proTip = "Pulling an all-nighter reduces cognitive working memory capacity by up to 40% and destroys memory consolidation."
        ),
        Strategy(
            id = 8,
            title = "Say Out Loud",
            subtitle = "Harness the production effect and auditory feedback loop",
            category = StrategyCategory.PRACTICE,
            whyItWorks = "Speaking words aloud requires dual-motor production and simultaneous auditory self-monitoring. This multi-sensory encoding makes ideas significantly more memorable.",
            actionableSteps = listOf(
                "Read complex definitions aloud with inflection, rhythm, and passion.",
                "The Feynman Voice Test: Explain the concept out loud as if teaching a bright 10-year-old.",
                "Voice-record your 60-second summary and listen to spot hesitations or vague hand-waving."
            ),
            proTip = "If you stumble or use jargon you cannot immediately define, stop and clarify the vocabulary term immediately."
        ),
        Strategy(
            id = 9,
            title = "Mindful Learning",
            subtitle = "Cultivate deep single-task flow and eliminate digital attention fragmentation",
            category = StrategyCategory.FOCUS_WELLNESS,
            whyItWorks = "Context switching leaves an 'attention residue' that degrades IQ by 10-15 points. Mindful awareness anchors your attention strictly onto the present problem.",
            actionableSteps = listOf(
                "Set a clear intention: 'For the next 30 minutes, I am solely solving these 4 physics proofs.'",
                "Execute an 'Environment Sweep': Clear physical clutter and hide mobile devices out of line-of-sight.",
                "When your mind wanders, gently notice the distraction without self-judgment and return to the text."
            ),
            proTip = "Keep a 'Distraction Scratchpad' beside your desk. If random errands pop into your mind, write them down to clear mental RAM."
        ),
        Strategy(
            id = 10,
            title = "Understand the Study Cycle",
            subtitle = "Follow the proven 5-stage university framework for academic mastery",
            category = StrategyCategory.COMPREHENSION,
            whyItWorks = "Developed by Frank Christ at LSU, the Study Cycle transforms disjointed studying into a systemic high-impact cycle with compound learning efficiency.",
            actionableSteps = listOf(
                "Stage 1: PREVIEW (5-15 min) skim chapter headers, diagrams, and summaries before lecture.",
                "Stage 2: ATTEND engage actively in lectures, asking questions and capturing cues.",
                "Stage 3: REVIEW (5-10 min) skim notes immediately after class to fill in gaps.",
                "Stage 4: INTENSE STUDY (30-50 min sessions) active problem-solving and self-testing.",
                "Stage 5: CHECK self-assess mastery: 'Can I teach this without looking?'"
            ),
            proTip = "Even a 5-minute pre-reading preview primes your neural schema so new lecture points immediately find anchor hooks."
        ),
        Strategy(
            id = 11,
            title = "Active Learning",
            subtitle = "Shift from passive spectator to active knowledge architect",
            category = StrategyCategory.COMPREHENSION,
            whyItWorks = "Learning is an active constructive process. Transforming text into original summaries, answering probing 'why' questions, and solving applied problems activates deep neural networks.",
            actionableSteps = listOf(
                "Ask 3 probing questions per page: 'What causes this? How does it relate to X? What if condition Y changed?'",
                "Write a 1-sentence synthesis of every major chapter section before turning the page.",
                "Apply the knowledge immediately to a concrete, real-world case study or numeric problem."
            ),
            proTip = "If your study session feels easy and effortless, you are likely reading passively. Productive struggle is the feeling of neuroplasticity."
        ),
        Strategy(
            id = 12,
            title = "Calm Your Mind with Meditation",
            subtitle = "Downregulate cortisol and boost prefrontal cortex working memory",
            category = StrategyCategory.FOCUS_WELLNESS,
            whyItWorks = "Exam anxiety and mental agitation flood the brain with cortisol, shutting down hippocampus access. 3 minutes of rhythmic breathing restores prefrontal executive function.",
            actionableSteps = listOf(
                "Perform 4-4-4-4 Box Breathing: Inhale for 4s, Hold for 4s, Exhale for 4s, Hold empty for 4s.",
                "Do a 60-second physical body scan: Release tension in your jaw, shoulders, and brow.",
                "Use the 'Observer Stance': Acknowledge feelings of imposter syndrome as passing clouds."
            ),
            proTip = "Practice 3 minutes of Box Breathing immediately before entering an examination room to trigger parasympathetic composure."
        ),
        Strategy(
            id = 13,
            title = "Manage Your Energy",
            subtitle = "Align rigorous cognitive tasks with your biological ultradian rhythms",
            category = StrategyCategory.FOCUS_WELLNESS,
            whyItWorks = "Time is finite, but cognitive energy is flexible. Forcing heavy analytical proofs during an afternoon circadian dip leads to frustration and double the errors.",
            actionableSteps = listOf(
                "Identify your peak biological prime time: usually 2-4 hours after waking or early evening.",
                "Reserve peak hours for new, complex conceptual synthesis and math/logic proofs.",
                "Use low-energy slump periods for passive admin: organizing flashcard decks, scheduling, light reading."
            ),
            proTip = "Respect 90-minute ultradian cycles. After 90 minutes of continuous high-intensity focus, your alertness naturally dips."
        ),
        Strategy(
            id = 14,
            title = "Building Skills with Mind Maps",
            subtitle = "Structure knowledge spatially with radial branching and associative links",
            category = StrategyCategory.COMPREHENSION,
            whyItWorks = "Linear notes conceal connections between chapters. Mind maps replicate the non-linear semantic web of human memory, clarifying hierarchies and cross-domain links.",
            actionableSteps = listOf(
                "Start with the core theme or equation at the exact center of a blank landscape canvas.",
                "Branch out 4-6 primary categories using thick colored radiating trunks.",
                "Add secondary sub-nodes with single keywords, symbols, or small doodles.",
                "Draw cross-linking dashed arrows between distant branches to reveal non-obvious relationships."
            ),
            proTip = "Limit node text to 1-3 keywords. Sentences clutter mind maps and prevent high-level spatial pattern recognition."
        ),
        Strategy(
            id = 15,
            title = "Deliberate Practice",
            subtitle = "Isolate weak sub-skills and train just beyond your current ability edge",
            category = StrategyCategory.PRACTICE,
            whyItWorks = "Mindlessly repeating what you're already good at gives false security. Deliberate practice requires micro-isolating errors, receiving immediate feedback, and repetitive correction.",
            actionableSteps = listOf(
                "Diagnose your bottleneck: Identify the exact problem type or derivation step where you hesitate.",
                "Create high-frequency micro-drills: Solve 5 variations of that single step in rapid succession.",
                "Immediate error analysis: Inspect every mistake—categorize as conceptual gap, calculation slip, or misread."
            ),
            proTip = "Maintain a 'Mistake Ledger'. Re-test yourself on every logged mistake 48 hours later until you solve it effortlessly."
        ),
        Strategy(
            id = 16,
            title = "Focus on Concepts",
            subtitle = "First-principles deconstruction over shallow formula memorization",
            category = StrategyCategory.COMPREHENSION,
            whyItWorks = "Formulas change across questions; fundamental principles never do. When you master core axioms, you can derive forgotten formulas under exam pressure.",
            actionableSteps = listOf(
                "Deconstruct to First Principles: Ask 'What are the foundational truths that make this theorem inevitable?'",
                "Analogize: Map abstract concepts to physical intuition (e.g., electricity voltage as water pressure).",
                "Extreme boundary testing: Ask 'What happens if variable X goes to zero or infinity?'"
            ),
            proTip = "Never memorize an equation until you understand what each symbol physically represents and how they interact."
        )
    )

    val allHabits = listOf(
        StudyHabit(
            id = 1,
            title = "Find a good place to study",
            subtitle = "Designate an ergonomic sanctuary with zero cognitive cross-contamination",
            actionGuide = "Never study in bed or where you play video games. Your brain builds environmental associations. A dedicated study zone primes your focus state automatically upon sitting.",
            checklistItems = listOf(
                "Ergonomic desk & upright chair setup",
                "Bright, natural or 5000K daylight illumination",
                "Cool room temperature (68-70°F / 20-21°C)",
                "All materials, pens, and notebook ready before sitting down"
            )
        ),
        StudyHabit(
            id = 2,
            title = "Minimize distractions",
            subtitle = "Erect physical & digital barriers before starting your focus clock",
            actionGuide = "Willpower is a depleting muscle. Do not rely on resisting phone notifications—physically remove the temptation from your field of vision entirely.",
            checklistItems = listOf(
                "Phone in Do-Not-Disturb and placed in another room/bag",
                "Close all unrelated browser tabs and messaging apps",
                "Use noise-canceling headphones or brown noise",
                "Inform family/roommates that you are in a 45-minute deep focus block"
            )
        ),
        StudyHabit(
            id = 3,
            title = "Determine best amount of study time",
            subtitle = "Calculate your personal cognitive sweet spot and protect it",
            actionGuide = "Studying for 6 marathon hours results in diminishing returns. Most human brains sustain peak focus for 25 to 50 minutes. Experiment to find your personal sweet spot.",
            checklistItems = listOf(
                "Test 25-minute Pomodoro vs 50-minute deep session",
                "Track when your eyes first glaze over or attention dips",
                "Set an audible timer so you don't keep checking the clock",
                "Stop at your target time even if you feel you could push further"
            )
        ),
        StudyHabit(
            id = 4,
            title = "Space out your studying",
            subtitle = "Replace emergency panic-cramming with daily incremental mastery",
            actionGuide = "Cramming stores facts in fragile short-term working memory. Distributing 5 hours of study across 5 days produces 200% higher retention than 5 hours on Sunday night.",
            checklistItems = listOf(
                "Schedule recurring calendar study slots at fixed hours",
                "Review yesterday's notes for 5 minutes before starting new topics",
                "Plan 3 spaced touchpoints for every major exam topic",
                "Break large syllabi into bite-sized daily modules"
            )
        ),
        StudyHabit(
            id = 5,
            title = "Set study goals for each session",
            subtitle = "Define clear, measurable deliverables before opening the book",
            actionGuide = "Saying 'I am going to study biology' invites aimless scrolling. State an explicit, verifiable objective: 'I will solve 12 genetics punnett squares and write 3 summary flashcards.'",
            checklistItems = listOf(
                "Write 1 specific primary goal at top of paper",
                "Define the verifiable proof of completion",
                "Estimate minutes required for each micro-task",
                "Check off items as completed to generate momentum"
            )
        ),
        StudyHabit(
            id = 6,
            title = "Reward yourself",
            subtitle = "Reinforce the dopamine habit loop with post-study celebrations",
            actionGuide = "Charles Duhigg's habit loop consists of Cue -> Routine -> Reward. If studying only feels like suffering, your brain resists it. A healthy post-session treat solidifies the habit.",
            checklistItems = listOf(
                "Designate reward beforehand: favorite snack, walk, coffee, gaming episode",
                "Strict rule: Reward is unlocked ONLY when the session goal is completed",
                "Celebrate small wins and acknowledge personal discipline",
                "End the study day with complete mental detachment"
            )
        ),
        StudyHabit(
            id = 7,
            title = "Study with a group",
            subtitle = "Amplify accountability, debate complex models, and spot blind spots",
            actionGuide = "The right study group multiplies learning speed. Keep groups small (2 to 4 people) with clear ground rules: zero social chit-chat until the break timer rings.",
            checklistItems = listOf(
                "Cap study group at 2-4 motivated peers",
                "Assign specific agenda topics to each person in advance",
                "Spend first 30 minutes in silent co-working, then 20 minutes peer teaching",
                "Quiz each other on difficult edge cases"
            )
        ),
        StudyHabit(
            id = 8,
            title = "Take practice tests",
            subtitle = "Simulate real exam conditions: timing, pressure, and closed notes",
            actionGuide = "The ultimate diagnostic tool. Practice tests condition your nervous system against exam anxiety, train your internal clock, and pinpoint exact revision targets.",
            checklistItems = listOf(
                "Strict closed-book conditions: no checking answer keys midway",
                "Set a countdown timer equal to real test duration",
                "Simulate physical conditions: desk, paper, calculator only",
                "Post-test autopsy: Thoroughly diagnose every incorrect answer"
            )
        )
    )

    val allLearningPaths = listOf(
        LearningPath(
            id = "exam_prep",
            title = "High-Retention Exam Accelerator",
            description = "Optimized for students preparing for finals, standardized tests, and high-stakes exams. Heavy emphasis on retrieval practice, spaced repetition, and exam simulations.",
            goal = StudyGoal.EXAM_PREP,
            dailyTargetMinutes = 90,
            keyStrategyIds = listOf(4, 5, 10, 16), // Test Yourself, Spaced Repetition, Study Cycle, Focus on Concepts
            coreHabitIds = listOf(4, 5, 8), // Space out studying, Set goals, Take practice tests
            morningRoutine = "10-min Spaced Repetition Flashcard queue review while brain is fresh.",
            studySessionRoutine = "50-min Active Recall & Problem-solving blocks with 10-min screen-free breaks.",
            eveningReviewRoutine = "15-min Practice Test error analysis & Cornell cue review."
        ),
        LearningPath(
            id = "stem_deep",
            title = "STEM & Conceptual Deep Dive",
            description = "Designed for engineering, mathematics, physics, and computer science. Focuses on first principles, mind maps, deliberate practice on difficult proofs, and active questioning.",
            goal = StudyGoal.STEM_DEEP,
            dailyTargetMinutes = 120,
            keyStrategyIds = listOf(16, 14, 15, 11), // Focus on Concepts, Mind Maps, Deliberate Practice, Active Learning
            coreHabitIds = listOf(1, 2, 3), // Study Sanctuary, Minimize distractions, Best study time
            morningRoutine = "First-principles conceptual breakdown of hardest theorems during peak circadian focus.",
            studySessionRoutine = "90-minute deep problem-set sprints with deliberate practice error ledgers.",
            eveningReviewRoutine = "Construct radial Mind Map connecting formulas to physical intuition."
        ),
        LearningPath(
            id = "language_mem",
            title = "Language & Memory Mastery",
            description = "Accelerate language vocabulary acquisition, medical terminology, and historical dates through mnemonics, verbal vocalization, and Leitner box scheduling.",
            goal = StudyGoal.LANGUAGE_MEM,
            dailyTargetMinutes = 60,
            keyStrategyIds = listOf(1, 8, 5, 4), // Mnemonic, Say Out Loud, Spaced Repetition, Test Yourself
            coreHabitIds = listOf(3, 4, 6), // Best study time, Space out studying, Reward yourself
            morningRoutine = "Say-out-loud vocal drills with 30 target vocabulary terms.",
            studySessionRoutine = "Construct vivid Acronyms and Memory Palace pegs for complex grammatical rules.",
            eveningReviewRoutine = "Quick 10-minute Leitner box spaced repetition session."
        ),
        LearningPath(
            id = "rapid_skills",
            title = "Rapid Skill Acquisition Blitz",
            description = "Master new software tools, instruments, or practical professional skills in weeks using intense deliberate micro-drills, peer feedback, and active synthesis.",
            goal = StudyGoal.SKILL_BLITZ,
            dailyTargetMinutes = 75,
            keyStrategyIds = listOf(15, 8, 3, 11), // Deliberate Practice, Say Out Loud, Peer Learning, Active Learning
            coreHabitIds = listOf(2, 5, 7), // Minimize distractions, Set goals, Study with group
            morningRoutine = "Identify the single weakest sub-skill and run 15 rapid deliberate drill repetitions.",
            studySessionRoutine = "Record 60-second Feynman voice summaries and seek immediate peer critique.",
            eveningReviewRoutine = "Log micro-adjustments and set target benchmark for tomorrow."
        ),
        LearningPath(
            id = "mindful_foundations",
            title = "Mindful & Sustainable Habit Builder",
            description = "Overcome procrastination, study anxiety, and burnout. Emphasizes circadian energy management, calming breathwork, and rewarding sustainable study streaks.",
            goal = StudyGoal.BALANCED_HABITS,
            dailyTargetMinutes = 45,
            keyStrategyIds = listOf(12, 13, 9, 6), // Meditation, Energy Management, Mindful Learning, Take Breaks
            coreHabitIds = listOf(1, 2, 6), // Good place, Minimize distraction, Reward yourself
            morningRoutine = "3-minute Box Breathing calm session and setting one realistic primary daily goal.",
            studySessionRoutine = "Two 20-minute mindful focus sprints with gentle physical stretch breaks.",
            eveningReviewRoutine = "Unlock personal reward and track sleep/hydration readiness score."
        )
    )

    val initialFlashcards = listOf(
        FlashcardItem(
            id = "fc_1",
            question = "What is the Ebbinghaus Forgetting Curve?",
            answer = "The mathematical decline of memory retention over time when no attempt is made to retain it. Reviewing at spaced intervals resets and flattens the curve.",
            category = "Cognitive Science",
            box = 2,
            nextReviewDayOffset = 3
        ),
        FlashcardItem(
            id = "fc_2",
            question = "Explain the Feynman Technique in 4 steps.",
            answer = "1. Choose a concept. 2. Teach it out loud to a 10-year-old using simple words. 3. Identify gaps where you rely on jargon. 4. Return to source text and simplify.",
            category = "Learning Strategies",
            box = 3,
            nextReviewDayOffset = 7
        ),
        FlashcardItem(
            id = "fc_3",
            question = "What is the difference between Active Recall and Passive Review?",
            answer = "Passive review (re-reading) produces recognition and false fluency. Active recall forces neural circuits to reconstruct the concept without prompts, strengthening synapses.",
            category = "Study Habits",
            box = 1,
            nextReviewDayOffset = 1
        ),
        FlashcardItem(
            id = "fc_4",
            question = "What are the 5 phases of Frank Christ's Study Cycle?",
            answer = "1. Preview (skim before class). 2. Attend (active lecture). 3. Review (5 min right after). 4. Study (intense 30-50m focus). 5. Check (assess mastery).",
            category = "Study Cycle",
            box = 1,
            nextReviewDayOffset = 1
        )
    )

    val initialNotes = listOf(
        CornellNoteItem(
            id = "cn_1",
            title = "Spaced Repetition & Cognitive Plasticity",
            cues = "• Forgetting Curve\n• Optimal Intervals\n• Leitner System",
            notes = "Spaced repetition leverages the spacing effect: information is better retained when exposure is spaced over time rather than concentrated.\n\nKey schedule: Day 1, Day 3, Day 7, Day 14, Day 30.\n\nLeitner box algorithm moves cards to higher boxes upon correct recall, widening the review window automatically.",
            summary = "Spaced repetition combats the natural forgetting curve by strategically testing memory right before decay, turning short-term memories into permanent neural architecture."
        )
    )

    val initialMindMap = listOf(
        MindMapNode(id = "root", text = "Smart Learning Mastery", parentId = null, colorIndex = 0),
        MindMapNode(id = "c1", text = "Memory & Recall", parentId = "root", colorIndex = 1),
        MindMapNode(id = "c2", text = "Deep Comprehension", parentId = "root", colorIndex = 2),
        MindMapNode(id = "c3", text = "Biological Energy", parentId = "root", colorIndex = 3),
        MindMapNode(id = "c4", text = "Active Practice", parentId = "root", colorIndex = 4),
        MindMapNode(id = "s1", text = "Mnemonics & Loci", parentId = "c1", colorIndex = 1),
        MindMapNode(id = "s2", text = "Spaced Repetition", parentId = "c1", colorIndex = 1),
        MindMapNode(id = "s3", text = "Study Cycle", parentId = "c2", colorIndex = 2),
        MindMapNode(id = "s4", text = "Handwritten Cornell", parentId = "c2", colorIndex = 2),
        MindMapNode(id = "s5", text = "Circadian Rhythms", parentId = "c3", colorIndex = 3),
        MindMapNode(id = "s6", text = "Box Breathing", parentId = "c3", colorIndex = 3),
        MindMapNode(id = "s7", text = "Deliberate Drills", parentId = "c4", colorIndex = 4),
        MindMapNode(id = "s8", text = "Feynman Out Loud", parentId = "c4", colorIndex = 4)
    )

    val initialSessionLogs = listOf(
        StudySessionLog(id = "log_1", title = "Active Recall Sprint", durationMinutes = 25, strategyId = 4),
        StudySessionLog(id = "log_2", title = "Cornell Notes & Synthesis", durationMinutes = 30, strategyId = 2),
        StudySessionLog(id = "log_3", title = "Mindful Focus Block", durationMinutes = 20, strategyId = 9)
    )
}
