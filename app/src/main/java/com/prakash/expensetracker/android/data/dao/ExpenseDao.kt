package com.prakash.expensetracker.android.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prakash.expensetracker.android.data.model.ExpenseEntity
import com.prakash.expensetracker.android.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun getAllExpense(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense_table WHERE type = 'Expense' ORDER BY amount DESC LIMIT 5")
    fun getTopExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT type, date, SUM(amount) AS total_amount FROM expense_table where type = :type GROUP BY type, date ORDER BY date")
    fun getAllExpenseByDate(type: String = "Expense"): Flow<List<ExpenseSummary>>

    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)
    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun deleteAll(expenses: List<ExpenseEntity>)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM expense_table WHERE id = :id")
    suspend fun getExpenseById(id: Int): ExpenseEntity
}
