package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): Flow<User>


    @Query("SELECT * from user ORDER BY firstName ASC")
    fun getUsers(): Flow<List<User>>






}
