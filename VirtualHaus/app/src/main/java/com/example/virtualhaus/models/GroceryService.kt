package com.example.virtualhaus.models

interface GroceryService {
    fun addOrUpdateGroceryItem(groceryItem: GroceryItem)

    fun deleteGroceryItem(groceryItem: GroceryItem)

    fun getGroceryItems(onUpdate: (List<GroceryItem>) -> Unit)
}