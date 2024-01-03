package com.example.virtualhaus.views.finance

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
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.destinations.FinanceScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ReceivePaymentView(navigator: DestinationsNavigator, transactionItem: TransactionItem? = null) {
    val dbManager = DatabaseManager.shared
    val userManager = UserManager.shared
    val context = LocalContext.current
    userManager.loadUserInfo(context)

    val expenseName = transactionItem?.users?.get(0) ?: ""
    val expenseDescription = transactionItem?.description ?: ""
    val expenseAmount = transactionItem?.amt ?: 0.00
    val expensePayee = transactionItem?.users?.get(0) ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        var name by remember { mutableStateOf(TextFieldValue(expenseName)) }
        var description by remember { mutableStateOf(TextFieldValue(expenseDescription)) }
        var amount by remember { mutableStateOf(if (expenseAmount == 0.00) TextFieldValue("") else TextFieldValue(String.format("%.2f", expenseAmount))) }
        var payee = expensePayee ?: ""
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
            ReceivePaymentHeader()
        }

        // Expense Name
        Row() {
            Column() {
                //Text("Receive Payment For:", fontFamily = BodyFont)
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Expense") },
                    singleLine = true,
                    readOnly = true
                )
            }
        }

        // Amount
        Row() {
            Column() {
                //Text("Amount:", fontFamily = BodyFont)
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(text = "Amount") },
                    singleLine = true,
                    readOnly = true
                )
            }
        }

        // Amount
        Row() {
            Column() {
                Text("From:", fontFamily = BodyFont)
            }
        }
        // Figure out how to get roommates
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f)
            ) {
                items(userItems.size) {i ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable{
                            userItems = userItems.mapIndexed { j, item ->
                                if (i == j) {
                                    //payee = item.name
                                    payee = payee
                                    item
                                    //item.copy(isSelected = !item.isSelected)
                                } else item
                            }
                        }.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            backgroundColor = BlueBackground,
                            border = BorderStroke(2.dp,BluePrimary)
                        ) {
                            Text(userItems[i].name, modifier = Modifier.padding(16.dp))
                        }
                        if (userItems[i].name == payee) {
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
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row() {
                        CancelButton(navigator)
                        Spacer(modifier = Modifier.size(10.dp))
                        ConfirmButton(navigator) {
                            // Update transaction to paid in db
                            val id = dbManager.updateTransaction(transactionItem?.id!!, "paid", true)
                            navigator.navigate(FinanceScreenDestination)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReceivePaymentHeader() {
    Box() {
        Text(
            text = "Receive Payment",
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
    }
}

@Composable
fun ConfirmButton(navigator: DestinationsNavigator, onClick: () -> Unit) {
    PrimaryButton(
            text = "Done",
            enabled = true,
    ) {
        onClick()
    }
}