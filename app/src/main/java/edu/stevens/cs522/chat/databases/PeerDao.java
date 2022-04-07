package edu.stevens.cs522.chat.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.stevens.cs522.chat.entities.Peer;

@Dao
public abstract class PeerDao {

    @Query("SELECT * FROM Peer")
    public abstract LiveData<List<Peer>> fetchAllPeers();

    @Query("SELECT * FROM Peer WHERE name LIKE :name LIMIT 1")
    protected abstract long getPeerId(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Peer peer);

    @Update
    protected abstract void update(Peer peer);

    @Transaction
    public void upsert(Peer peer) {
        long id = getPeerId(peer.name);
        if (id == 0){
            insert(peer);
        }else{
            peer.id = id;
            update(peer);
        }
    }
}
