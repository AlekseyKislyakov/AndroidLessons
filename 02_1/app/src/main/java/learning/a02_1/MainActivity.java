package learning.a02_1;

import android.app.NativeActivity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String mLocale = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocale = getResources().getConfiguration().locale.toString();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        final String newLocale = newConfig.locale.toString();
        Locale.setDefault(newConfig.locale);
    }

}
