package kislyakov.a09_1;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements ServiceCallbacks {

    private TemperatureService temperatureService;
    private boolean bound = false;
    public static final int REQUEST_CODE_PERMISSION = 101;

    ServiceConnection serviceConnection;
    Button fileDownloadButton;
    TextView temperatureTV;
    ImageView fileDownloadImageView;

    private BroadcastReceiver broadcastReceiver;

    @Nullable
    TemperatureService.LocalBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperatureTV = findViewById(R.id.temperature_textview);
        fileDownloadImageView = findViewById(R.id.filedownload_imageview);

        fileDownloadButton = findViewById(R.id.filedownload_button);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            fileDownloadButton.setText("Request permission");
            fileDownloadButton.setOnClickListener(l-> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION));
        }
        else {
            bindAndStartService();
        }
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder iBinder) {
                // cast the IBinder and get TemperatureService instance
                Log.d("myTag", "MainActivity onServiceConnected");
                binder = (TemperatureService.LocalBinder) iBinder;
                temperatureService = binder.getService();
                bound = true;// register
                temperatureService.setCallbacks(MainActivity.this);
            }


            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                bound = false;
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bindAndStartService();
                } else {
                    Toast.makeText(this,"YOU HAVE DENIED THE REQUEST, TRY AGAIN", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void bindAndStartService() {
        fileDownloadButton.setText("Download");
        fileDownloadButton.setOnClickListener(l -> {
            if (isServiceRunning(DownloadService.class)) {
                Toast.makeText(getApplicationContext(), "Service is already running", Toast.LENGTH_SHORT).show();
            } else {
                fileDownloadButton.setText("Download initiated");
                Intent downloadIntent = new Intent(getApplicationContext(), DownloadService.class);
                //startService(downloadIntent);
                ContextCompat.startForegroundService(getApplicationContext(), downloadIntent);
            }

        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra("test")) {
                    fileDownloadButton.setText("Downloading ... " + intent.getIntExtra("test", 0) + "%");
                }
                if (intent.hasExtra("zip")) {
                    fileDownloadButton.setText("Unzipping...");
                }
                if (intent.hasExtra("finish")) {
                    fileDownloadButton.setText("Download");
                    Log.d("myTag", "Finish + " + intent.getStringExtra("finish"));
                    Bitmap bmp = BitmapFactory.decodeFile(intent.getStringExtra("finish"));
                    fileDownloadImageView.setImageBitmap(bmp);
                }
                Log.d("myTag", "RECEIVED!");
            }
        };

        IntentFilter intFilter = new IntentFilter("LOL");
        registerReceiver(broadcastReceiver, intFilter);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TemperatureService.class);
        getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            temperatureService.setCallbacks(null); // unregister
            getApplicationContext().unbindService(serviceConnection);
            bound = false;
        }
    }

    /**
     * Callbacks for service binding, passed to bindService()
     */
    @Override
    public void startWeatherUpdate(String temperature) {
        temperatureTV.setText("Temperature " + temperature);
    }
}
