package kislyakov.a12_1.ui.bridgesdetail;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kislyakov.a12_1.data.BridgesProvider;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.ui.base.BasePresenter;

public class BridgeDetailPresenter extends BasePresenter<BridgeDetailMvpView> {
    private final BridgesProvider mBridgesProvider;
    private Disposable mDisposable;

    @Inject
    public BridgeDetailPresenter(BridgesProvider bridgesProvider) {
        mBridgesProvider = bridgesProvider;
    }

    @Override
    public void attachView(BridgeDetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void loadSingleBridge(int bridgeId) {
        checkViewAttached();

        mDisposable = mBridgesProvider.getSingleBridge(bridgeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Bridge bridge) -> getMvpView().showSingleBridge(bridge),
                        error -> {
                            error.printStackTrace();
                            getMvpView().showLoadingError();
                        });
    }

}
