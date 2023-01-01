package com.temp.ggtest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.temp.ggtest.data.DataSource
import com.temp.ggtest.ui.misc.Navigation.Companion.NAV_ID
import com.temp.ggtest.util.RefreshFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for Hit page
 */
@HiltViewModel
class HitViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataSource : DataSource
): ViewModel() {
    private val id : String = requireNotNull(savedStateHandle[NAV_ID])
    private val hitFlow = RefreshFlow{
        dataSource.getHit(id)
    }
    val hit = hitFlow.data
    fun refresh() = hitFlow.refresh()
}