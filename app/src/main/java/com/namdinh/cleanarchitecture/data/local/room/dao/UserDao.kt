/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    // @todo: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    @Query("SELECT * FROM userEntity WHERE login = :login")
    fun findByLogin(login: String): Flowable<List<UserEntity>>
}
