package kislyakov.a10_2.mvp;

import kislyakov.a10_2.models.BridgeResponse;

public interface MainViewInterface {

    void showToast(String s);
    void showProgressBar();
    void hideProgressBar();
    void displayBridges(BridgeResponse bridgeResponse);
    void displayError(String s);
}
