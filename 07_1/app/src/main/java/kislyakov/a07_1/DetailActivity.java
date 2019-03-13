package kislyakov.a07_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.BitSet;
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
        Intent parentIntent = getIntent();
        setIntent(parentIntent);

        setContentView(R.layout.activity_detail);

        TextView nameTV = findViewById(R.id.nameDetailTV);
        TextView divorceTV = findViewById(R.id.divorce_textView);
        ImageView mainPicture = findViewById(R.id.detailBridgePictureIV);

        if (parentIntent.hasExtra("LOL")) {
            DetailObject detailObject = (DetailObject) getIntent().getParcelableExtra("LOL");

            Glide
                    .with(this)
                    .load("http://gdemost.handh.ru/" + detailObject.getPictureClosed())
                    .into(mainPicture);
            nameTV.setText(detailObject.getBridgeName());
        } else {
            nameTV.setText("Ты долбаеб");
        }

    }
}
