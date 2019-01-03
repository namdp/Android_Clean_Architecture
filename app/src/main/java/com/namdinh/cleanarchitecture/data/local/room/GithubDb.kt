package com.namdinh.cleanarchitecture.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.namdinh.cleanarchitecture.data.local.room.dao.RepoDao
import com.namdinh.cleanarchitecture.data.local.room.dao.UserDao
import com.namdinh.cleanarchitecture.data.local.room.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoSearchResultEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.UserEntity

/**
 * Main database description.
 */
@Database(
    entities = [
        UserEntity::class,
        RepoEntity::class,
        ContributorEntity::class,
        RepoSearchResultEntity::class],
    version = 3,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao
}
