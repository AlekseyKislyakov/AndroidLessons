package kislyakov.a12_1.data;

import java.util.List;
import java.util.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.data.network.NetworkService;

@Singleton
public class BridgesProvider {

        private final NetworkService networkService;

        @Inject
        public BridgesProvider(NetworkService apiService) {
            this.networkService = apiService;
        }

        public Single<Bridge> getSingleBridge(int bridgeId){
            return networkService.getBridgeInfo(bridgeId);
        }

        public Single<List<Bridge>> getBridges() {
            return networkService.getBridges()
                    .map(bridgeResponse -> bridgeResponse.getBridges());
        }



}
