package com.namdinh.cleanarchitecture.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.namdinh.cleanarchitecture.data.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.entity.RepoSearchResultEntity
import com.namdinh.cleanarchitecture.data.entity.UserEntity
import com.namdinh.cleanarchitecture.data.local.room.dao.RepoDao
import com.namdinh.cleanarchitecture.data.local.room.dao.UserDao

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
