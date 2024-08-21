package com.prakash.expensetracker.android.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prakash.expensetracker.android.Utils
import com.prakash.expensetracker.android.data.ExpenseDatabase
import com.prakash.expensetracker.android.data.dao.ExpenseDao
import com.prakash.expensetracker.android.data.model.ExpenseEntity
import java.lang.IllegalArgumentException

class HomeViewModel(val dao: ExpenseDao) : ViewModel() {
    val expenses = dao.getAllExpense()

    fun getBalance(list: List<ExpenseEntity>): String {
        var balance = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return "₹ ${Utils.formatToDecimalValue(balance)}"
    }

//    fun getTotalExpense(list: List<ExpenseEntity>): String {
//        var total = 0.0
//        for (expense in list) {
//            total += expense.amount
//        }
//
//        return "₹ ${Utils.formatToDecimalValue(total)}"
//    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var totalExpense = 0.0
        for (expense in list) {
            if (expense.type != "Income") {  // Only sum up the actual expenses
                totalExpense += expense.amount
            }
        }
        return "₹ ${Utils.formatToDecimalValue(totalExpense)}"
    }


    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                totalIncome += expense.amount
            }
        }
        return "₹ ${Utils.formatToDecimalValue(totalIncome)}"
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDatabase.getInstance(context).expenseDao()
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}