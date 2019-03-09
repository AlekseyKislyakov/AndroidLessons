package kislyakov.a07_1.main;

import kislyakov.a07_1.models.BridgeResponse;

/**
 * Created by anujgupta on 26/12/17.
 */

public interface MainViewInterface {

    void showToast(String s);
    void showProgressBar();
    void hideProgressBar();
    void displayBridges(BridgeResponse bridgeResponse);
    void displayError(String s);
}
