package kislyakov.a12_1.data.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import io.reactivex.Single;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.data.models.BridgeResponse;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkService {

    @GET("bridges/?format=json")
    Single<BridgeResponse> getBridges();

    @GET("bridges/{id}/?format=json")
    Single<Bridge> getBridgeInfo(@Path("id") int bridgeId);

    class Creator {

        static String TIME_FORMAT = "HH:mm:ss";
        static String ENDPOINT = "http://gdemost.handh.ru/api/v1/";

        public static NetworkService newNetworkService(Context context) {

            Gson gson = new GsonBuilder()
                    .setDateFormat(TIME_FORMAT)
                    .create();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

            okBuilder.addInterceptor(new ChuckInterceptor(context));


            OkHttpClient client = okBuilder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(NetworkService.class);
        }
    }
}
