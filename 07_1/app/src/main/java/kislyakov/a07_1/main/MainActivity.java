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

import kislyakov.a07_1.R;
import kislyakov.a07_1.adapters.BridgesAdapter;
import kislyakov.a07_1.models.BridgeResponse;


public class MainActivity extends AppCompatActivity implements MainViewInterface {

    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    //Added in Part 2 of the series
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String TAG = "MainActivity";
    RecyclerView.Adapter adapter;
    MainPresenter mainPresenter;
    //RecyclerView rvMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        setupMVP();
        setupViews();
        getMovieList();
    }



    private void setupMVP() {
        mainPresenter = new MainPresenter(this);
    }

    private void setupViews(){
        //Added in Part 2 of the series
        setSupportActionBar(toolbar);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMovieList() {

     mainPresenter.getBridges();

    }



    @Override
    public void showToast(String str) {
        Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
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
    public void displayBridges(BridgeResponse bridgeResponse) {
        if(bridgeResponse!=null) {
            //Log.d(TAG,movieResponse.getResults().get(1).getTitle());
            adapter = new BridgesAdapter(bridgeResponse.getObjects(), MainActivity.this);
            rvMovies.setAdapter(adapter);
        }else{
            Log.d(TAG,"Movies response null");
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
        if(id == R.id.search){
            showToast("Search Clicked");
        }

        return super.onOptionsItemSelected(item);
    }
}
