package com.namdinh.cleanarchitecture.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.namdinh.cleanarchitecture.data.local.room.entity.UserEntity
import io.reactivex.Flowable

/**
 * Interface for database access for User related operations.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    // TODO we wrapped into `List` to get an empty list instead of complete silence if there is no record
    @Query("SELECT * FROM userEntity WHERE login = :login")
    fun findByLogin(login: String): Flowable<List<UserEntity>>
}
