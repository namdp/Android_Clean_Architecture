package com.namdinh.cleanarchitecture.data.local.room.entity

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.namdinh.cleanarchitecture.domain.vo.User

@Entity(primaryKeys = ["login"])
data class UserEntity(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatarUrl: String?,
    @field:SerializedName("name")
    val name: String?,
    @field:SerializedName("company")
    val company: String?,
    @field:SerializedName("repos_url")
    val reposUrl: String?,
    @field:SerializedName("blog")
    val blog: String?
) {
    fun toUser() = User(login, avatarUrl, name, company, reposUrl, blog)
}
