package com.prakash.expensetracker.android.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prakash.expensetracker.android.Utils
import com.prakash.expensetracker.android.data.ExpenseDatabase
import com.prakash.expensetracker.android.data.dao.ExpenseDao
import com.prakash.expensetracker.android.data.model.ExpenseSummary
import com.github.mikephil.charting.data.Entry
import java.lang.IllegalArgumentException

class StatsViewModel(val dao: ExpenseDao) : ViewModel() {
    val entries = dao.getAllExpenseByDate()
    val topEntries =  dao.getTopExpenses()
    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in entries) {
            val formattedDate = Utils.getMillisFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            val dao = ExpenseDatabase.getInstance(context).expenseDao()
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}