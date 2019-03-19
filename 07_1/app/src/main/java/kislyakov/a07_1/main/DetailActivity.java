package kislyakov.a07_1.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import kislyakov.a07_1.AlertReceiver;
import kislyakov.a07_1.DetailObject;
import kislyakov.a07_1.R;
import kislyakov.a07_1.TimePickerActivity;
import kislyakov.a07_1.adapters.BridgesAdapter;
import kislyakov.a07_1.models.Divorce;

public class DetailActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView divorceTV;
    private TextView decriptionTV;

    private ImageView mainPicture;
    private ImageView bridgeStateIV;

    private Button remindButton;

    private List<Divorce> divorceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent parentIntent = getIntent();
        setIntent(parentIntent);

        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);


        nameTV = findViewById(R.id.nameDetailTV);
        divorceTV = findViewById(R.id.divorce_textView);
        decriptionTV = findViewById(R.id.description_textView);

        mainPicture = findViewById(R.id.detailBridgePictureIV);
        bridgeStateIV = findViewById(R.id.statusSmallIV);

        remindButton = findViewById(R.id.detail_reminder_button);

        if (parentIntent.hasExtra("LOL")) {
            final DetailObject detailObject = (DetailObject) getIntent().getParcelableExtra("LOL");
            Divorce temp = new Divorce();
            divorceList = detailObject.getDivorces();

            int stateBridge = temp.DivorceState(detailObject.getDivorces());
            if (stateBridge == 1) {
                bridgeStateIV.setBackgroundResource(R.drawable.ic_brige_normal);
                Glide
                        .with(this)
                        .load("http://gdemost.handh.ru/" + detailObject.getPictureOpen())
                        .into(mainPicture);
                nameTV.setText(detailObject.getBridgeName());

            } else if (stateBridge == 0) {
                bridgeStateIV.setBackgroundResource(R.drawable.ic_brige_soon);
                Glide
                        .with(this)
                        .load("http://gdemost.handh.ru/" + detailObject.getPictureOpen())
                        .into(mainPicture);
                nameTV.setText(detailObject.getBridgeName());
            } else {
                bridgeStateIV.setBackgroundResource(R.drawable.ic_brige_late);
                Glide
                        .with(this)
                        .load("http://gdemost.handh.ru/" + detailObject.getPictureClosed())
                        .into(mainPicture);
                nameTV.setText(detailObject.getBridgeName());
            }
            String divorces = temp.DivorceConverter(detailObject.getDivorces());
            divorceTV.setText(divorces);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                decriptionTV.setText(Html.fromHtml(detailObject.getBridgeDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                decriptionTV.setText(Html.fromHtml(detailObject.getBridgeDescription()));
            }

            remindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TimePickerActivity.class);
                    intent.putExtra(Intent.EXTRA_COMPONENT_NAME, detailObject.getBridgeName());
                    startActivityForResult(intent,1);
                }
            });

        } else {
            nameTV.setText("Ты долбаеб");
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            int dataIntExtra = data.getIntExtra(Intent.EXTRA_INDEX, 0);
            Calendar now = Calendar.getInstance();
            Divorce temp = new Divorce();

            boolean reminderSetFlag = false;
            Calendar start = Calendar.getInstance();
            start.set(Calendar.SECOND, 0);
            for (int i = 0; i < divorceList.size(); i++)
            {
                start.setTime(temp.TimestrToCalendar(divorceList.get(i).getStart()));
                start.add(Calendar.MINUTE,-1*dataIntExtra);
                if(now.before(start)){
                    reminderSetFlag = true;
                    break;
                }
            }

            if(!reminderSetFlag){
                start = Calendar.getInstance();
                start.setTime(temp.TimestrToCalendar(divorceList.get(0).getStart()));
                start.add(Calendar.DATE,1);
                start.add(Calendar.MINUTE,-1*dataIntExtra);
            }
            Date correct = new Date(start.getTimeInMillis()-10800000);
            start.setTime(correct);
            start = Calendar.getInstance();
            startAlarm(start);

            remindButton.setText("За " + dataIntExtra + " минут");
        }
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}

