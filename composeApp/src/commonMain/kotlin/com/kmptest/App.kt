package com.kmptest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kmptest.models.InventoryItem
import com.kmptest.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: InventoryViewModel = viewModel { InventoryViewModel() }
    val items by viewModel.items.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Inventory Manager") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Text("+")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Total Items: ${items.size}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Total Value: $${items.sumOf { it.price * it.quantity }}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Items List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        InventoryItemCard(
                            item = item,
                            onDelete = { viewModel.removeItem(item.id) },
                            onUpdateQuantity = { newQty ->
                                viewModel.updateQuantity(item.id, newQty)
                            }
                        )
                    }
                }
            }
        }
        
        if (showAddDialog) {
            AddItemDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { item ->
                    viewModel.addItem(item)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun InventoryItemCard(
    item: InventoryItem,
    onDelete: () -> Unit,
    onUpdateQuantity: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text("${item.category} • $${item.price}", style = MaterialTheme.typography.bodySmall)
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { if (item.quantity > 0) onUpdateQuantity(item.quantity - 1) },
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("-")
                }
                Text("${item.quantity}", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { onUpdateQuantity(item.quantity + 1) },
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("+")
                }
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("×")
                }
            }
        }
    }
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAdd: (InventoryItem) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") }
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") }
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && quantity.isNotBlank() && price.isNotBlank()) {
                        onAdd(
                            InventoryItem(
                                id = (0..999999).random().toString(),
                                name = name,
                                quantity = quantity.toIntOrNull() ?: 0,
                                price = price.toDoubleOrNull() ?: 0.0,
                                category = category.ifBlank { "General" }
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
