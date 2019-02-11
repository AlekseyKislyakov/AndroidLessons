package kislyakov.a05_1;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity4 extends AppCompatActivity {

    private TextView show_time_tv;
    private Button goto4_button_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent parentIntent = getIntent();
        show_time_tv = findViewById(R.id.show_time_tv);

        if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String getExtra = parentIntent.getStringExtra(Intent.EXTRA_TEXT);
            show_time_tv.setText(TimeConverter(getExtra));
        }

        goto4_button_again = findViewById(R.id.button_goto4_again);

        goto4_button_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Activity4.this;
                String currentTime = Long.toString(System.currentTimeMillis());
                Class goto4_activity = Activity4.class;
                Intent intent_goto4 = new Intent(context, goto4_activity);
                intent_goto4.putExtra(Intent.EXTRA_TEXT, currentTime);
                onNewIntent(intent_goto4);
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);

        Intent parentIntent = getIntent();

        show_time_tv = findViewById(R.id.show_time_tv);

        if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String getExtra = parentIntent.getStringExtra(Intent.EXTRA_TEXT);
            show_time_tv.setText(TimeConverter(getExtra));
        }


    }

    private String TimeConverter(String mills){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(mills));
        String result = formatter.format(calendar.getTime());
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}

