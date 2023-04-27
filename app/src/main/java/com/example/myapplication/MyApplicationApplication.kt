package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.User
import com.example.myapplication.data.UserRoomDatabase

class MyApplicationApplication : Application() {
    val database: UserRoomDatabase by lazy {UserRoomDatabase.getDatabase(this)}
}