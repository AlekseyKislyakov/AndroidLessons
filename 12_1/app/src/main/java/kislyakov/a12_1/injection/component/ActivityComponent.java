package kislyakov.a12_1.injection.component;



import dagger.Subcomponent;
import kislyakov.a12_1.injection.PerActivity;
import kislyakov.a12_1.injection.module.ActivityModule;
import kislyakov.a12_1.ui.bridgesdetail.BridgeDetailActivity;
import kislyakov.a12_1.ui.bridgeslist.BridgeListActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(BridgeListActivity bridgeListActivity);

    void inject(BridgeDetailActivity bridgeDetailActivity);

}
