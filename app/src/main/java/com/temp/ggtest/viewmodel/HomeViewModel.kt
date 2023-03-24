package com.temp.ggtest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.temp.ggtest.data.DataSource
import com.temp.ggtest.ui.misc.Navigation.Hit.navigateToHit
import com.temp.ggtest.ui.misc.Navigator
import com.temp.ggtest.util.RefreshFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ViewModel for Home page
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataSource: DataSource,
    private val navigator: Navigator
) : ViewModel() {
    fun navigate(id : String) {
        navigator.controller?.navigateToHit(id)
    }
    private val hitListFlow = RefreshFlow {
        dataSource.getHits()
    }
    val hitList = hitListFlow.data
    fun refresh() = hitListFlow.refresh()
}