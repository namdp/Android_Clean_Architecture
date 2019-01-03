package com.namdinh.cleanarchitecture.data.local.room.dao

import android.util.SparseIntArray
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.namdinh.cleanarchitecture.data.local.room.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoSearchResultEntity
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Interface for database access on Repo related operations.
 */
@Dao
abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repos: RepoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContributors(contributors: List<ContributorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<RepoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createRepoIfNotExists(repo: RepoEntity): Long

    // TODO Room: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    @Query("SELECT * FROM repoEntity WHERE owner_login = :ownerLogin AND name = :name")
    abstract fun load(ownerLogin: String, name: String): Flowable<List<RepoEntity>>

    @Query(
        """
        SELECT login, avatarUrl, repoName, repoOwner, contributions FROM contributorEntity
        WHERE repoName = :name AND repoOwner = :owner
        ORDER BY contributions DESC"""
    )
    abstract fun loadContributors(owner: String, name: String): Flowable<List<ContributorEntity>>

    @Query(
        """
        SELECT * FROM RepoEntity
        WHERE owner_login = :owner
        ORDER BY stars DESC"""
    )
    abstract fun loadRepositories(owner: String): Flowable<List<RepoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: RepoSearchResultEntity)

    // TODO Room: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    @Query("SELECT * FROM RepoSearchResultEntity WHERE `query` = :query")
    abstract fun search(query: String): Flowable<List<RepoSearchResultEntity>>

    fun loadOrdered(repoIds: List<Int>): Flowable<List<RepoEntity>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return loadById(repoIds).map { repositories ->
            repositories.sortedWith(kotlin.Comparator { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            })
        }
    }

    @Query("SELECT * FROM RepoEntity WHERE id in (:repoIds)")
    protected abstract fun loadById(repoIds: List<Int>): Flowable<List<RepoEntity>>

    // TODO Room: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    @Query("SELECT * FROM RepoSearchResultEntity WHERE `query` = :query")
    abstract fun findSearchResult(query: String): Single<List<RepoSearchResultEntity>>
}
