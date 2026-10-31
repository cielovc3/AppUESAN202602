package dev.lchang.appuesan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTeamDao {

    // Flow: la UI se actualiza sola cada vez que cambia la tabla
    @Query("SELECT * FROM favorite_teams ORDER BY savedAt DESC")
    fun getAll(): Flow<List<FavoriteTeamEntity>>

    @Query("SELECT id FROM favorite_teams")
    fun getAllIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: FavoriteTeamEntity)

    @Query("DELETE FROM favorite_teams WHERE id = :teamId")
    suspend fun deleteById(teamId: Int)
}
