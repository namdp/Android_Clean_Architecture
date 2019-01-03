package com.namdinh.cleanarchitecture.data.local.room.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import com.namdinh.cleanarchitecture.data.local.room.converter.RepoSearchTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(RepoSearchTypeConverters::class)
data class RepoSearchResultEntity(
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
)
