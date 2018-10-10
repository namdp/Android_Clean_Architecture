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
import com.namdinh.cleanarchitecture.db.dto.User

data class UserEntity(
        @SerializedName("login")
        val login: String,

        @SerializedName("avatar_url")
        val avatarUrl: String?,

        @SerializedName("name")
        val name: String?,

        @SerializedName("company")
        val company: String?,

        @SerializedName("repos_url")
        val reposUrl: String?,

        @SerializedName("blog")
        val blog: String?
) {
    fun toUser() = User(login, avatarUrl, name, company, reposUrl, blog)
}