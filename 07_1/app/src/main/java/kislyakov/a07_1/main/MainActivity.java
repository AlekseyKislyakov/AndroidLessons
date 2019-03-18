package kislyakov.a07_1.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import kislyakov.a07_1.DetailObject;
import kislyakov.a07_1.R;
import kislyakov.a07_1.adapters.BridgesAdapter;
import kislyakov.a07_1.models.BridgeResponse;
import kislyakov.a07_1.models.Object;


public class MainActivity extends AppCompatActivity implements MainViewInterface {

    @BindView(R.id.rvBridges)
    RecyclerView rvBridges;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String TAG = "MainActivity";
    RecyclerView.Adapter adapter;
    MainPresenter mainPresenter;

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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void displayBridges(final BridgeResponse bridgeResponse) {
        if (bridgeResponse != null) {
            //Log.d(TAG,movieResponse.getResults().get(1).getTitle());
            adapter = new BridgesAdapter(bridgeResponse.getObjects(), MainActivity.this, new BridgesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int positionItem) {
                    Object object = bridgeResponse.getObjects().get(positionItem);
                    DetailObject detailObject = new DetailObject(object.getPhotoOpen(), object.getPhotoClose(),
                            object.getName(), object.getDivorces(), object.getDescription());
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("LOL", detailObject);
                    startActivity(intent);
                }
            });
            rvBridges.setAdapter(adapter);
        } else {
            Log.d(TAG, "Bridges response null");
        }
    }

    @Override
    public void displayError(String e) {

        showToast(e);

    }

    //Added in Part 2 of the series
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.search) {

        }

        return super.onOptionsItemSelected(item);
    }
}
