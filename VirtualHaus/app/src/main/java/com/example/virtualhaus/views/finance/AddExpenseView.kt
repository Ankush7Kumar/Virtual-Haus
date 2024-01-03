package com.example.virtualhaus.views.finance

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.models.DatabaseManager
import com.example.virtualhaus.models.TransactionItem
import com.example.virtualhaus.models.UserItem
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.ui.theme.BlueBackground
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.ui.theme.DarkGreen
import com.example.virtualhaus.views.common.DatePicker
import com.example.virtualhaus.views.common.MoneyTextField
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.destinations.FinanceScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun AddExpenseView(navigator: DestinationsNavigator, transactionItem: TransactionItem? = null) {
    val dbManager = DatabaseManager.shared
    val userManager = UserManager.shared
    val context = LocalContext.current
    userManager.loadUserInfo(context)

    val userId = UserManager.shared.userId!!
    val userData = DatabaseManager.shared.getUserData(userId)
    val userName = userData?.get("name").toString()

    val expenseName = transactionItem?.name ?: ""
    val expenseDescription = transactionItem?.description ?: ""
    val expenseAmt = "${transactionItem?.amt ?: ""}"
    val expenseAmount = transactionItem?.amt ?: 0.00
    val itemPurchaseDate = transactionItem?.date ?: Date()

    Column(
        modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
    ) {
        var name by remember { mutableStateOf(TextFieldValue(expenseName)) }
        var description by remember { mutableStateOf(TextFieldValue(expenseDescription)) }
        var amount by remember { mutableStateOf(if (expenseAmount == 0.00) TextFieldValue("") else TextFieldValue(String.format("%.2f", expenseAmount))) }
        var amt by remember { mutableStateOf(expenseAmt) }
        var purchaseDate by remember { mutableStateOf(itemPurchaseDate) }
        var costInputIsValid by remember { mutableStateOf(true) }
        val Roommates = getAllRoommates()
        var userItems by remember {
            mutableStateOf(
                Roommates.map {
                    UserItem(
                        name = it,
                        isSelected = false
                    )
                }
            )
        }

        Row() {
            AddExpenseHeader()
        }

        // Expense Name
        Row() {
            Column() {
                OutlinedTextField(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Expense") },
                    singleLine = true,
                )
            }
        }

        // Amount
        Row() {
            Column() {
                MoneyTextField(
                        value = amt,
                        label = "Amount",
                        isValid = costInputIsValid,
                        onValueChange = { text, isValid -> amt = text; costInputIsValid = isValid },
                        enabled = true,
                )
                /*
                OutlinedTextField(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(text = "Amount") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                */
            }
        }

        Row() {
            Column() {
                DatePicker(
                        initialDate = purchaseDate,
                        label = "Purchase date",
                        onDateChange = { purchaseDate = it },
                )
            }
        }



        Row() {
            Column() {
                Text("For:", fontFamily = BodyFont)
            }
        }

        // Figure out how to get roommates
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,) {
            LazyColumn(
                modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
            ) {
                items(userItems.size) {i ->
                    Row(
                        modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    userItems = userItems.mapIndexed { j, item ->
                                        if (i == j) {
                                            item.copy(isSelected = !item.isSelected)
                                        } else item
                                    }
                                }
                                .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            backgroundColor = BlueBackground,
                            border = BorderStroke(2.dp,BluePrimary)
                        ) {
                            Text(userItems[i].name, modifier = Modifier.padding(16.dp))
                        }
                        if (userItems[i].isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "selected",
                                tint = DarkGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            // Buttons
            Row(
                modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row() {
                        CancelButton(navigator)
                        Spacer(modifier = Modifier.size(10.dp))

                        val selectedUserItems = userItems.filter { it.isSelected }

                        val submitButtonEnabled = description.text != "" && amt != "" && selectedUserItems.isNotEmpty() && costInputIsValid

                        PrimaryButton(
                                text = "Add",
                                enabled = submitButtonEnabled,
                        ) {
                            //on click

                            for (user in selectedUserItems) {
                                /*
                                val transactionItem = TransactionItem(
                                        id = UUID.randomUUID().toString(),
                                        borrowed = false,
                                        name = user.name,
                                        description = description.text.toString(),
                                        amt = (amount.text.toString().toDouble()) / (selectedUserItems.size + 1),
                                        users = mutableListOf(userName),
                                        date = purchaseDate,
                                        paid = false,
                                )

                             */
                                //val id1 = dbManager.addTransaction(userManager.userId!!, userManager.homeId!!, transactionItem)

                                // Transaction item for money that selected users owe current user
                                val transactionItemBorrowed = TransactionItem(
                                        borrowed = true,
                                        // Person to be paid back
                                        name = userName,
                                        description = description.text.toString(),
                                        amt = amt.toString().toDouble() / (selectedUserItems.size + 1),//(amount.text.toString().toDouble()) / (selectedUserItems.size + 1),
                                        // Person who borrowed
                                        users = mutableListOf(user.name),
                                        date = purchaseDate,
                                        paid = false,
                                )
                                Log.v("Store Transaction", transactionItemBorrowed.toString())
                                val id2 = dbManager.addTransaction(userManager.userId!!, userManager.homeId!!, transactionItemBorrowed)
                            }
                            navigator.navigate(FinanceScreenDestination)
                        }
                    }
                }
            }
        }
    }
}

// Mock Data
@Composable
fun getAllRoommates(): MutableList<String> {
    val dbManager = DatabaseManager.shared
    val userManager = UserManager.shared
    val context = LocalContext.current
    userManager.loadUserInfo(context)
    val usersInHouse = dbManager.getUsersInHouse(userManager.homeId!!)

    //Get Current user
    val userId = UserManager.shared.userId!!
    val userData = DatabaseManager.shared.getUserData(userId)
    val userName = userData?.get("name")

    val users = mutableListOf<String>()
    for (user in usersInHouse) {
        if (user["name"] != null && user["name"] != userName) {
            users.add(user["name"].toString())
        }
    }
    return users
}

@Composable
fun AddExpenseHeader() {
    Box() {
        Text(
            text = "Add Expense",
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
    }
}

@Composable
fun AddButton(navigator: DestinationsNavigator, onClick: () -> Unit) {
    PrimaryButton(
            text = "Add",
            enabled = true,
    ) {
        onClick()
    }
}

@Composable
fun CancelButton(navigator: DestinationsNavigator) {
    PrimaryButton(
            text = "Cancel",
            enabled = true,
    ) {
        navigator.navigateUp()
    }
}