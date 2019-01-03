package com.namdinh.cleanarchitecture.domain.vo

data class User(
    val login: String,
    val avatarUrl: String?,
    val name: String?,
    val company: String?,
    val reposUrl: String?,
    val blog: String?
)
