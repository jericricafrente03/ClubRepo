package com.android.clubserve.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.clubserve.data.remote.api.ClubServeApi
import com.android.clubserve.data.repository.HomeDomainState
import com.android.clubserve.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val api = ClubServeApi()
    private val repository = HomeRepository(api)

    private val _uiState = MutableStateFlow(HomeDomainState())
    val uiState: StateFlow<HomeDomainState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getHomeData()
            _uiState.value = result
            _isLoading.value = false
        }
    }
}
