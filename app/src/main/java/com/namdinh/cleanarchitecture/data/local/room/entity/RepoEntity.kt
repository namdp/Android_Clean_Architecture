package com.namdinh.cleanarchitecture.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import com.namdinh.cleanarchitecture.domain.vo.Repo

/**
 * Using name/owner_login as primary key instead of id since name/owner_login is always available
 * vs id is not.
 */
@Entity(
    indices = [
        Index("id"),
        Index("owner_login")],
    primaryKeys = ["name", "owner_login"]
)
data class RepoEntity(
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("full_name")
    val fullName: String,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("owner")
    @field:Embedded(prefix = "owner_")
    val ownerEntity: OwnerEntity,
    @field:SerializedName("stargazers_count")
    val stars: Int
) {

    companion object {
        const val UNKNOWN_ID = -1
    }

    data class OwnerEntity(
        @field:SerializedName("login")
        val login: String,
        @field:SerializedName("url")
        val url: String?
    ) {
        fun toOwner() = Repo.Owner(login, url)
    }

    fun toRepo() = Repo(id, name, fullName, description, ownerEntity.toOwner(), stars)
}
