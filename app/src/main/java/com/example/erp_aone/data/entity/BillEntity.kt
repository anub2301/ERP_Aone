package com.example.erp_aone.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity(
    tableName = "bills",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val customerName: String,
    val mobile: String,
    val address: String,
    val date: String,
    val totalDiscount: Double,
    val taxableAmount: Double,
    val sgst: Double,
    val cgst: Double,
    val totalTax: Double,
    val grandTotal: Double,
    val terms: String,
    val bankHolder: String,
    val bankName: String,
    val bankAccount: String,
    val bankIfsc: String
)

data class BillWithLines(
    @Embedded val bill: BillEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "billId"
    )
    val lines: List<BillLineEntity>
)
