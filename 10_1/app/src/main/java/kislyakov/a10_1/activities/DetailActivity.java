package kislyakov.a10_1.activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kislyakov.a10_1.classes.AlertReceiver;
import kislyakov.a10_1.models.DetailObject;
import kislyakov.a10_1.R;
import kislyakov.a10_1.models.Divorce;

public class DetailActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView divorceTV;
    private TextView decriptionTV;

    private ImageView mainPicture;
    private ImageView bridgeStateIV;

    private Button remindButton;

    private List<Divorce> divorceList;

    private DetailObject detailObjectforPI;

    private boolean reminderSetFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent parentIntent = getIntent();
        setIntent(parentIntent);

        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);

        int sizeStack =  am.getRunningTasks(5).size();

        for(int i = 0;i < sizeStack;i++) {

            ComponentName cn = am.getRunningTasks(5).get(i).baseActivity;
            Log.d("Activities", cn.getClassName());
        }
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
            detailObjectforPI = detailObject;
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
                    startActivityForResult(intent, 1);
                }
            });

        } else {
            nameTV.setText("Ты долбаеб");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(this, "You pressed the home button!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("reminder_result", reminderSetFlag);
        setResult(RESULT_FIRST_USER,intent);
        finish();
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int dataIntExtra = data.getIntExtra(Intent.EXTRA_INDEX, 0);
            Calendar now = Calendar.getInstance();
            Divorce temp = new Divorce();

            reminderSetFlag = false;
            Calendar start = Calendar.getInstance();
            start.set(Calendar.SECOND, 0);
            String divorceTime = "";
            for (int i = 0; i < divorceList.size(); i++) {
                start.setTime(temp.TimestrToCalendar(divorceList.get(i).getStart()));
                divorceTime = divorceList.get(i).getStart();
                start.add(Calendar.MINUTE, -1 * dataIntExtra);
                Date correct = new Date(start.getTimeInMillis() - 10800000);
                start.setTime(correct);
                if (now.before(start)) {
                    reminderSetFlag = true;
                    break;
                }
            }

            if (!reminderSetFlag) {
                start = Calendar.getInstance();
                start.setTime(temp.TimestrToCalendar(divorceList.get(0).getStart()));
                start.add(Calendar.DATE, 1);
                divorceTime = divorceList.get(0).getStart();
                start.add(Calendar.MINUTE, -1 * dataIntExtra);

            }
            //start = Calendar.getInstance();
            String bridgeName = detailObjectforPI.getBridgeName();

            startAlarm(start, bridgeName, divorceTime, detailObjectforPI.getPosition());

            DateFormat sdf = new SimpleDateFormat();
            Log.d("LOL","Alarm will start on " + sdf.format(new Date(start.getTimeInMillis()+15000)));

            remindButton.setText("За " + dataIntExtra + " минут");
        } else if (resultCode == RESULT_CANCELED) {
            boolean alarmUp = (PendingIntent.getBroadcast(this, detailObjectforPI.getPosition(),
                    new Intent(this, AlertReceiver.class),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp) {

                stopAlarm(detailObjectforPI.getPosition());
                reminderSetFlag = false;
                remindButton.setText("НАПОМНИТЬ");
            } else {
                Log.d("myTag", "There is no alarm at" + detailObjectforPI.getPosition());
            }

        }
    }

    public void startAlarm(Calendar c, String name, String divorce, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("bridge_name", name);
        intent.putExtra("divorce_time", divorce);
        intent.putExtra("item_position", position);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void stopAlarm(int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
        Log.d("LOL", "Alarm stopped infa 100");
    }
}

