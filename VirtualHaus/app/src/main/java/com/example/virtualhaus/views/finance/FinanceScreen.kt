package com.example.virtualhaus.views.finance

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtualhaus.models.DatabaseManager
import com.example.virtualhaus.models.TransactionItem
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.ui.theme.*
import com.example.virtualhaus.views.common.PrimaryButton
import com.example.virtualhaus.views.destinations.AddExpenseViewDestination
import com.example.virtualhaus.views.destinations.UserFinanceViewDestination
import com.google.firebase.Timestamp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.abs

//Get Current user
val userId = UserManager.shared.userId!!
val userData = DatabaseManager.shared.getUserData(userId)
val userName = userData?.get("name")

@Destination
@Composable
fun FinanceScreen(navigator: DestinationsNavigator) {
    val dbManager = DatabaseManager.shared
    val userManager = UserManager.shared
    val context = LocalContext.current
    userManager.loadUserInfo(context)

    val transactionsList = getAllTransactions()

    Row() {
        Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row() {
                //FinanceHeader()
            }
            Row(
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row() {
                        AddExpenseButton(navigator)
                        Spacer(modifier = Modifier.size(10.dp))
                        ReceivePaymentButton(navigator)
                    }
                }
            }
            Row() {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        Card(backgroundColor = BlueBackground,
                            border = BorderStroke(2.dp,BluePrimary)
                        ) {
                            Row() {
                                Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    //YouOwe()
                                    //Add amount you owe
                                    var amtOwed = 0.00
                                    for (transaction in transactionsList) {
                                        if (userName in transaction.users) {
                                            amtOwed -= transaction.amt!!
                                        }
                                        else {
                                            amtOwed += transaction.amt!!
                                        }
                                    }
                                    var rounded = String.format("%.2f", abs(amtOwed))
                                    val textColor = if (amtOwed < 0) DarkRed else DarkGreen
                                    val textMsg = if (amtOwed < 0) "You Owe" else "You are Owed"
                                    Box() {
                                        Text(
                                                text = textMsg,
                                                textAlign = TextAlign.Center,
                                                fontSize = 24.sp,
                                                fontFamily = BodyFont
                                        )
                                    }
                                    Text(text = "$$rounded", color = textColor, fontWeight = FontWeight.Bold, fontSize = 20.sp,)
                                }
                                /*
                                Spacer(modifier = Modifier.size(30.dp))
                                Column(modifier = Modifier.padding(10.dp)) {
                                    OweYou()
                                    //Add amount you owe
                                    var amtOwed = 0.00
                                    Log.v("Transactions", transactionsList.toString())
                                    for (transaction in transactionsList) {
                                        if (transaction.borrowed == false) {
                                            if (transaction.paid!!) {
                                                amtOwed -= transaction.amt!!
                                            } else {
                                                amtOwed += transaction.amt!!
                                            }
                                        }
                                    }
                                    var rounded = String.format("%.2f", amtOwed)
                                    Text(text = "$$rounded", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp,)
                                    //Add amount owed to you
                                }
                                */
                            }
                        }
                    }
                }
            }
            Row() {
                //List of transactions
                val transactionsList = getAllTransactions()
                // Get all users that you have transactions with
                val users = mutableListOf<String>()
                for (transaction in transactionsList) {
                    if (!(transaction.name in users)) {
                        users.add(transaction.name.toString())
                    }
                    if (transaction.users.size > 0 && !(transaction.users[0] in users)) {
                        users.add(transaction.users[0].toString())
                    }
                }
                val userList = users.filter{it != userName}
                Log.v("Users", userList.toString())
                LazyColumn(Modifier.fillMaxSize()) {
                    itemsIndexed(userList) { _, user ->
                        var transactions = transactionsList.filter {it.name == user || user in it.users}
                        Log.v(user, transactions.toString())
                        UserView(user, transactions) {
                            navigator.navigate(UserFinanceViewDestination(name = user))
                        }
                    }
                }
                /*
                LazyColumn(Modifier.fillMaxSize()) {
                    itemsIndexed(transactionsList) { _, transaction ->
                        TransactionView(transaction) {
                            navigator.navigate(ReceivePaymentViewDestination(transactionItem = transaction))
                        }
                    }
                }
                */
            }
        }
    }
}

