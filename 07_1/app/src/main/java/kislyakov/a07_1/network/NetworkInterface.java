package kislyakov.a07_1.network;


import kislyakov.a07_1.models.BridgeResponse;
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
