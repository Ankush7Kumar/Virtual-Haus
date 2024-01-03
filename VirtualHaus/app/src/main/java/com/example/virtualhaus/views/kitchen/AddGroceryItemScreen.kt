package com.example.virtualhaus.views.kitchen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.models.GroceryItem
import com.example.virtualhaus.viewmodels.AddGroceryItemViewModel
import com.example.virtualhaus.views.common.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun AddGroceryItemScreen(
        navigator: DestinationsNavigator,
        groceryItem: GroceryItem? = null,
        viewModel: AddGroceryItemViewModel = viewModel(),
) {
    val itemName = groceryItem?.itemName ?: ""
    val itemNeedToBuy = groceryItem?.needToBuy ?: false
    val itemOwner = groceryItem?.ownerName ?: viewModel.ownerName
    val itemPurchaseDate = groceryItem?.purchaseDate ?: Date()
    val itemCost = "${groceryItem?.cost ?: ""}"
    val operationText = if (groceryItem == null) "Add" else "Update"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        var name by remember { mutableStateOf(itemName) }
        var needToBuy by remember { mutableStateOf(itemNeedToBuy) }
        var purchaseDate by remember { mutableStateOf(itemPurchaseDate) }
        var cost by remember { mutableStateOf(itemCost) }
        var nameInputIsValid by remember { mutableStateOf(true) }
        var costInputIsValid by remember { mutableStateOf(true) }

        Title(text = "$operationText Item", modifier = Modifier.padding(bottom = 10.dp))

        LabeledCheckbox(
            label = "Need to buy?",
            checked = needToBuy,
            onCheckedChanged = { needToBuy = it },
        )

        NameTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            label = "Item name",
            value = name,
            isValid = nameInputIsValid,
            onValueChange = { text, isValid -> name = text; nameInputIsValid = isValid },
        )

        if (!needToBuy) {
            TextBox(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                value = itemOwner,
                label = "Owner",
            )

            MoneyTextField(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                value = cost,
                label = "Cost",
                isValid = costInputIsValid,
                onValueChange = { text, isValid -> cost = text; costInputIsValid = isValid },
                enabled = !needToBuy,
            )

            DatePicker(
                initialDate = purchaseDate,
                label = "Purchase date",
                enabled = !needToBuy,
                onDateChange = { purchaseDate = it },
            )
        }

        Spacer(modifier = Modifier.weight(weight = 1F))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            PrimaryButton(text = "Cancel") { navigator.navigateUp() }

            Spacer(modifier = Modifier.size(20.dp))

            val updatedGroceryItem = GroceryItem(
                id = groceryItem?.id ?: "",
                itemName = name,
                needToBuy = needToBuy,
                ownerName = itemOwner,
                purchaseDate = purchaseDate.takeUnless { needToBuy },
                cost = cost.toDoubleOrNull(),
            )

            val submitButtonEnabled = canSubmitUpdate(
                oldItem = groceryItem,
                newItem = updatedGroceryItem,
                inputsAreValid = needToBuy || (nameInputIsValid && costInputIsValid),
            )
            PrimaryButton(text = operationText, enabled = submitButtonEnabled) {
                viewModel.submitGroceryItem(updatedGroceryItem)
                navigator.navigateUp()
            }
        }
    }
}

private fun canSubmitUpdate(
    oldItem: GroceryItem?,
    newItem: GroceryItem,
    inputsAreValid: Boolean,
): Boolean {
    if (!inputsAreValid) return false

    return if (oldItem == null) {
        newItem.itemName.isNotBlank()
    } else {
        oldItem != newItem
    }
}
