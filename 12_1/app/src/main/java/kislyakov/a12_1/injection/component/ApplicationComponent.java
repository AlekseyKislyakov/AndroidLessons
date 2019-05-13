package kislyakov.a12_1.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import kislyakov.a12_1.data.BridgesProvider;
import kislyakov.a12_1.data.network.NetworkService;
import kislyakov.a12_1.injection.ApplicationContext;
import kislyakov.a12_1.injection.module.ApplicationModule;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BridgesProvider bridgesProvider);

    @ApplicationContext
    Context context();
    Application application();
    BridgesProvider bridgesProvider();
}
