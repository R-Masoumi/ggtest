package com.temp.ggtest.util

import androidx.compose.ui.test.SemanticsMatcher
import com.temp.ggtest.ui.misc.IconId
import com.temp.ggtest.ui.misc.UrlString

fun hasUrlString(url : String): SemanticsMatcher =
    SemanticsMatcher.expectValue(UrlString, url)

fun hasIconId(id : String): SemanticsMatcher =
    SemanticsMatcher.expectValue(IconId, id)