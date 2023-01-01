package com.temp.ggtest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.temp.ggtest.data.DataSource
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
    private val dataSource: DataSource
) : ViewModel() {
    private val hitListFlow = RefreshFlow {
        dataSource.getHits()
    }
    val hitList = hitListFlow.data
    fun refresh() = hitListFlow.refresh()
}