package com.example.erp_aone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erp_aone.data.AppRepository
import com.example.erp_aone.data.UserPreferences
import com.example.erp_aone.data.entity.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlySheetViewModel @Inject constructor(
    private val repository: AppRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyReports: StateFlow<List<DailyReport>> = userPreferences.loggedInUserId
        .flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(emptyList())
            
            combine(
                repository.getSales(userId),
                repository.getItems(userId),
                repository.getExpenses(userId),
                repository.getInvestments(userId)
            ) { sales, items, expenses, investments ->
                val allDates = (sales.map { it.sellingDate } + 
                               items.map { it.purchaseDate } + 
                               expenses.map { it.date } + 
                               investments.map { it.date }).distinct().sortedDescending()
                
                allDates.map { date ->
                    val daySales = sales.filter { it.sellingDate == date }
                    val dayPurchases = items.filter { it.purchaseDate == date }
                    val dayExpenses = expenses.filter { it.date == date }
                    val dayInvestments = investments.filter { it.date == date }
                    
                    val salesAmount = daySales.sumOf { it.sellingPrice * it.soldCount }
                    val purchaseAmount = dayPurchases.sumOf { it.purchasePrice * it.initialCount }
                    val costOfSoldItems = daySales.sumOf { it.purchasePriceAtSale * it.soldCount }
                    val expenseAmount = dayExpenses.sumOf { it.amount }
                    val investmentAmount = dayInvestments.sumOf { it.amount }
                    
                    DailyReport(
                        date = date,
                        sales = salesAmount,
                        purchase = purchaseAmount,
                        profit = salesAmount - costOfSoldItems - expenseAmount,
                        expense = expenseAmount,
                        investment = investmentAmount,
                        details = DayDetails(daySales, dayPurchases, dayExpenses, dayInvestments)
                    )
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addExpense(date: String, amount: Double, note: String) {
        viewModelScope.launch {
            val userId = userPreferences.loggedInUserId.first() ?: return@launch
            repository.insertExpense(ExpenseEntity(userId = userId, date = date, amount = amount, note = note))
        }
    }

    fun addInvestment(date: String, amount: Double, note: String) {
        viewModelScope.launch {
            val userId = userPreferences.loggedInUserId.first() ?: return@launch
            repository.insertInvestment(InvestmentEntity(userId = userId, date = date, amount = amount, note = note))
        }
    }
}

data class DailyReport(
    val date: String,
    val sales: Double,
    val purchase: Double,
    val profit: Double,
    val expense: Double,
    val investment: Double,
    val details: DayDetails
)

data class DayDetails(
    val sales: List<SaleEntity>,
    val purchases: List<ItemEntity>,
    val expenses: List<ExpenseEntity>,
    val investments: List<InvestmentEntity>
)
