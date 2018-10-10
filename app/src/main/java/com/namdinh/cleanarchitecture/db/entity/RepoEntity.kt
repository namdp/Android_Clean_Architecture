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

package com.namdinh.cleanarchitecture.db.entity

import com.google.gson.annotations.SerializedName
import com.namdinh.cleanarchitecture.db.dto.Repo

data class RepoEntity(
        val id: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("full_name")
        val fullName: String,

        @SerializedName("description")
        val description: String?,

        @SerializedName("owner")
        val owner: OwnerEntity,

        @SerializedName("stargazers_count")
        val stars: Int
) {

    companion object {
        const val UNKNOWN_ID = -1
    }

    data class OwnerEntity(
            @SerializedName("login")
            val login: String,

            @SerializedName("url")
            val url: String?
    ) {
        fun toOwner() = Repo.Owner(login, url)
    }

    fun toRepo() = Repo(id, name, fullName, description, owner.toOwner(), stars)
}