// Mock Data
@Composable
fun getAllTransactions(): List<TransactionItem> {
    val dbManager = DatabaseManager.shared
    val userManager = UserManager.shared
    val context = LocalContext.current
    userManager.loadUserInfo(context)

    val transactions = dbManager.getTransactionsForUser(userManager.userId.toString() ,userManager.homeId.toString())
    val transactionsList = mutableListOf<TransactionItem>()

    //Get Current user
    val userId = UserManager.shared.userId!!
    val userData = DatabaseManager.shared.getUserData(userId)
    val userName = userData?.get("name")

    for (transaction in transactions) {
        if (transaction["amt"] != null && transaction["paid"] == false) {
            val users = mutableListOf<String>()
            if (transaction["users"] != null) {
                for (user in transaction["users"] as MutableList<String>) {
                    users.add(user)
                }
                // Not involved in transaction
                if (!(userName in users) && userName != transaction["name"]) {
                    continue
                }

                if (!(userName in users) && userName != transaction["name"]) {
                    continue
                }
            }
            val item = TransactionItem(
                    transaction["id"].toString(),
                    transaction["borrowed"] as Boolean?,
                    transaction["name"].toString(),
                    transaction["description"].toString(),
                    transaction["amt"] as Double?,
                    users,
                    (transaction["date"] as Timestamp?)?.toDate(),
                    transaction["paid"] as Boolean?
            )
            transactionsList.add(item)
        }
    }
    Log.v("TRANSACTIONS", transactionsList.toString())
    return transactionsList
}

@Composable
fun FinanceHeader() {
    Box() {
        Text(
            text = "Finances",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontFamily = HeaderFont,
        )
    }
}

@Composable
fun YouOwe() {
    Box() {
        Text(
            text = "You Owe",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = BodyFont
        )
    }
}

@Composable
fun OweYou() {
    Box() {
        Text(
            text = "You are Owed",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = BodyFont
        )
    }
}

@Composable
fun UserView(name: String, transactions: List<TransactionItem>, onClick: () -> Unit) {
    // Calculate totals
    var total = 0.00
    for (transaction in transactions) {
        if (userName in transaction.users) {
            total -= transaction.amt!!
        } else {
            total += transaction.amt!!
        }
    }
    val textColor = if (total < 0) DarkRed else DarkGreen
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
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                    text = name,
                                    fontFamily = BodyFont,
                                    fontSize = 24.sp,
                            )
                        }
                        Spacer(modifier = Modifier.size(30.dp))
                        Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp), horizontalAlignment = Alignment.End) {
                            Text(
                                    text = ("$").plus(String.format("%.2f", total)),
                                    fontFamily = BodyFont,
                                    fontSize = 24.sp,
                                    color = textColor
                            )
                        }
                    }
                }
            },
    )
}

@Composable
fun TransactionView(transaction: TransactionItem, onClick: () -> Unit) {
    val textColor = if (userName in transaction.users) DarkRed else DarkGreen
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
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                                text = transaction.name,
                                fontFamily = BodyFont,
                                fontSize = 24.sp,
                        )
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp), horizontalAlignment = Alignment.End) {
                        Text(
                                text = ("$").plus(String.format("%.2f", transaction.amt)),
                                fontFamily = BodyFont,
                                fontSize = 24.sp,
                                color = textColor
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = transaction.description,
                        fontFamily = BodyFont,
                        fontSize = 16.sp,
                    )
                }
            }
        },
    )
}

@Composable
fun AddExpenseButton(navigator: DestinationsNavigator) {
    PrimaryButton(
            text = "Add Expense",
            enabled = true,
    ) {
        navigator.navigate(AddExpenseViewDestination())
    }

    /*
    Button(onClick = {
        navigator.navigate(AddExpenseViewDestination())
    },
        colors = ButtonDefaults.buttonColors(backgroundColor = BluePrimary))
    {
        Text(text = "Add Expense", color = Color.White, fontFamily = BodyFont)
    }
    */
}

@Composable
fun ReceivePaymentButton(navigator: DestinationsNavigator) {
    PrimaryButton(
            text = "Receive Payment",
            enabled = true,
    ) {
        navigator.navigate(UserFinanceViewDestination(name = ""))
    }
}