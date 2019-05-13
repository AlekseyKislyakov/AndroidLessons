package kislyakov.a12_1.ui.base;

import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.atomic.AtomicLong;

import kislyakov.a12_1.BaseApp;
import kislyakov.a12_1.injection.component.ActivityComponent;
import kislyakov.a12_1.injection.component.ConfigPersistentComponent;
import kislyakov.a12_1.injection.component.DaggerConfigPersistentComponent;
import kislyakov.a12_1.injection.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "myTag";

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);

        if (configPersistentComponent == null) {
            Log.d(TAG, "onCreate: configPersistentComp");
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(BaseApp.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Log.d(TAG, "onDestroy: "+ "Clearing ConfigPersistentComponent id=" + mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

}