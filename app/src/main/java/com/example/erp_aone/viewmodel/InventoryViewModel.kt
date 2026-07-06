package com.example.erp_aone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_aone.data.AppRepository
import com.example.erp_aone.data.UserPreferences
import com.example.erp_aone.data.entity.ItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: StateFlow<List<ItemEntity>> = userPreferences.loggedInUserId
        .flatMapLatest { userId ->
            if (userId != null) repository.getItems(userId) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addItem(
        shortName: String,
        purchasePrice: Double,
        purchaseDate: String,
        purchasedFrom: String,
        currentCount: Double
    ) {
        viewModelScope.launch {
            val userId = userPreferences.loggedInUserId.first() ?: return@launch
            val item = ItemEntity(
                userId = userId,
                shortName = shortName,
                purchasePrice = purchasePrice,
                purchaseDate = purchaseDate,
                purchasedFrom = purchasedFrom,
                initialCount = currentCount,
                currentCount = currentCount
            )
            repository.insertItem(item)
        }
    }
}
