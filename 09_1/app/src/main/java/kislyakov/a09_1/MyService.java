package kislyakov.a09_1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import kislyakov.a09_1.models.Example;
import kislyakov.a09_1.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service {

    private final IBinder binder = new LocalBinder();

    private ServiceCallbacks serviceCallbacks = null;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        startWeatherUpdate();
        Log.d("myTag", "onBind MyService");
        return binder;
    }

    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    public String startWeatherUpdate() {

        final String[] result = {""};
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                NetworkClient.getInstance()
                        .getJSONApi()
                        .getWeather("")
                        .enqueue(new Callback<Example>() {
                            @Override
                            public void onResponse(Call<Example> call, Response<Example> response) {
                                Example ex = response.body();
                                Log.d("myTag", ex.getMain().getTemp().toString());
                                result[0] = ex.getMain().getTemp().toString();
                                if (serviceCallbacks != null) {
                                    serviceCallbacks.startWeatherUpdate(result[0]);
                                }
                            }

                            @Override
                            public void onFailure(Call<Example> call, Throwable t) {
                                Log.d("myTag", "Failed " + t.toString());
                            }
                        });
                handler.postDelayed(this, 60000);
            }

        };
        handler.post(runnable);

        return result[0];
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }
}
