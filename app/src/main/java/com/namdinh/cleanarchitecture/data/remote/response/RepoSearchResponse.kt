package com.namdinh.cleanarchitecture.data.remote.response

import com.google.gson.annotations.SerializedName
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoEntity

/**
 * Simple object to hold repo search responses. This is different from the Entity in the database
 * because we are keeping a search result in 1 row and denormalizing list of results into a single
 * column.
 */
data class RepoSearchResponse(
    @SerializedName("total_count")
    val total: Int = 0,

    @SerializedName("items")
    val items: List<RepoEntity>
) {
    var nextPage: Int? = null
}
