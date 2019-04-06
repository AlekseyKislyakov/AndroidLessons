package kislyakov.a08_1.DataModels;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.migration.Migration;

@Entity(tableName = "notes_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int noteId;

    @ColumnInfo(name = "first_name")
    private String noteHeader;

    @ColumnInfo(name = "last_name")
    private String noteText;

    @ColumnInfo(name = "note_color")
    private String noteColor;

    @Ignore
    private boolean isShown;

    public Note(String noteHeader, String noteText) {
        this.noteHeader = noteHeader;
        this.noteText = noteText;

    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteHeader() {
        return noteHeader;
    }

    public void setNoteHeader(String noteHeader) {
        this.noteHeader = noteHeader;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    public boolean getShown() {
        return isShown;
    }

    public void setShown(boolean isShown) {
        this.isShown = isShown;
    }


}

