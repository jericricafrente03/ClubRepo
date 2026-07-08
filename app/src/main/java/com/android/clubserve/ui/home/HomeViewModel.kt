package com.android.clubserve.ui.home

import android.util.Log
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
    private val api = ClubServeApi.create()
    private val repository = HomeRepository(api)

    private val _uiState = MutableStateFlow(HomeDomainState(isLoading = true))
    val uiState: StateFlow<HomeDomainState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            repository.getHomeData().collect { state ->
                state.subCategories
                    .filter { it.status == "ACTIVE" }
                    .distinctBy { it.mainCategoryName }
                    .forEach { data ->
                        Log.v("meme", "lovedByLocals ${data.mainCategoryName}")
                    }
                _uiState.value = state
            }
        }
    }
}
