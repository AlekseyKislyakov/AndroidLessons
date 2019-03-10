package kislyakov.a07_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import kislyakov.a07_1.DetailActivity;
import kislyakov.a07_1.R;
import kislyakov.a07_1.adapters.BridgesAdapter;
import kislyakov.a07_1.models.BridgeResponse;
import kislyakov.a07_1.models.Object;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        List<Object> bridgeList;
        // getIntent() should always return the most recent
        setIntent(intent);
        BridgeResponse.getObjects();
        Intent parentIntent = getIntent();
        int getExtra = 0;

        if (parentIntent.hasExtra(Intent.EXTRA_INDEX)) {
            getExtra = parentIntent.getIntExtra(Intent.EXTRA_INDEX, 0);
        }
        TextView nameTV = findViewById(R.id.nameDetailTV);
        nameTV.setText(bridgeList.get(getExtra).getName());

    }
}
