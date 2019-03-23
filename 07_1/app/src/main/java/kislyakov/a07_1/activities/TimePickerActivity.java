package kislyakov.a07_1.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kislyakov.a07_1.R;

public class TimePickerActivity extends AppCompatActivity {
    private int timeToRemind = 15;

    private TextView bridgeNameTextView;

    private TextView textView15min;
    private TextView textView30min;
    private TextView textView45min;

    private View topLine;
    private View bottomLine;

    private Button okButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        bridgeNameTextView = findViewById(R.id.time_picker_bridge_name);

        textView15min = findViewById(R.id.time_picker_15min);
        textView30min = findViewById(R.id.time_picker_30min);
        textView45min = findViewById(R.id.time_picker_45min);

        topLine = findViewById(R.id.time_picker_top_line);
        bottomLine = findViewById(R.id.time_picker_bottom_line);

        okButton = findViewById(R.id.time_picker_ok_button);
        cancelButton = findViewById(R.id.time_picker_cancel_button);

        Bundle extras = getIntent().getExtras();

        if(extras == null) {

        } else {
            bridgeNameTextView.setText(extras.getString(Intent.EXTRA_COMPONENT_NAME));
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_INDEX, timeToRemind);
                Log.d("LOL","Alarm started");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Log.d("LOL","Alarm stopped");
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    public void timePicker15min(View view) {
        textView15min.setTypeface(null, Typeface.BOLD);
        textView30min.setTypeface(null, Typeface.NORMAL);
        textView45min.setTypeface(null, Typeface.NORMAL);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topLine.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.time_picker_15min);
        topLine.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) bottomLine.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, R.id.time_picker_15min);
        bottomLine.setLayoutParams(params1);

        timeToRemind = 15;
    }

    public void timePicker30min(View view) {
        textView15min.setTypeface(null, Typeface.NORMAL);
        textView30min.setTypeface(null, Typeface.BOLD);
        textView45min.setTypeface(null, Typeface.NORMAL);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topLine.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.time_picker_30min);
        topLine.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) bottomLine.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, R.id.time_picker_30min);
        bottomLine.setLayoutParams(params1);

        timeToRemind = 30;
    }

    public void timePicker45min(View view) {
        textView15min.setTypeface(null, Typeface.NORMAL);
        textView30min.setTypeface(null, Typeface.NORMAL);
        textView45min.setTypeface(null, Typeface.BOLD);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topLine.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.time_picker_45min);
        topLine.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) bottomLine.getLayoutParams();
        params1.addRule(RelativeLayout.BELOW, R.id.time_picker_45min);
        bottomLine.setLayoutParams(params1);

        timeToRemind = 45;
    }
}
