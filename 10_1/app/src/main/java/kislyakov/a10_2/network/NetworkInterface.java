package kislyakov.a10_2.network;


import kislyakov.a10_2.models.BridgeResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anujgupta on 26/12/17.
 */

public interface NetworkInterface {

    @GET("api/v1/bridges/?format=json")
    Observable<BridgeResponse> getBridges(@Query("api_key") String api_key);


}
