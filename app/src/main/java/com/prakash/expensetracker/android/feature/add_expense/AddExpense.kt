@file:OptIn(ExperimentalMaterial3Api::class)

package com.prakash.expensetracker.android.feature.add_expense

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.prakash.expensetracker.android.R
import com.prakash.expensetracker.android.Utils
import com.prakash.expensetracker.android.data.model.ExpenseEntity
import com.prakash.expensetracker.android.viewmodel.AddExpenseViewModel
import com.prakash.expensetracker.android.viewmodel.AddExpenseViewModelFactory
import com.prakash.expensetracker.android.widget.ExpenseTextView
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpense(navController: NavController) {
    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("/home") { popUpTo(navController.graph.startDestinationId) { inclusive = true } } }
                        .align(Alignment.CenterStart)
                )
                ExpenseTextView(
                    text = "Add Transaction",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
//                Image(
//                    painter = painterResource(id = R.drawable.dots_menu),
//                    contentDescription = null,
//                    modifier = Modifier.align(Alignment.CenterEnd)
//                )
            }

            DataForm(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onAddExpenseClick = {
                    coroutineScope.launch {
                        if (viewModel.addExpense(it)) {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier: Modifier, onAddExpenseClick: (model: ExpenseEntity) -> Unit) {

    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }

    val nameError = remember { mutableStateOf(false) }
    val amountError = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        ExpenseTextView(text = "Name", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
                nameError.value = it.any { char -> char.isDigit() }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError.value
        )
        if (nameError.value) {
            ExpenseTextView(
                text = "Transaction name should not contain numbers",
                fontSize = 12.sp,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.size(8.dp))

        ExpenseTextView(text = "Amount", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = {
                amount.value = it
                amountError.value = it.toDoubleOrNull() == null && it.isNotEmpty()
            },
            modifier = Modifier.fillMaxWidth(),
            isError = amountError.value
        )
        if (amountError.value) {
            ExpenseTextView(
                text = "Please enter a valid number",
                fontSize = 12.sp,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.size(8.dp))

        // Date
        ExpenseTextView(text = "Date", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(
            value = if (date.value == 0L) "" else Utils.formatDateToHumanReadableForm(date.value),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            isError = showError.value && date.value == 0L
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Category Dropdown
        ExpenseTextView(text = "Category", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(
            listOf("Salary", "Bills", "EMI", "Entertainment", "Food & Drinks", "Fuel", "Groceries", "Health", "Investment", "Shopping", "Money Transfer", "Travel", "Other"),
            onItemSelected = {
                category.value = it
            },
            isError = showError.value && category.value.isEmpty()
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Type Dropdown
        ExpenseTextView(text = "Type", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(
            listOf("Income", "Expense"),
            onItemSelected = {
                type.value = it
            },
            isError = showError.value && type.value.isEmpty()
        )
        Spacer(modifier = Modifier.size(8.dp))

        if (showError.value) {
            ExpenseTextView(text = "Please fill in all required fields.", fontSize = 12.sp, color = Color.Red)
            Spacer(modifier = Modifier.size(8.dp))
        }

        // Add Expense Button
        Button(onClick = {
            if (name.value.isEmpty() || amount.value.isEmpty() || date.value == 0L || category.value.isEmpty() || type.value.isEmpty() || nameError.value || amountError.value) {
                showError.value = true
            } else {
                showError.value = false
                val model = ExpenseEntity(
                    null,
                    name.value,
                    amount.value.toDoubleOrNull() ?: 0.0,
                    Utils.formatDateToHumanReadableForm(date.value),
                    category.value,
                    type.value
                )
                onAddExpenseClick(model)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            ExpenseTextView(text = "Add Transaction", fontSize = 14.sp, color = Color.White)
        }
    }

    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(
            onDateSelected = {
                date.value = it
                dateDialogVisibility.value = false
            },
            onDismiss = {
                dateDialogVisibility.value = false
            }
        )
    }
}

@Composable
fun ExpenseDropDown(
    listOfItems: List<String>,
    onItemSelected: (item: String) -> Unit,
    isError: Boolean = false
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<String?>(null) }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        TextField(
            value = selectedItem.value ?: "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            isError = isError // Mark the dropdown field as an error if needed
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            listOfItems.forEach { item ->
                DropdownMenuItem(
                    text = { ExpenseTextView(text = item) },
                    onClick = {
                        selectedItem.value = item
                        onItemSelected(item)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    val currentDate = System.currentTimeMillis()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                ExpenseTextView(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                ExpenseTextView(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = { it <= currentDate } // Disable dates after the current date
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewAddExpense() {
    AddExpense(rememberNavController())
}