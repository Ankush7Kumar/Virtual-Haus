package com.example.virtualhaus.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val GROCERY_COLLECTION_PATH = "groceryList"

class GroceryServiceImpl : GroceryService {
    private val dbGroceryItems = Firebase.firestore.collection(GROCERY_COLLECTION_PATH)
    private val homeId = UserManager.shared.homeId

    override fun addOrUpdateGroceryItem(groceryItem: GroceryItem) {
        val item = hashMapOf(
            "itemName" to groceryItem.itemName,
            "needToBuy" to groceryItem.needToBuy,
            "ownerName" to groceryItem.ownerName,
            "purchaseDate" to groceryItem.purchaseDate,
            "cost" to groceryItem.cost,
            "homeId" to homeId,
        )
        val dbGroceryItem = if (groceryItem.id.isBlank()) {
            dbGroceryItems.document() // new document
        } else {
            dbGroceryItems.document(groceryItem.id) // existing document
        }
        dbGroceryItem.set(item)
    }

    override fun deleteGroceryItem(groceryItem: GroceryItem) {
        dbGroceryItems
            .document(groceryItem.id)
            .delete()
    }

    override fun getGroceryItems(onUpdate: (List<GroceryItem>) -> Unit) {
        dbGroceryItems
            .whereEqualTo("homeId", homeId)
            .addDocumentSnapshotListener { documents ->
                val groceryItems = documents.mapNotNull { document ->
                    val data = document.data ?: return@mapNotNull null
                    GroceryItem(
                        id = document.id,
                        itemName = data["itemName"] as String,
                        needToBuy = data["needToBuy"] as Boolean,
                        ownerName = data["ownerName"] as String?,
                        purchaseDate = (data["purchaseDate"] as Timestamp?)?.toDate(),
                        cost = data["cost"] as Double?,
                    )
                }

                onUpdate(groceryItems)
            }
    }
}