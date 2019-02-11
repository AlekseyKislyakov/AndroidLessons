package kislyakov.a05_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button goto4_button;
    private Button goto2_button;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goto4_button = findViewById(R.id.button_goto4);

        goto4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                String currentTime = Long.toString(System.currentTimeMillis());
                Class goto4_activity = Activity4.class;
                Intent intent_goto4 = new Intent(context, goto4_activity);
                intent_goto4.putExtra(Intent.EXTRA_TEXT, currentTime);
                startActivity(intent_goto4);
            }
        });

        goto2_button = findViewById(R.id.button_goto2);

        goto2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                String currentTime = Long.toString(System.currentTimeMillis());
                Class goto2_activity = Activity2.class;
                Intent intent_goto2 = new Intent(context, goto2_activity);
                startActivity(intent_goto2);
            }
        });
    }
}
