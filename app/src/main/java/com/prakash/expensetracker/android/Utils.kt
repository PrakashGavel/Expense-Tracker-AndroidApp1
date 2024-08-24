package com.prakash.expensetracker.android

import com.prakash.expensetracker.android.R
import com.prakash.expensetracker.android.data.model.ExpenseEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Utils {

    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDateForChart(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDayMonth(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatToDecimalValue(d: Double): String {
        return String.format("%.2f", d)
    }

    fun getMillisFromDate(date: String): Long {
        return getMilliFromDate(date)
    }

    fun getMilliFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }

    // Parses a date string in the format "dd/MM/yyyy" to milliseconds
    fun parseDateToMillis(dateString: String): Long {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = formatter.parse(dateString)
            date?.time ?: 0L
        } catch (e: ParseException) {
            e.printStackTrace()
            0L
        }
    }

    fun getItemIcon(item: ExpenseEntity): Int {
        if (item.category == "Salary") {
            return R.drawable.ic_salary
        } else if (item.category == "Bills") {
            return R.drawable.ic_bills
        } else if (item.category == "EMI") {
            return R.drawable.ic_emi
        } else if (item.category == "Entertainment") {
            return R.drawable.ic_entertainment
        } else if (item.category == "Food & Drinks") {
            return R.drawable.ic_foodanddrinks
        } else if (item.category == "Fuel") {
            return R.drawable.ic_fuel
        } else if (item.category == "Groceries") {
            return R.drawable.ic_groceries
        } else if (item.category == "Health") {
            return R.drawable.ic_health
        } else if (item.category == "Investment") {
            return R.drawable.ic_investment
        } else if (item.category == "Shopping") {
            return R.drawable.ic_shopping
        } else if (item.category == "Money Transfer") {
            return R.drawable.ic_transfer
        } else if (item.category == "Travel") {
            return R.drawable.ic_travel
        } else {
            return R.drawable.ic_other
        }
    }
}
