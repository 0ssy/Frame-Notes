// One Compose UI test
package com.you.framenotes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.you.framenotes.ui.EmptyState
import com.you.framenotes.ui.theme.FrameNotesTheme
import org.junit.Rule
import org.junit.Test

class EmptyStateTest {
    @get:Rule val rule = createComposeRule()
    @Test fun emptyState_showsMessage() {
        rule.setContent { FrameNotesTheme { EmptyState("No photos match", "Try clearing filters.") } }
        rule.onNodeWithText("No photos match").assertIsDisplayed()
    }
}
