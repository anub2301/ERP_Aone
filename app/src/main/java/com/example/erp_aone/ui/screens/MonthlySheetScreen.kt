package com.example.erp_aone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.erp_aone.viewmodel.DailyReport
import com.example.erp_aone.viewmodel.MonthlySheetViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun MonthlySheetScreen(viewModel: MonthlySheetViewModel) {
    val reports by viewModel.dailyReports.collectAsStateWithLifecycle()
    var showExpenseForm by remember { mutableStateOf(false) }
    var showInvestmentForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showExpenseForm = true }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add Expense")
            }
            Button(onClick = { showInvestmentForm = true }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add Invest")
            }
        }

        if (showExpenseForm) {
            EntryForm(title = "Add Expense", onSave = { d, a, n -> viewModel.addExpense(d, a, n); showExpenseForm = false }, onCancel = { showExpenseForm = false })
        }
        if (showInvestmentForm) {
            EntryForm(title = "Add Investment", onSave = { d, a, n -> viewModel.addInvestment(d, a, n); showInvestmentForm = false }, onCancel = { showInvestmentForm = false })
        }

        DailyReportHeader()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reports) { report ->
                DailyReportRow(report)
            }
        }
    }
}

@Composable
fun EntryForm(title: String, onSave: (String, Double, String) -> Unit, onCancel: () -> Unit) {
    var date by remember { mutableStateOf("2026-07-06") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancel) { Text("Cancel") }
                Button(onClick = { onSave(date, amount.toDoubleOrNull() ?: 0.0, note) }) { Text("Save") }
            }
        }
    }
}

@Composable
fun DailyReportHeader() {
    Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer).padding(8.dp)) {
        Text("Date", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        Text("Sales", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        Text("Profit", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        Text("Exp", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        Text("Inv", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun DailyReportRow(report: DailyReport) {
    var expanded by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(report.date, modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall)
            Text(currencyFormat.format(report.sales), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
            Text(currencyFormat.format(report.profit), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = if(report.profit >= 0) Color(0xFF2E7D32) else Color.Red)
            Text(currencyFormat.format(report.expense), modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.bodySmall)
            Text(currencyFormat.format(report.investment), modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.bodySmall)
            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp)) {
                if (report.details.sales.isNotEmpty()) {
                    Text("Sales", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                    report.details.sales.forEach { Text("• ${it.itemName}: ${it.soldCount} @ ${it.sellingPrice}", style = MaterialTheme.typography.bodySmall) }
                }
                if (report.details.purchases.isNotEmpty()) {
                    Text("Purchases", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
                    report.details.purchases.forEach { Text("• ${it.shortName}: ${it.initialCount} @ ${it.purchasePrice}", style = MaterialTheme.typography.bodySmall) }
                }
                if (report.details.expenses.isNotEmpty()) {
                    Text("Expenses", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
                    report.details.expenses.forEach { Text("• ${it.note}: ${it.amount}", style = MaterialTheme.typography.bodySmall) }
                }
                if (report.details.investments.isNotEmpty()) {
                    Text("Investments", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
                    report.details.investments.forEach { Text("• ${it.note}: ${it.amount}", style = MaterialTheme.typography.bodySmall) }
                }
            }
        }
        HorizontalDivider()
    }
}
