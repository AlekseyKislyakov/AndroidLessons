package kislyakov.a04_2;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private RecyclerView numbersList;
    private NumbersAdapter numbersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        numbersList = findViewById(R.id.rv_data);
        numbersList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        numbersList.setLayoutManager(layoutManager);


        numbersAdapter = new NumbersAdapter(10, new NumbersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item, View v) {
                Snackbar.make(v,item,Toast.LENGTH_SHORT).setAction("Action", null).show();;

            }
        });
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(numbersAdapter.getItemViewType(position)){
                    case NumbersAdapter.DESCRIPTION_RED:
                        return 1;
                    case NumbersAdapter.DEFAULT:
                        return 1;
                    case NumbersAdapter.ONEINTWO:
                        return 2;
                    case NumbersAdapter.BASECELL:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        numbersList.setAdapter(numbersAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.ic_info_white) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Information");
            builder.setMessage("Hey brother, you've clicked the info");

            // add a button
            builder.setPositiveButton("OK", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();;
            return true;
        }
        else if (id == R.id.ic_home){
            Toast.makeText(this, "Home is clicked", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
