package kislyakov.a10_2.mvp;

import android.util.Log;

import kislyakov.a10_2.models.BridgeResponse;
import kislyakov.a10_2.network.NetworkClient;
import kislyakov.a10_2.network.NetworkInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainPresenterInterface {

    MainViewInterface mvi;
    private String TAG = "MainPresenter";

    public MainPresenter(MainViewInterface mvi) {
        this.mvi = mvi;
    }

    @Override
    public void getBridges() {
        getObservable().subscribeWith(getObserver());
    }

    public Observable<BridgeResponse> getObservable(){
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                            .getBridges("")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<BridgeResponse> getObserver(){
        return new DisposableObserver<BridgeResponse>() {

            @Override
            public void onNext(@NonNull BridgeResponse bridgeResponse) {
                //Log.d(TAG,"OnNext"+bridgeResponse.getObjects());
                mvi.displayBridges(bridgeResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                mvi.displayError("Error fetching bridge Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
                mvi.hideProgressBar();
            }
        };
    }
}
