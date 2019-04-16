package kislyakov.a10_1.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kislyakov.a10_1.R;
import kislyakov.a10_1.classes.AlertReceiver;
import kislyakov.a10_1.models.DetailObject;
import kislyakov.a10_1.models.Divorce;

import static kislyakov.a10_1.models.Divorce.DivorceConverter;
import static kislyakov.a10_1.models.Divorce.DivorceState;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<DetailObject> detailObjects;

    CardView infoCardView;
    ImageView rowImageView;
    TextView rowTitleTextView;
    TextView rowTimeTextView;
    Button rowStatusButton;
    int markerId;
    public static final int REQUEST_CODE_PERMISSION = 101;
    ArrayList<MarkerOptions> markerOptionsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        infoCardView = findViewById(R.id.row_bridge);

        rowImageView = infoCardView.findViewById(R.id.bridgeStatusImage);
        rowTitleTextView = infoCardView.findViewById(R.id.bridgeTitle);
        rowTimeTextView = infoCardView.findViewById(R.id.bridgeTime);
        rowStatusButton = infoCardView.findViewById(R.id.kolokolButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_maps);



        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();
        Intent parentIntent = getIntent();
        if (parentIntent.hasExtra("detailObjects")) {
            detailObjects = getIntent().getParcelableArrayListExtra("detailObjects");

            Log.d("myTag", detailObjects.get(1).getBridgeName());
        }



        /*BehaviorSubject mapSubject = BehaviorSubject.create();
        Observable.create(
                (ObservableOnSubscribe<GoogleMap>) (ObservableEmitter<GoogleMap> e) -> {
                    mapFragment.getMapAsync(e::onNext);
                })
                .subscribe(mapSubject);*/


        mapFragment.getMapAsync(this);

    }

    private void refreshMarkers(){
        for (int i = 0; i < markerOptionsArrayList.size(); i++){
            int openState = DivorceState(detailObjects.get(i).getDivorces());
            if(openState > 0){
                markerOptionsArrayList.get(i).icon(getBitmapDescriptor(R.drawable.ic_brige_normal));
            }
            else if(openState == 0){
                markerOptionsArrayList.get(i).icon(getBitmapDescriptor(R.drawable.ic_brige_soon));
            }
            else {
                markerOptionsArrayList.get(i).icon(getBitmapDescriptor(R.drawable.ic_brige_late));
            }

        }
    }

    private void refreshRow(){
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), markerId,
                new Intent(getApplicationContext(), AlertReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp){
            Log.d("myTag", "Alarm is already active");
            rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_on);
        }
        else {
            rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_off);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        markerOptionsArrayList = new ArrayList<>();
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.93,30.36),11));
        for (int i = 0; i < detailObjects.size(); i++) {
            MarkerOptions mo = new MarkerOptions()
                    .position(new LatLng(detailObjects.get(i).getLat(), detailObjects.get(i).getLng()))
                    .title(detailObjects.get(i).getBridgeName())
                    .anchor((float) 0.5,(float)0.5);

            int openState = DivorceState(detailObjects.get(i).getDivorces());
            if(openState > 0){
                mo.icon(getBitmapDescriptor(R.drawable.ic_brige_normal));
            }
            else if(openState == 0){
                mo.icon(getBitmapDescriptor(R.drawable.ic_brige_soon));
            }
            else {
                mo.icon(getBitmapDescriptor(R.drawable.ic_brige_late));
            }
            markerOptionsArrayList.add(mo);
            mMap.addMarker(mo);
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                refreshMarkers();
                refreshRow();
                handler.postDelayed(this, 15000);
            }
        };
        handler.post(runnable);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("myTag", marker.getId());
                infoCardView.setVisibility(View.VISIBLE);

                markerId = markerToIntConverter(marker.getId());

                infoCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                        intent.putExtra("LOL", detailObjects.get(markerId));
                        startActivityForResult(intent,2);
                    }
                });

                boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), markerId,
                        new Intent(getApplicationContext(), AlertReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);

                if (alarmUp){
                    Log.d("myTag", "Alarm is already active");
                    rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_on);
                }
                else {
                    rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_off);
                }

                rowTitleTextView.setText(detailObjects.get(markerId).getBridgeName());
                rowTimeTextView.setText(DivorceConverter(detailObjects.get(markerId).getDivorces()));

                int openState = DivorceState(detailObjects.get(markerId).getDivorces());
                if(openState > 0){
                    rowImageView.setBackgroundResource(R.drawable.ic_brige_normal);
                }
                else if(openState == 0){
                    rowImageView.setBackgroundResource(R.drawable.ic_brige_soon);
                }
                else {
                    rowImageView.setBackgroundResource(R.drawable.ic_brige_late);
                }
                //marker.
                return true;
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this,"LOCATION PERMISSION HAS BEEN DENIED!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshRow();
        refreshMarkers();
    }



    private int markerToIntConverter(String markerId){
        return Integer.parseInt(markerId.substring(1));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) getDrawable(id);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }
}
