package com.example.erp_aone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.erp_aone.data.entity.ItemEntity
import com.example.erp_aone.data.entity.SaleEntity
import com.example.erp_aone.viewmodel.InventoryViewModel
import com.example.erp_aone.viewmodel.SalesViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun SalesScreen(salesViewModel: SalesViewModel, inventoryViewModel: InventoryViewModel) {
    val sales by salesViewModel.sales.collectAsStateWithLifecycle()
    val items by inventoryViewModel.items.collectAsStateWithLifecycle()
    var showForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showForm) {
            SaleEntryForm(
                items = items,
                onSave = { item, soldTo, date, price, count ->
                    salesViewModel.makeSale(item, soldTo, date, price, count)
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
                Text("New Sale Entry")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sales) { sale ->
                SaleRow(sale)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleEntryForm(
    items: List<ItemEntity>,
    onSave: (ItemEntity, String, String, Double, Double) -> Unit,
    onCancel: () -> Unit
) {
    var selectedItem by remember { mutableStateOf<ItemEntity?>(null) }
    var soldTo by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-07-06") }
    var price by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.padding(16.dp).fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Sale Entry", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedItem?.shortName ?: "Select Item",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Item") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text("${item.shortName} (Stock: ${item.currentCount})") },
                            onClick = {
                                selectedItem = item
                                price = item.purchasePrice.toString() // Default to purchase price
                                expanded = false
                            }
                        )
                    }
                }
            }

            selectedItem?.let {
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Purchase Info: ₹${it.purchasePrice} on ${it.purchaseDate}", style = MaterialTheme.typography.bodySmall)
                        Text("Current Stock: ${it.currentCount}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedTextField(value = soldTo, onValueChange = { soldTo = it }, label = { Text("Sold To") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Selling Price") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = count, onValueChange = { count = it }, label = { Text("Quantity") }, modifier = Modifier.weight(1f))
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) { Text("Cancel") }
                Button(
                    onClick = {
                        selectedItem?.let { onSave(it, soldTo, date, price.toDoubleOrNull() ?: 0.0, count.toDoubleOrNull() ?: 0.0) }
                    },
                    enabled = selectedItem != null && (count.toDoubleOrNull() ?: 0.0) <= (selectedItem?.currentCount ?: 0.0)
                ) { Text("Submit Sale") }
            }
        }
    }
}

@Composable
fun SaleRow(sale: SaleEntity) {
    val total = sale.sellingPrice * sale.soldCount
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }

    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(sale.itemName, fontWeight = FontWeight.Bold)
                Text(currencyFormat.format(total), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text("Sold to: ${sale.soldTo} | Date: ${sale.sellingDate}", style = MaterialTheme.typography.bodySmall)
            Text("Qty: ${sale.soldCount} @ ${currencyFormat.format(sale.sellingPrice)}", style = MaterialTheme.typography.bodyMedium)
            Text("Stock After: ${sale.stockAfter}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
