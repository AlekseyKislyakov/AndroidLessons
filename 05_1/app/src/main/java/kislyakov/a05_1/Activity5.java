package kislyakov.a05_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity5 extends AppCompatActivity {
    private EditText editText;
    private Button goto3_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        editText = (EditText) findViewById(R.id.edit_text_snackbar);
        goto3_button = (Button) findViewById(R.id.button_goto3_again);

        Toolbar toolbar5 = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        goto3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                String result = editText.getText().toString();
                returnIntent.putExtra(Intent.ACTION_ANSWER, result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
