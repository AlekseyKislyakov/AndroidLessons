package kislyakov.a06_1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Fragment_basic.onSomeEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_item1()).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.bottom_navigation_item1:
                                selectedFragment = new Fragment_item1();
                                break;
                            case R.id.bottom_navigation_item2:
                                selectedFragment = new Fragment_item2();
                                break;
                            case R.id.bottom_navigation_item3:
                                selectedFragment = new Fragment_item3();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.appbar_subitem1) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Subitem 1 pressed", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        } else if (id == R.id.appbar_subitem2) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Subitem 2 pressed", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.appbar_subitem3) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Subitem 3 pressed", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.appbar_search) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Search pressed", Toast.LENGTH_SHORT);
            toast.show();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Item 1 in navigation bar pressed", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_item2) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Item 2 in navigation bar pressed", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.nav_item3) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Item 3 in navigation bar pressed", Toast.LENGTH_SHORT);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void someEvent(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
