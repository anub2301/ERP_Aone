package com.example.erp_aone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.erp_aone.data.entity.CustomerEntity
import com.example.erp_aone.viewmodel.CustomerViewModel

@Composable
fun CustomerScreen(viewModel: CustomerViewModel) {
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    var showForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Search Customers") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        if (showForm) {
            CustomerEntryForm(
                onSave = { n, p, a, g -> viewModel.addCustomer(n, p, a, g); showForm = false },
                onCancel = { showForm = false }
            )
        } else {
            Button(onClick = { showForm = true }, modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add New Customer")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(customers) { customer ->
                CustomerRow(customer)
            }
        }
    }
}

@Composable
fun CustomerEntryForm(onSave: (String, String, String, String) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gst by remember { mutableStateOf("") }

    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Customer Details", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = gst, onValueChange = { gst = it }, label = { Text("GST Number") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onCancel) { Text("Cancel") }
                Button(onClick = { onSave(name, phone, address, gst) }) { Text("Save") }
            }
        }
    }
}

@Composable
fun CustomerRow(customer: CustomerEntity) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(customer.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Phone: ${customer.phone}", style = MaterialTheme.typography.bodyMedium)
            Text("Address: ${customer.address}", style = MaterialTheme.typography.bodySmall)
            if (customer.gstNumber.isNotEmpty()) {
                Text("GST: ${customer.gstNumber}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
