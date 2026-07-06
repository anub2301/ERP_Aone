package com.example.erp_aone.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.erp_aone.data.entity.*

@Database(
    entities = [
        UserEntity::class,
        ItemEntity::class,
        SaleEntity::class,
        CustomerEntity::class,
        ExpenseEntity::class,
        InvestmentEntity::class,
        BillEntity::class,
        BillLineEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        const val DATABASE_NAME = "erp_aone_db"
    }
}
