package com.example.erp_aone.data

import com.example.erp_aone.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val appDao: AppDao
) {
    // User
    suspend fun insertUser(user: UserEntity) = appDao.insertUser(user)
    suspend fun getUserByUsername(username: String) = appDao.getUserByUsername(username)
    suspend fun getUserById(userId: Long) = appDao.getUserById(userId)

    // Item
    suspend fun insertItem(item: ItemEntity) = appDao.insertItem(item)
    suspend fun updateItem(item: ItemEntity) = appDao.updateItem(item)
    fun getItems(userId: Long): Flow<List<ItemEntity>> = appDao.getItems(userId)
    suspend fun getItemById(userId: Long, itemId: Long) = appDao.getItemById(userId, itemId)
    suspend fun getItemByName(userId: Long, name: String) = appDao.getItemByName(userId, name)

    // Sale
    suspend fun insertSale(sale: SaleEntity) = appDao.insertSale(sale)
    fun getSales(userId: Long): Flow<List<SaleEntity>> = appDao.getSales(userId)
    suspend fun getSalesByDate(userId: Long, date: String) = appDao.getSalesByDate(userId, date)

    // Customer
    suspend fun insertCustomer(customer: CustomerEntity) = appDao.insertCustomer(customer)
    fun getCustomers(userId: Long): Flow<List<CustomerEntity>> = appDao.getCustomers(userId)
    fun searchCustomers(userId: Long, query: String) = appDao.searchCustomers(userId, query)

    // Expense
    suspend fun insertExpense(expense: ExpenseEntity) = appDao.insertExpense(expense)
    fun getExpenses(userId: Long): Flow<List<ExpenseEntity>> = appDao.getExpenses(userId)
    suspend fun getExpensesByDate(userId: Long, date: String) = appDao.getExpensesByDate(userId, date)

    // Investment
    suspend fun insertInvestment(investment: InvestmentEntity) = appDao.insertInvestment(investment)
    fun getInvestments(userId: Long): Flow<List<InvestmentEntity>> = appDao.getInvestments(userId)
    suspend fun getInvestmentsByDate(userId: Long, date: String) = appDao.getInvestmentsByDate(userId, date)

    // Bill
    suspend fun insertBill(bill: BillEntity, lines: List<BillLineEntity>): Long {
        val billId = appDao.insertBill(bill)
        val billLines = lines.map { it.copy(billId = billId) }
        appDao.insertBillLines(billLines)
        return billId
    }
    fun getBills(userId: Long): Flow<List<BillEntity>> = appDao.getBills(userId)
    suspend fun getBillWithLines(userId: Long, billId: Long) = appDao.getBillWithLines(userId, billId)

    // Reset
    suspend fun resetUserData(userId: Long) = appDao.resetUserData(userId)
}
