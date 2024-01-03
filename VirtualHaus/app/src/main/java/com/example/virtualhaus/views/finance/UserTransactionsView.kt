package com.example.virtualhaus.views.finance

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.models.TransactionItem
import com.example.virtualhaus.ui.theme.*
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.common.formatted
import com.example.virtualhaus.views.destinations.ReceivePaymentViewDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun UserFinanceView(navigator: DestinationsNavigator, name: String) {
    val transactionsList = getAllTransactions().sortedByDescending { it.date }
    val userTransactions = if (name != "") transactionsList.filter {it.name == name || name in it.users} else transactionsList
    Log.v(name, userTransactions.toString())

    // Calculate totals
    var total = 0.00
    for (transaction in userTransactions) {
        if (userName in transaction.users) {
            total -= transaction.amt!!
        } else {
            total += transaction.amt!!
        }
    }

    Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,) {
        Row() {
            Box() {
                Text(
                        text = if (name != "") ("Transaction History:  ").plus(name) else "Select a transaction",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = HeaderFont,
                )
            }
        }
        Row() {
            PrimaryButton(
                    text = "Go Back",
                    enabled = true,
            ) {
                navigator.navigateUp()
            }

        }
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(userTransactions) { _, transaction ->
                TransactionCard(transaction, name) {
                    if (userName == transaction.name) {
                        navigator.navigate(ReceivePaymentViewDestination(transactionItem = transaction))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: TransactionItem, name:String, onClick: () -> Unit) {
    val textColor = if (userName in transaction.users) DarkRed else DarkGreen
    val symbol = if (userName in transaction.users) "-$" else "$"
    Button(
            onClick = onClick,
            modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(BlueBackground),
            shape = Shapes.medium,
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                                text = transaction.description,
                                fontFamily = BodyFont,
                                fontSize = 16.sp,
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.End) {
                            Text(
                                    text = (symbol).plus(String.format("%.2f", transaction.amt)),
                                    fontFamily = BodyFont,
                                    fontSize = 24.sp,
                                    color = textColor
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (name == "") {
                            val displayName = if (userName == transaction.name) transaction.users[0] else transaction.name
                            Text(
                                    text = displayName,
                                    fontFamily = BodyFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                        }
                        Text(
                                text = if (transaction.date != null) transaction.date?.formatted()!! else "N/A",
                                fontFamily = BodyFont,
                                fontSize = 16.sp,
                        )
                    }
                }
            },
    )
}