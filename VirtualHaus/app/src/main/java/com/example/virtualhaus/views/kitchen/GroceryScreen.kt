package com.example.virtualhaus.views.kitchen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualhaus.R
import com.example.virtualhaus.models.GroceryItem
import com.example.virtualhaus.models.TransactionItem
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BlueSecondary
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.ui.theme.Shapes
import com.example.virtualhaus.viewmodels.GroceryViewModel
import com.example.virtualhaus.views.common.ActionButton
import com.example.virtualhaus.views.common.Title
import com.example.virtualhaus.views.common.formatted
import com.example.virtualhaus.views.destinations.AddExpenseViewDestination
import com.example.virtualhaus.views.destinations.AddGroceryItemScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun GroceryScreen(navigator: DestinationsNavigator, viewModel: GroceryViewModel = viewModel()) {
    val groceryItems by viewModel.getGroceryItems().observeAsState(initial = emptyList())
    var isEditing by rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomEnd) {
        if (groceryItems.isEmpty()) EmptyListView()

        LazyColumn(Modifier.fillMaxSize()) {
            items(groceryItems) { groceryItem ->
                GroceryItemView(
                    groceryItem = groceryItem,
                    isEditing = isEditing,
                    onDelete = { viewModel.deleteGroceryItem(groceryItem) },
                    onAddExpense = if (groceryItem.cost == null) null else {{
                        val transactionItem = TransactionItem(
                            borrowed = false,
                            name = "",
                            description = groceryItem.itemName,
                            amt = groceryItem.cost,
                            users = mutableListOf(),
                            date = groceryItem.purchaseDate,
                            paid = false
                        )
                        navigator.navigate(AddExpenseViewDestination(transactionItem))
                    }},
                    onClick = {
                        navigator.navigate(AddGroceryItemScreenDestination(groceryItem))
                    },
                    modifier = when (groceryItem.id) {
                        groceryItems.firstOrNull()?.id -> Modifier.padding(top = 10.dp)
                        groceryItems.lastOrNull()?.id -> Modifier.padding(bottom = 10.dp)
                        else -> Modifier
                    }
                )
            }

            // Allow the user to scroll past the bottom buttons
            item { Box(modifier = Modifier.fillMaxWidth().height(70.dp)) }
        }

        Row(modifier = Modifier.padding(10.dp)) {
            ActionButton(iconId = R.drawable.ic_edit) {
                isEditing = !isEditing
            }

            Spacer(modifier = Modifier.width(10.dp))

            ActionButton(iconId = R.drawable.ic_plus) {
                navigator.navigate(AddGroceryItemScreenDestination())
            }
        }
    }
}

@Composable
private fun GroceryItemView(
    groceryItem: GroceryItem,
    isEditing: Boolean,
    onAddExpense: (() -> Unit)?,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (buttonColor, textColor) = if (groceryItem.needToBuy) {
        BlueSecondary to Color.Black
    } else {
        BluePrimary to Color.White
    }
    
    Button(
        onClick = onClick,
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .fillMaxWidth()
            .height(80.dp)
    ) {
        GroceryItemInfo(groceryItem = groceryItem, textColor = textColor)
        
        Spacer(modifier = Modifier.weight(1F))

        GroceryItemActions(isEditing = isEditing, onDelete = onDelete, onAddExpense = onAddExpense)
    }
}

@Composable
fun GroceryItemInfo(groceryItem: GroceryItem, textColor: Color) {
    Column {
        Row {
            Text(
                text = groceryItem.itemName,
                fontFamily = BodyFont,
                fontSize = 18.sp,
                color = textColor,
            )

            groceryItem.ownerName?.let { owner ->
                Text(
                    text = " - $owner",
                    fontFamily = BodyFont,
                    fontSize = 18.sp,
                    color = textColor,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            if (groceryItem.needToBuy) {
                Text(
                    text = "Need to buy",
                    fontFamily = BodyFont,
                    fontSize = 12.sp,
                    color = textColor,
                )
            }

            groceryItem.purchaseDate?.let { purchaseDate ->
                Text(
                    text = "Purchased: ${purchaseDate.formatted()}",
                    fontFamily = BodyFont,
                    fontSize = 12.sp,
                    color = textColor,
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

            groceryItem.cost?.let { cost ->
                Text(
                    text = "Cost: $${String.format("%.2f", cost)}",
                    fontFamily = BodyFont,
                    fontSize = 12.sp,
                    color = textColor,
                )
            }
        }
    }
}

@Composable
fun GroceryItemActions(isEditing: Boolean, onDelete: () -> Unit, onAddExpense: (() -> Unit)?) {
    AnimatedVisibility(visible = isEditing) {
        Row(modifier = Modifier.widthIn(max = 70.dp)) {
            IconButton(
                onClick = { onAddExpense?.let { it() } },
                modifier = Modifier.alpha(if (onAddExpense == null) 0F else 1F)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dollar),
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
fun EmptyListView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Title(text = "Your grocery list is empty!")

            Text(
                text = "Add items using the plus icon.",
                fontFamily = BodyFont,
                fontSize = 14.sp,
            )
        }
    }
}
