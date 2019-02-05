package kislyakov.a03_1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView profileCard = findViewById(R.id.profile_text);
        profileCard.setText("Карта №12345678\nСпециалист");

        TextView userName = findViewById(R.id.name_textView);
        userName.setText("Василий");

        TextView userSurname = findViewById(R.id.surname_textView);
        userSurname.setText("Уткин");

        TextView userLogin = findViewById(R.id.login_textView);
        userLogin.setText("abc@gmail.com");

        TextView userEmail = findViewById(R.id.email_textView);
        userEmail.setText("abc@gmail.com");

        TextView userRegion = findViewById(R.id.region_textView);
        userRegion.setText("Мордвиния");


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_pencil) {
            Toast.makeText(this, "Pencil is clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == android.R.id.home){
            Toast.makeText(this, "Back is clicked", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickPencilCV(View view) {
        Toast.makeText(this, "Region pencil is clicked", Toast.LENGTH_SHORT).show();
    }

    public void onClickLogout(View view) {
        Toast.makeText(this, "Logout is clicked", Toast.LENGTH_SHORT).show();
    }
}
