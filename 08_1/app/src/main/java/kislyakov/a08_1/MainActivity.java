package kislyakov.a08_1;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import kislyakov.a08_1.DataModels.Note;
import kislyakov.a08_1.DataModels.NoteDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvNotes;
    List<Note> notesList;
    NoteDatabase db;
    NoteDatabase archiveDB;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvNotes = findViewById(R.id.rvNotes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
        archiveDB = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, "archive")
                .allowMainThreadQueries()
                .build();

        notesList = db.noteDao().getAll();

        rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesAdapter(notesList, this, positionItem -> {
            Intent intent = new Intent(getApplicationContext(), NoteCreateActivity.class);
            intent.putExtra("position_item", getNoteFromTable(positionItem).getNoteId());
            startActivityForResult(intent, 1);
        }, positionLong -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Что делать с заметкой")
                    //.setMessage("")
                    .setCancelable(true)
                    .setPositiveButton("Архивировать",
                            (dialog, which) -> {
                                dialog.cancel();
                                //db.noteDao().delete(db.noteDao().getById(positionLong+1));
                                Flowable.fromCallable(() -> {
                                    archiveDB.noteDao().insert(getNoteFromTable(positionLong));
                                    db.noteDao().delete(getNoteFromTable(positionLong));
                                    return db.noteDao().getAll();
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(notes -> UpdateTable(notes));
                            })
                    .setNegativeButton("Удалить",
                            (dialog, id) -> {
                                dialog.cancel();
                                Flowable.fromCallable(() -> {
                                    db.noteDao().delete(getNoteFromTable(positionLong));
                                    return db.noteDao().getAll();
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(notes -> UpdateTable(notes));
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        });
        rvNotes.setAdapter(adapter);


        //db.noteDao().insert(new Note(0, "Димок", "Бегает как конь"));
        //db.noteDao().insert(new Note(1, "Колек", "Жирный как танк"));
        //db.noteDao().insert(new Note(2, "Никит", "Любит карате"));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteCreateActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // запишем в лог значения requestCode и resultCode
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        // если пришло ОК
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {

            Flowable.fromCallable(() -> {
                Note temp = new Note(
                        data.getStringExtra("note_title"),
                        data.getStringExtra("note_text"));
                if (data.getIntExtra("note_id", 0) == 0) {
                    db.noteDao().insert(temp);
                } else {
                    temp.setNoteId(data.getIntExtra("note_id", 1));
                    db.noteDao().insert(temp);
                }
                return db.noteDao().getAll();
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(notes -> UpdateTable(notes));

        } else {
            Log.d("myTag", "error");
        }
    }


    public void UpdateTable(List<Note> notesList) {
        adapter.notesList = notesList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Note getNoteFromTable(int position){
        return db.noteDao().getAll().get(position);
    }
}
