package kislyakov.a09_1.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkClient {

    private static NetworkClient mInstance;

    private Retrofit mRetrofit;

    public static NetworkClient getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkClient();
        }
        return mInstance;
    }

    private NetworkClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public NetworkInterface getJSONApi() {
        return mRetrofit.create(NetworkInterface.class);
    }
}
