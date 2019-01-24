package learning.a02_1;

import android.app.NativeActivity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        Locale.setDefault(newConfig.locale);
    }

}
