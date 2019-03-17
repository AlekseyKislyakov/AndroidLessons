package kislyakov.a07_1.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kislyakov.a07_1.DetailObject;
import kislyakov.a07_1.R;
import kislyakov.a07_1.TimePickerActivity;
import kislyakov.a07_1.adapters.BridgesAdapter;
import kislyakov.a07_1.models.Divorce;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent parentIntent = getIntent();
        setIntent(parentIntent);

        setContentView(R.layout.activity_detail);

        TextView nameTV = findViewById(R.id.nameDetailTV);
        TextView divorceTV = findViewById(R.id.divorce_textView);
        TextView decriptionTV = findViewById(R.id.description_textView);

        ImageView mainPicture = findViewById(R.id.detailBridgePictureIV);
        ImageView bridgeStateIV = findViewById(R.id.statusSmallIV);

        if (parentIntent.hasExtra("LOL")) {
            DetailObject detailObject = (DetailObject) getIntent().getParcelableExtra("LOL");
            Divorce temp = new Divorce();
            int stateBridge = temp.DivorceState(detailObject.getDivorces());
            if (stateBridge == 1) {
                bridgeStateIV.setBackgroundResource(R.drawable.ic_brige_normal);
                Glide
                        .with(this)
                        .load("http://gdemost.handh.ru/" + detailObject.getPictureOpen())
                        .into(mainPicture);
                nameTV.setText(detailObject.getBridgeName());
            } else if (stateBridge == 0) {
                bridgeStateIV.setBackgroundResource(R.drawable.ic_brige_soon);
                Glide
                        .with(this)
                        .load("http://gdemost.handh.ru/" + detailObject.getPictureOpen())
                        .into(mainPicture);
                nameTV.setText(detailObject.getBridgeName());
            } else {
                bridgeStateIV.setBackgroundResource(R.drawable.ic_brige_late);
                Glide
                        .with(this)
                        .load("http://gdemost.handh.ru/" + detailObject.getPictureClosed())
                        .into(mainPicture);
                nameTV.setText(detailObject.getBridgeName());
            }
            String divorces = temp.DivorceConverter(detailObject.getDivorces());
            divorceTV.setText(divorces);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                decriptionTV.setText(Html.fromHtml(detailObject.getBridgeDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                decriptionTV.setText(Html.fromHtml(detailObject.getBridgeDescription()));
            }
            Intent intent = new Intent(this, TimePickerActivity.class);
            startActivity(intent);
        } else {
            nameTV.setText("Ты долбаеб");
        }

    }
}
