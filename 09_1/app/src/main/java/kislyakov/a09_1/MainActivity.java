package kislyakov.a09_1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements ServiceCallbacks{

    private MyService myService;
    private boolean bound = false;
    TextView temperatureTV;
    ServiceConnection serviceConnection;

    @Nullable
    MyService.LocalBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperatureTV = findViewById(R.id.temp_textview);
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder iBinder) {
                // cast the IBinder and get MyService instance
                Log.d("myTag", "MainActivity onServiceConnected");
                binder = (MyService.LocalBinder) iBinder;
                myService = binder.getService();
                bound = true;// register
                myService.setCallbacks(MainActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                bound = false;
            }
        };
        Intent intent = new Intent(this, MyService.class);
        getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            //myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    /** Callbacks for service binding, passed to bindService() */
    @Override
    public void startWeatherUpdate(String temperature) {
        temperatureTV.setText(temperature);
    }
}
