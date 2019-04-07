package kislyakov.a09_1.network;


import io.reactivex.Observable;
import kislyakov.a09_1.models.Example;
import kislyakov.a09_1.models.Main;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anujgupta on 26/12/17.
 */

public interface NetworkInterface {

    @GET("weather?q=saransk&units=metric&appid=a924f0f5b30839d1ecb95f0a6416a0c2")
    Call<Example> getWeather(@Query("api_key") String api_key);

}
