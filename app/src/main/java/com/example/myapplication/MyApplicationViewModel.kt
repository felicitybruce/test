package com.example.myapplication

import android.view.View
import androidx.lifecycle.*
import com.example.myapplication.data.User
import com.example.myapplication.data.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MyApplicationViewModel(private val userDao: UserDao) : ViewModel() {

    private val selectedUser = MutableLiveData<Flow<User>>()
    val selectedUserLiveData: LiveData<Flow<User>>
    get() = selectedUser

    /**
     * Updates an existing Item in the database.
     */
    fun updateUser(
        userId: Int,
        userName: String,
        userFirstName: String,
        userEmail: String
    ) {
        val updatedUser = getUpdatedUserEntry(userId, userName, userFirstName, userEmail)
        updateUser(updatedUser)
    }

    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateUser(user: User) {
        viewModelScope.launch {
            userDao.update(user)
        }
    }

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
     * Retrieve an user from the repository.
     */
    fun retrieveUser(id: Int) {
        viewModelScope.launch {
            selectedUser.postValue(userDao.getUser(id))
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

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [User] entity class with the item info updated by the user.
     */
    fun getUpdatedUserEntry(
        userId: Int,
        userName: String,
        userFirstName: String,
        userEmail: String

    ): User {
        return User(
            id = userId,
            username = userName,
            firstName = userFirstName,
            email = userEmail
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class MyApplicationViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyApplicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyApplicationViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
