package kislyakov.a12_1.ui.bridgesdetail;

import java.util.List;

import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.ui.base.MvpView;

public interface BridgeDetailMvpView extends MvpView {
    void showLoadingError();
    void showBridges(List<Bridge> bridges);
    void showSingleBridge(Bridge bridge);
    void showProgressView();
}
