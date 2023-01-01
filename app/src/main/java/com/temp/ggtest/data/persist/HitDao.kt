package com.temp.ggtest.data.persist

import androidx.room.*
import androidx.room.Dao
import com.temp.ggtest.data.model.Hit

/**
 * Room Dao interface , provides methods for persisting hits
 */
@Dao
interface HitDao {

    /**
     * gets hit from the database using its id
     * @param id of the hit
     * @return hit
     */
    @Query("SELECT * FROM Hit WHERE  id = :id LIMIT 1")
    suspend fun loadHit(id : String) : Hit?

    /**
     * gets all hits
     * @return list of hits
     */
    @Query("SELECT * FROM Hit")
    suspend fun loadHits() : List<Hit>?

    /**
     * inserts a single hit row
     * @param hit to insert
     * @return row number
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHit(hit : Hit) : Long

    /**
     * inserts hits
     * @param hits to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHits(hits : List<Hit>)

    /**
     * deletes all hits from database
     */
    @Query("DELETE FROM Hit")
    suspend fun deleteHits()

    /**
     * refresh all hits
     * @param hits to insert
     * @return whether hits were changed
     */
    @Transaction
    suspend fun refreshHits(hits : List<Hit> , oldHits : List<Hit>? = null) : Boolean{
        if(oldHits != hits){
            deleteHits()
            insertHits(hits)
            return true
        }
        return false
    }
}