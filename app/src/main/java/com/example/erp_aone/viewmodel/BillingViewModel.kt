package com.example.erp_aone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_aone.data.AppRepository
import com.example.erp_aone.data.UserPreferences
import com.example.erp_aone.data.entity.BillEntity
import com.example.erp_aone.data.entity.BillLineEntity
import com.example.erp_aone.data.entity.BillWithLines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val bills: StateFlow<List<BillEntity>> = userPreferences.loggedInUserId
        .flatMapLatest { userId ->
            if (userId != null) repository.getBills(userId) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentBillLines = MutableStateFlow<List<BillLineEntity>>(emptyList())
    val currentBillLines: StateFlow<List<BillLineEntity>> = _currentBillLines

    fun addBillLine(item: String, qty: Double, price: Double, discount: Double) {
        val lines = _currentBillLines.value.toMutableList()
        lines.add(BillLineEntity(billId = 0, itemName = item, qty = qty, price = price, discount = discount))
        _currentBillLines.value = lines
    }

    fun removeBillLine(index: Int) {
        val lines = _currentBillLines.value.toMutableList()
        if (index in lines.indices) {
            lines.removeAt(index)
            _currentBillLines.value = lines
        }
    }

    fun generateBill(
        customerName: String,
        mobile: String,
        address: String,
        date: String,
        reduceStock: Boolean,
        bankDetails: BankDetails
    ) {
        viewModelScope.launch {
            val userId = userPreferences.loggedInUserId.first() ?: return@launch
            val lines = _currentBillLines.value
            
            val taxableAmount = lines.sumOf { (it.price * it.qty) - it.discount }
            val sgst = taxableAmount * 0.09
            val cgst = taxableAmount * 0.09
            val totalTax = sgst + cgst
            val grandTotal = taxableAmount + totalTax
            val totalDiscount = lines.sumOf { it.discount }

            val bill = BillEntity(
                userId = userId,
                customerName = customerName,
                mobile = mobile,
                address = address,
                date = date,
                totalDiscount = totalDiscount,
                taxableAmount = taxableAmount,
                sgst = sgst,
                cgst = cgst,
                totalTax = totalTax,
                grandTotal = grandTotal,
                terms = "Goods once sold will not be taken back.",
                bankHolder = bankDetails.holder,
                bankName = bankDetails.name,
                bankAccount = bankDetails.account,
                bankIfsc = bankDetails.ifsc
            )

            val billId = repository.insertBill(bill, lines)
            
            if (reduceStock) {
                lines.forEach { line ->
                    val item = repository.getItemByName(userId, line.itemName)
                    if (item != null) {
                        repository.updateItem(item.copy(currentCount = item.currentCount - line.qty))
                    }
                }
            }
            
            _currentBillLines.value = emptyList()
        }
    }
}

data class BankDetails(
    val holder: String,
    val name: String,
    val account: String,
    val ifsc: String
)
