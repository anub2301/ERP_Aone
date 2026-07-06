package com.example.erp_aone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.erp_aone.data.entity.BillEntity
import com.example.erp_aone.data.entity.BillLineEntity
import com.example.erp_aone.viewmodel.BankDetails
import com.example.erp_aone.viewmodel.BillingViewModel
import com.example.erp_aone.viewmodel.CustomerViewModel
import com.example.erp_aone.viewmodel.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun BillingScreen(
    billingViewModel: BillingViewModel,
    customerViewModel: CustomerViewModel,
    inventoryViewModel: InventoryViewModel
) {
    var showInvoiceForm by remember { mutableStateOf(false) }
    val bills by billingViewModel.bills.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        if (showInvoiceForm) {
            InvoiceForm(
                billingViewModel = billingViewModel,
                customerViewModel = customerViewModel,
                inventoryViewModel = inventoryViewModel,
                onClose = { showInvoiceForm = false }
            )
        } else {
            Button(onClick = { showInvoiceForm = true }, modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Create New Invoice")
            }
            
            Text("Previous Invoices", modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bills) { bill ->
                    BillHistoryRow(bill)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceForm(
    billingViewModel: BillingViewModel,
    customerViewModel: CustomerViewModel,
    inventoryViewModel: InventoryViewModel,
    onClose: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-07-06") }
    var reduceStock by remember { mutableStateOf(true) }
    
    val currentLines by billingViewModel.currentBillLines.collectAsStateWithLifecycle()
    val customers by customerViewModel.customers.collectAsStateWithLifecycle()
    val inventory by inventoryViewModel.items.collectAsStateWithLifecycle()

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("New GST Invoice", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        
        // Customer Info
        Card {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Customer Information", fontWeight = FontWeight.Bold)
                OutlinedTextField(value = customerName, onValueChange = { customerName = it }, label = { Text("Customer Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
            }
        }

        // Bill Items
        Card {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Bill Items", fontWeight = FontWeight.Bold)
                
                currentLines.forEachIndexed { index, line ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(line.itemName, fontWeight = FontWeight.Bold)
                            Text("${line.qty} x ${currencyFormat.format(line.price)} (Disc: ${line.discount})", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { billingViewModel.removeBillLine(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                    HorizontalDivider()
                }
                
                var newItemName by remember { mutableStateOf("") }
                var newQty by remember { mutableStateOf("") }
                var newPrice by remember { mutableStateOf("") }
                var newDisc by remember { mutableStateOf("") }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(value = newItemName, onValueChange = { newItemName = it }, label = { Text("Item") }, modifier = Modifier.weight(2f))
                    OutlinedTextField(value = newQty, onValueChange = { newQty = it }, label = { Text("Qty") }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(value = newPrice, onValueChange = { newPrice = it }, label = { Text("Price") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = newDisc, onValueChange = { newDisc = it }, label = { Text("Disc") }, modifier = Modifier.weight(1f))
                    Button(onClick = {
                        billingViewModel.addBillLine(newItemName, newQty.toDoubleOrNull() ?: 0.0, newPrice.toDoubleOrNull() ?: 0.0, newDisc.toDoubleOrNull() ?: 0.0)
                        newItemName = ""; newQty = ""; newPrice = ""; newDisc = ""
                    }, modifier = Modifier.align(Alignment.CenterVertically)) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }
        }

        // Calculations
        val taxable = currentLines.sumOf { (it.price * it.qty) - it.discount }
        val tax = taxable * 0.18
        val total = taxable + tax

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Taxable Amount:")
                    Text(currencyFormat.format(taxable))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("GST (18%):")
                    Text(currencyFormat.format(tax))
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Grand Total:", fontWeight = FontWeight.Bold)
                    Text(currencyFormat.format(total), fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = reduceStock, onCheckedChange = { reduceStock = it })
            Text("Reduce Stock after Billing")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onClose, modifier = Modifier.weight(1f)) { Text("Cancel") }
            Button(onClick = {
                billingViewModel.generateBill(
                    customerName, mobile, address, date, reduceStock,
                    BankDetails("ERP Aone", "State Bank", "123456789", "SBIN0001")
                )
                onClose()
            }, modifier = Modifier.weight(1f)) { Text("Generate Bill") }
        }
    }
}

@Composable
fun BillHistoryRow(bill: BillEntity) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Bill #${bill.id}", fontWeight = FontWeight.Bold)
                Text(bill.date, style = MaterialTheme.typography.bodySmall)
            }
            Text(bill.customerName, style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(currencyFormat.format(bill.grandTotal), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Row {
                    IconButton(onClick = {}) { Icon(Icons.Default.Share, contentDescription = null) }
                    IconButton(onClick = {}) { Icon(Icons.Default.Print, contentDescription = null) }
                }
            }
        }
    }
}
