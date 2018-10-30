package com.namdinh.cleanarchitecture.data.local.room.converter

import androidx.room.TypeConverter
import timber.log.Timber

object RepoSearchTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let { it ->
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }
}
