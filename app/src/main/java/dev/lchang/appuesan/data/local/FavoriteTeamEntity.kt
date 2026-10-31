package dev.lchang.appuesan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla local (SQLite vía Room) donde guardamos los equipos marcados como favoritos
@Entity(tableName = "favorite_teams")
data class FavoriteTeamEntity(
    @PrimaryKey val id: Int,          // mismo id que devuelve API Football
    val name: String,
    val country: String,
    val logo: String,
    val founded: Int?,
    val savedAt: Long = System.currentTimeMillis()
)
