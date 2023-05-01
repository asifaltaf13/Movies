package com.asifaltaf.movies.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.asifaltaf.movies.ui.util.Tags
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAppBarSearchButton_clicked_expectedSearchBarVisible() {
        composeTestRule.onNodeWithTag(Tags.AppBarSearch).performClick()
        composeTestRule.onNodeWithTag(Tags.SearchAppBarText).assertIsDisplayed()
    }

    @Test
    fun testAppBarDeleteButton_clicked_expectedAlertDialogVisible() {
        composeTestRule.onNodeWithTag(Tags.AppBarDelete).performClick()
        composeTestRule.onNodeWithTag(Tags.AlertDialog).assertIsDisplayed()
    }

}