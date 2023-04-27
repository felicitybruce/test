package com.example.myapplication

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.User
import com.example.myapplication.data.UserDao
import kotlinx.coroutines.launch

class MyApplicationViewModel(private val userDao: UserDao) : ViewModel() {

    /**
     * Inserts the new User into database.
     */
    fun addNewUser(userName: String, userFirstName: String, userEmail: String) {
        val newUser = getNewUserEntry(userName, userFirstName, userEmail)
        insertUser(newUser)
    }

    /**
     * Launching a new coroutine to insert an user in a non-blocking way
     */
    private fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    /**
     * Returns an instance of the [User] entity class with the user info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewUserEntry(userName: String, userFirstName: String, userEmail: String): User {
        return User(
            // Left is exact name for User.kt
            username = userName,
            firstName = userFirstName,
            email = userEmail

        )
    }


}