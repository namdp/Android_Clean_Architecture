package com.namdinh.cleanarchitecture.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import com.namdinh.cleanarchitecture.domain.vo.Contributor

@Entity(
    primaryKeys = ["repoName", "repoOwner", "login"],
    foreignKeys = [ForeignKey(
        entity = RepoEntity::class,
        parentColumns = ["name", "owner_login"],
        childColumns = ["repoName", "repoOwner"],
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class ContributorEntity(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("contributions")
    val contributions: Int,
    @field:SerializedName("avatar_url")
    val avatarUrl: String?
) {

    // does not show up in the response but set in post processing.
    lateinit var repoName: String
    // does not show up in the response but set in post processing.
    lateinit var repoOwner: String

    fun toContributor() = Contributor(login, contributions, avatarUrl, repoName, repoOwner)
}
