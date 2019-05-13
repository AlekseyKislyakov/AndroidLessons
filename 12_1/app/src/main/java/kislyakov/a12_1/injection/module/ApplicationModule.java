package kislyakov.a12_1.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import kislyakov.a12_1.data.BridgesProvider;
import kislyakov.a12_1.data.BridgesProvider_Factory;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.data.models.BridgeResponse;
import kislyakov.a12_1.data.network.NetworkService;
import kislyakov.a12_1.injection.ApplicationContext;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    NetworkService provideBridgesProvider() {
        return NetworkService.Creator.newNetworkService(mApplication);
    }

}
