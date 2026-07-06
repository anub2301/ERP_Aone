package com.example.erp_aone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_aone.data.AppRepository
import com.example.erp_aone.data.UserPreferences
import com.example.erp_aone.data.entity.CustomerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val customers: StateFlow<List<CustomerEntity>> = combine(
        userPreferences.loggedInUserId,
        _searchQuery
    ) { userId, query -> userId to query }
        .flatMapLatest { (userId, query) ->
            if (userId == null) flowOf(emptyList())
            else if (query.isEmpty()) repository.getCustomers(userId)
            else repository.searchCustomers(userId, query)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCustomer(name: String, phone: String, address: String, gst: String) {
        viewModelScope.launch {
            val userId = userPreferences.loggedInUserId.first() ?: return@launch
            repository.insertCustomer(CustomerEntity(userId = userId, name = name, phone = phone, address = address, gstNumber = gst))
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
