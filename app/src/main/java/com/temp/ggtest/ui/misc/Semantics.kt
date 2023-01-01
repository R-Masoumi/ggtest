package com.temp.ggtest.ui.misc

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

/**
 * UrlString semantics that indicates item is tied to a UrlString. used in testing
 */
val UrlString = SemanticsPropertyKey<String?>("UrlString")
var SemanticsPropertyReceiver.urlId by UrlString

/**
 * IconId semantics that indicates icon is tied to an id. used in testing
 */
val IconId = SemanticsPropertyKey<String>("IconId")
var SemanticsPropertyReceiver.iconId by IconId