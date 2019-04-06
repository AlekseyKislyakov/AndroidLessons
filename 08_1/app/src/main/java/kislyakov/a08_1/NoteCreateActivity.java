package kislyakov.a08_1;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kislyakov.a08_1.DataModels.Note;
import kislyakov.a08_1.DataModels.NoteDatabase;
import petrov.kristiyan.colorpicker.ColorPicker;

public class NoteCreateActivity extends AppCompatActivity {

    private EditText noteTitleEditText;
    private EditText noteTextEditText;

    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        Intent parentIntent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitleEditText = findViewById(R.id.note_title_edittext);
        noteTextEditText = findViewById(R.id.note_text_edittext);

        final NoteDatabase db = Room.databaseBuilder(this,
                NoteDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
        if (parentIntent.hasExtra("position_item")) {
            int positionItem = parentIntent.getIntExtra("position_item", 0);
            db.noteDao().getById(positionItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(note -> {
                        noteTitleEditText.setHint("Название");
                        noteTitleEditText.setText(note.getNoteHeader());

                        noteTextEditText.setHint("Текст");
                        noteTextEditText.setText(note.getNoteText());

                        currentNote = note;
                        Log.d("myTag", "OPEN EXISTING");

                    });
        } else {
            noteTitleEditText.setText("");
            noteTextEditText.setText("");
            noteTitleEditText.setHint("Название");
            noteTextEditText.setHint("Текст");
            currentNote = new Note("", "");
            currentNote.setNoteId(0);
            Log.d("myTag", "CREATE NEW");
        }
    }

    @Override
    public void onBackPressed() {

        if (noteTextEditText.getText().toString().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NoteCreateActivity.this);
            builder.setTitle("Нет текста заметки")
                    .setMessage("Введите текст заметки!")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("note_id", currentNote.getNoteId());
            returnIntent.putExtra("note_title", noteTitleEditText.getText().toString());
            returnIntent.putExtra("note_text", noteTextEditText.getText().toString());
            returnIntent.putExtra("note_color", currentNote.getNoteColor());
            setResult(1, returnIntent);
            finish();
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                onBackPressed();
                break;
            }
            case R.id.action_colorize:{
                LinearLayout layout = findViewById(R.id.color_picker_layout);
                final ColorPicker colorPicker = new ColorPicker(this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#456789");
                colors.add("#66aaaa");
                colors.add("#ff8888");
                colors.add("#dd88dd");
                colors.add("#8800ff");
                colors.add("#44ffff");
                colors.add("#88ff88");
                colors.add("#aa44aa");
                colors.add("#ffff00");
                colors.add("#ff00ff");
                colorPicker.setColors(colors)
                        .setColumns(5)
                        .disableDefaultButtons(true)
                        .setDefaultColorButton(0xff00ff)
                        .setTitle("Выберите цвет заметки")
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {

                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .addListenerButton("OK", (v, position, color) -> {
                            if(position == -1){
                                colorPicker.dismissDialog();
                            }
                            else {
                                Log.d("myTag", Integer.toHexString(color));
                                currentNote.setNoteColor(colors.get(position));
                                colorPicker.dismissDialog();
                            }

                        })
                        .addListenerButton("ОТМЕНА", (v, position, color) ->
                                colorPicker.dismissDialog()).show();

            }
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_create, menu);
        return true;
    }

}
