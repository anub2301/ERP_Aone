package com.example.erp_aone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_aone.data.AppRepository
import com.example.erp_aone.data.UserPreferences
import com.example.erp_aone.data.entity.ItemEntity
import com.example.erp_aone.data.entity.SaleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val sales: StateFlow<List<SaleEntity>> = userPreferences.loggedInUserId
        .flatMapLatest { userId ->
            if (userId != null) repository.getSales(userId) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun makeSale(
        item: ItemEntity,
        soldTo: String,
        date: String,
        price: Double,
        count: Double
    ) {
        viewModelScope.launch {
            val userId = userPreferences.loggedInUserId.first() ?: return@launch
            if (count > item.currentCount) return@launch // Validation

            val sale = SaleEntity(
                userId = userId,
                itemId = item.id,
                itemName = item.shortName,
                soldTo = soldTo,
                sellingDate = date,
                sellingPrice = price,
                soldCount = count,
                purchasePriceAtSale = item.purchasePrice,
                purchaseDateAtSale = item.purchaseDate,
                stockAfter = item.currentCount - count
            )
            repository.insertSale(sale)
            repository.updateItem(item.copy(currentCount = item.currentCount - count))
        }
    }
}
