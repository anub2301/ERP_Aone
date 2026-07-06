package com.example.erp_aone.data

import androidx.room.*
import com.example.erp_aone.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // User
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    // Item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Query("SELECT * FROM items WHERE userId = :userId")
    fun getItems(userId: Long): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE userId = :userId AND id = :itemId")
    suspend fun getItemById(userId: Long, itemId: Long): ItemEntity?

    @Query("SELECT * FROM items WHERE userId = :userId AND LOWER(shortName) = LOWER(:name)")
    suspend fun getItemByName(userId: Long, name: String): ItemEntity?

    // Sale
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity)

    @Query("SELECT * FROM sales WHERE userId = :userId ORDER BY sellingDate DESC, id DESC")
    fun getSales(userId: Long): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE userId = :userId AND sellingDate = :date")
    suspend fun getSalesByDate(userId: Long, date: String): List<SaleEntity>

    // Customer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE userId = :userId")
    fun getCustomers(userId: Long): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%')")
    fun searchCustomers(userId: Long, query: String): Flow<List<CustomerEntity>>

    // Expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    fun getExpenses(userId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date = :date")
    suspend fun getExpensesByDate(userId: Long, date: String): List<ExpenseEntity>

    // Investment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestment(investment: InvestmentEntity)

    @Query("SELECT * FROM investments WHERE userId = :userId")
    fun getInvestments(userId: Long): Flow<List<InvestmentEntity>>

    @Query("SELECT * FROM investments WHERE userId = :userId AND date = :date")
    suspend fun getInvestmentsByDate(userId: Long, date: String): List<InvestmentEntity>

    // Bill
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Query("SELECT * FROM bills WHERE userId = :userId ORDER BY date DESC, id DESC")
    fun getBills(userId: Long): Flow<List<BillEntity>>

    @Transaction
    @Query("SELECT * FROM bills WHERE userId = :userId AND id = :billId")
    suspend fun getBillWithLines(userId: Long, billId: Long): BillWithLines?

    // Bill Line
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillLines(lines: List<BillLineEntity>)

    // Reset Data
    @Query("DELETE FROM items WHERE userId = :userId")
    suspend fun deleteItems(userId: Long)

    @Query("DELETE FROM sales WHERE userId = :userId")
    suspend fun deleteSales(userId: Long)

    @Query("DELETE FROM customers WHERE userId = :userId")
    suspend fun deleteCustomers(userId: Long)

    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun deleteExpenses(userId: Long)

    @Query("DELETE FROM investments WHERE userId = :userId")
    suspend fun deleteInvestments(userId: Long)

    @Query("DELETE FROM bills WHERE userId = :userId")
    suspend fun deleteBills(userId: Long)
    
    @Transaction
    suspend fun resetUserData(userId: Long) {
        deleteItems(userId)
        deleteSales(userId)
        deleteCustomers(userId)
        deleteExpenses(userId)
        deleteInvestments(userId)
        deleteBills(userId)
    }
}
