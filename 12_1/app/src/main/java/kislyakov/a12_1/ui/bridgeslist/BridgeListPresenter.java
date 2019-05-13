package kislyakov.a12_1.ui.bridgeslist;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kislyakov.a12_1.data.BridgesProvider;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.ui.base.BasePresenter;

public class BridgeListPresenter extends BasePresenter<BridgeListMvpView> {
    private final BridgesProvider mBridgesProvider;
    private Disposable mDisposable;

    @Inject
    public BridgeListPresenter(BridgesProvider bridgesProvider) {
        mBridgesProvider = bridgesProvider;
    }

    @Override
    public void attachView(BridgeListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void loadBridges() {
        checkViewAttached();

        mDisposable = mBridgesProvider.getBridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bridges -> getMvpView().showBridges(bridges),
                        error -> {
                            error.printStackTrace();
                            getMvpView().showLoadingError();
                        });
    }

}
