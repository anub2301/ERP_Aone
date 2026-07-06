package com.example.erp_aone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.erp_aone.data.entity.ItemEntity
import com.example.erp_aone.viewmodel.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun InventoryScreen(viewModel: InventoryViewModel) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    var showForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Warning Banner
        val lowStockItems = items.filter { it.currentCount < 5 }
        if (lowStockItems.isNotEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${lowStockItems.size} items are low on stock!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        if (showForm) {
            ItemEntryForm(
                onSave = { name, price, date, from, count ->
                    viewModel.addItem(name, price, date, from, count)
                    showForm = false
                },
                onCancel = { showForm = false }
            )
        } else {
            Button(
                onClick = { showForm = true },
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add New Purchase")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                InventoryItemRow(item)
            }
        }
    }
}

@Composable
fun ItemEntryForm(
    onSave: (String, Double, String, String, Double) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-07-06") } // Placeholder for demo
    var from by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Purchase Entry", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Short Name") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Purchase Price") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (yyyy-MM-dd)") }, modifier = Modifier.weight(1f))
            }
            OutlinedTextField(value = from, onValueChange = { from = it }, label = { Text("Purchased From") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = count, onValueChange = { count = it }, label = { Text("Current Count") }, modifier = Modifier.fillMaxWidth())
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) { Text("Cancel") }
                Button(onClick = {
                    onSave(name, price.toDoubleOrNull() ?: 0.0, date, from, count.toDoubleOrNull() ?: 0.0)
                }) { Text("Save Item") }
            }
        }
    }
}

@Composable
fun InventoryItemRow(item: ItemEntity) {
    val stockValue = item.currentCount * item.purchasePrice
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(item.shortName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(currencyFormat.format(item.purchasePrice), color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Stock: ${item.currentCount}", style = MaterialTheme.typography.bodyMedium)
                Text("Value: ${currencyFormat.format(stockValue)}", fontWeight = FontWeight.SemiBold)
            }
            Text("From: ${item.purchasedFrom} | Date: ${item.purchaseDate}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (item.currentCount < 5) {
                    Badge(containerColor = MaterialTheme.colorScheme.error) { Text("Low Stock", color = Color.White, modifier = Modifier.padding(horizontal = 4.dp)) }
                }
                if (stockValue < 2000) {
                    Badge(containerColor = Color(0xFFE65100)) { Text("Low Value", color = Color.White, modifier = Modifier.padding(horizontal = 4.dp)) }
                }
            }
        }
    }
}
