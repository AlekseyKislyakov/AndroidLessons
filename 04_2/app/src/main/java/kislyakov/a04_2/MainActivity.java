package kislyakov.a04_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    private RecyclerView numbersList;
    private NumbersAdapter numbersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        numbersList = findViewById(R.id.rv_data);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        numbersList.setLayoutManager(layoutManager);

        numbersAdapter = new NumbersAdapter(5);
        numbersList.setAdapter(numbersAdapter);


    }
}
