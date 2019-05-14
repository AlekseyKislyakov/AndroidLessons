package kislyakov.a10_2.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kislyakov.a10_2.R;
import kislyakov.a10_2.activities.DetailActivity;
import kislyakov.a10_2.activities.MapsActivity;
import kislyakov.a10_2.adapters.BridgesAdapter;
import kislyakov.a10_2.models.BridgeResponse;
import kislyakov.a10_2.models.DetailObject;
import kislyakov.a10_2.models.Object;


public class MainActivity extends AppCompatActivity implements MainViewInterface {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    @BindView(R.id.rvBridges)
    RecyclerView rvBridges;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.buttonRetry)
    Button buttonRetry;

    private String TAG = "MainActivity";
    RecyclerView.Adapter adapter;
    MainPresenter mainPresenter;
    BridgeResponse bridgeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupMVP();
        setupViews();
        getBridgeList();

    }


    private void setupMVP() {
        mainPresenter = new MainPresenter(this);
    }

    private void setupViews() {
        //Added in Part 2 of the series
        setSupportActionBar(toolbar);
        buttonRetry.setOnClickListener(l -> getBridgeList());
        rvBridges.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getBridgeList() {
        mainPresenter.getBridges();

    }


    @Override
    public void showToast(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        viewFlipper.setDisplayedChild(VIEW_LOADING);
    }

    @Override
    public void hideProgressBar() {
        viewFlipper.setDisplayedChild(VIEW_DATA);
    }


    @Override
    public void displayBridges(final BridgeResponse bridgeResponse) {
        if (bridgeResponse != null) {
            this.bridgeResponse = bridgeResponse;
            adapter = new BridgesAdapter(bridgeResponse.getObjects(), MainActivity.this, positionItem -> {
                Object object = bridgeResponse.getObjects().get(positionItem);
                DetailObject detailObject = new DetailObject(object.getPhotoOpen(), object.getPhotoClose(),
                        object.getName(), object.getDivorces(), object.getDescription(), positionItem,
                        object.getLat(),object.getLng());
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("LOL", detailObject);
                startActivityForResult(intent,1);
            });
            rvBridges.setAdapter(adapter);

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    handler.postDelayed(this, 15000);
                }
            };

            handler.post(runnable);
        } else {
            Log.d(TAG, "Bridges response null");
        }
    }

    @Override
    public void displayError(String e) {

        showToast(e);
        viewFlipper.setDisplayedChild(VIEW_ERROR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.map_item) {
            Intent intent = new Intent(this, MapsActivity.class);
            //intent.putExtra("bridges", bridgeResponse);
            ArrayList<DetailObject> detailObject = new ArrayList<>();
            for (int i = 0; i < bridgeResponse.getObjects().size(); i++) {
                Object object = bridgeResponse.getObjects().get(i);
                detailObject.add(new DetailObject(object.getPhotoOpen(), object.getPhotoClose(),
                        object.getName(), object.getDivorces(), object.getDescription(), i,
                        object.getLat(),object.getLng()));
            }
            intent.putExtra("detailObjects", detailObject);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //finish();
            //Toast.makeText(this, "TO BE DONE LATER", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
    }

}
