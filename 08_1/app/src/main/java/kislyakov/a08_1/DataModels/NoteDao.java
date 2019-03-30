package kislyakov.a08_1.DataModels;


import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@android.arch.persistence.room.Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes_table")
    List<Note> getAll();

    @Query("SELECT * FROM notes_table")
    Flowable<List<Note>> getAllObservable();

    @Query("SELECT * FROM notes_table WHERE noteId = :id")
    Flowable<Note> getById(long id);

}
