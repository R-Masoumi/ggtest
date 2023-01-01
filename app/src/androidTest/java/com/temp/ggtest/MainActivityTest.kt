package com.temp.ggtest

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.temp.ggtest.util.Util.waitUntilExists
import com.temp.ggtest.util.hasIconId
import com.temp.ggtest.util.hasUrlString
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testHitList(){
        activityRule.waitUntilExists(hasUrlString("https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb.png"))
        activityRule.onNodeWithText("Parallel Lines in the Coordinate Plane: Quick Exploration").assertExists()
        activityRule.onNodeWithText("Geometry Apps with Whiteboards").assertExists()
        activityRule.onNodeWithText("3D Calculator with Whiteboards").assertExists()
        activityRule.onNodeWithText("Working in 3D").assertExists()
        activityRule.onNode(hasUrlString("https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb.png")).assertExists()
        activityRule.onNode(hasUrlString("https://www.geogebra.org/resource/bjgvfrdw/Ek4v4lrsVrmTNVIt/material-bjgvfrdw-thumb.png")).assertExists()
        activityRule.onNode(hasUrlString("https://www.geogebra.org/resource/sn5xg2ah/nED4cQ7epTTkC6MM/material-sn5xg2ah-thumb.png")).assertExists()
        activityRule.onNode(hasUrlString("https://www.geogebra.org/resource/s5sz5qtt/A6Ur0hx6VAitZPWs/material-s5sz5qtt-thumb.png")).assertExists()
        activityRule.onNode(hasUrlString("https://www.geogebra.org/resource/tw4qnw9v/8DOgzCWBbYTHlkeh/material-tw4qnw9v-thumb.png")).assertExists()
    }

    @Test
    fun testHit(){
        activityRule.waitUntilExists(hasUrlString("https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb.png"))
        activityRule.onNodeWithText("Parallel Lines in the Coordinate Plane: Quick Exploration").performClick()
        activityRule.waitUntilExists(hasUrlString("https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb.png"))
        activityRule.onNode(hasUrlString("https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb.png")).assertExists()
        activityRule.onNodeWithText("Parallel Lines in the Coordinate Plane: Quick Exploration").assertExists()
        activityRule.onNodeWithText("m9cvbqgp").assertExists()
        activityRule.onNodeWithText("ws").assertExists()
        activityRule.onNodeWithText("Mon, 27 4 2020 13:58:26").assertExists()
        activityRule.onNodeWithText("Thu, 14 5 2020 19:10:03").assertExists()
        activityRule.onNodeWithText("5743822").assertExists()
        activityRule.onNodeWithText("GeoGebra Team").assertExists()
    }

    @Test
    fun testNavigation() {
        activityRule.onNode(hasIconId("home")).assertExists()
        activityRule.waitUntilExists(hasUrlString("https://www.geogebra.org/resource/m9cvbqgp/jwu26zO0VaMqZUpt/material-m9cvbqgp-thumb.png"))
        activityRule.onNodeWithText("Parallel Lines in the Coordinate Plane: Quick Exploration").performClick()
        activityRule.onNode(hasIconId("back")).performClick()
        activityRule.onNode(hasIconId("home")).assertExists()
    }

}