package kislyakov.a12_1;

import android.app.Application;
import android.content.Context;

import kislyakov.a12_1.injection.component.ApplicationComponent;
import kislyakov.a12_1.injection.component.DaggerApplicationComponent;
import kislyakov.a12_1.injection.module.ApplicationModule;

public class BaseApp extends Application {
    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static BaseApp get(Context context) {
        return (BaseApp) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
