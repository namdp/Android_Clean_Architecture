package com.namdinh.cleanarchitecture.domain.vo

data class Contributor(
    val login: String,
    val contributions: Int,
    val avatarUrl: String?,
    val repoName: String?,
    val repoOwner: String?
)
