package com.example.virtualhaus.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualhaus.models.GroceryItem
import com.example.virtualhaus.models.GroceryService
import com.example.virtualhaus.models.GroceryServiceImpl

class GroceryViewModel : ViewModel() {
    private val groceryService: GroceryService = GroceryServiceImpl()

    private val groceryItems: MutableLiveData<List<GroceryItem>> by lazy {
        MutableLiveData<List<GroceryItem>>().apply {
            groceryService.getGroceryItems { updatedItems ->
                // Sort everything by purchase date, where null (need to buy) items come first
                value = updatedItems.sortedBy { it.purchaseDate }
            }
        }
    }

    fun getGroceryItems(): LiveData<List<GroceryItem>> = groceryItems

    fun deleteGroceryItem(groceryItem: GroceryItem) {
        groceryService.deleteGroceryItem(groceryItem)
    }
}