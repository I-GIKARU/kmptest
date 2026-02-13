package com.kmptest.viewmodels

import androidx.lifecycle.ViewModel
import com.kmptest.models.InventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventoryViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<InventoryItem>>(emptyList())
    val items: StateFlow<List<InventoryItem>> = _items.asStateFlow()
    
    init {
        // Sample data
        _items.value = listOf(
            InventoryItem("1", "Laptop", 10, 999.99, "Electronics"),
            InventoryItem("2", "Mouse", 50, 29.99, "Electronics"),
            InventoryItem("3", "Desk", 5, 299.99, "Furniture"),
            InventoryItem("4", "Chair", 15, 149.99, "Furniture"),
            InventoryItem("5", "Notebook", 100, 4.99, "Stationery")
        )
    }
    
    fun addItem(item: InventoryItem) {
        _items.value = _items.value + item
    }
    
    fun removeItem(id: String) {
        _items.value = _items.value.filter { it.id != id }
    }
    
    fun updateQuantity(id: String, newQuantity: Int) {
        _items.value = _items.value.map {
            if (it.id == id) it.copy(quantity = newQuantity) else it
        }
    }
}
