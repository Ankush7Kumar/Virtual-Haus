package com.example.virtualhaus.viewmodels

import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.*

class AddGroceryItemViewModel : ViewModel() {
    private val groceryService: GroceryService = GroceryServiceImpl()
    private val userId = UserManager.shared.userId!!

    var ownerName = ""
        private set

    init {
        DatabaseManager.shared.getUserData(userId)?.let { data ->
            ownerName = data["name"] as String
        }
    }

    fun submitGroceryItem(groceryItem: GroceryItem) {
        groceryService.addOrUpdateGroceryItem(groceryItem)
    }
}