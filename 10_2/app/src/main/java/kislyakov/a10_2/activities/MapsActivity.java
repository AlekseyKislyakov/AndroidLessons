package kislyakov.a10_2.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import kislyakov.a10_2.R;
import kislyakov.a10_2.classes.AlertReceiver;
import kislyakov.a10_2.models.DetailObject;

import static kislyakov.a10_2.models.Divorce.DivorceConverter;
import static kislyakov.a10_2.models.Divorce.DivorceState;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int REQUEST_CODE_PERMISSION = 1000;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Boolean locationUpdateStart = false;
    Boolean settingsSet = false;


    private GoogleMap mMap;
    ArrayList<DetailObject> detailObjects;

    CardView infoCardView;
    ImageView rowImageView;
    TextView rowTitleTextView;
    TextView rowTimeTextView;
    Button rowStatusButton;

    int markerId;


    ArrayList<Marker> markerArrayList;
    Marker locationMarker;

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

        mapFragment.getMapAsync(this);


    }

    private void refreshMarkers() {
        for (int i = 0; i < markerArrayList.size(); i++) {
            int openState = DivorceState(detailObjects.get(i).getDivorces());
            if (openState > 0) {
                markerArrayList.get(i).setIcon(getBitmapDescriptor(R.drawable.ic_brige_normal));
            } else if (openState == 0) {
                markerArrayList.get(i).setIcon(getBitmapDescriptor(R.drawable.ic_brige_soon));
            } else {
                markerArrayList.get(i).setIcon(getBitmapDescriptor(R.drawable.ic_brige_late));
            }

        }
    }

    private void refreshRow() {
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), markerId,
                new Intent(getApplicationContext(), AlertReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d("myTag", "Alarm is already active");
            rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_on);
        } else {
            rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_off);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        markerArrayList = new ArrayList<>();
        mMap = googleMap;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        } else {
            buildLocationRequest();
            buildLocationCallback();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            settingsSet = true;
            locationUpdateStart = true;
            startLocationUpdates();

        }


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.93, 30.36), 11));
        for (int i = 0; i < detailObjects.size(); i++) {
            MarkerOptions mo = new MarkerOptions()
                    .position(new LatLng(detailObjects.get(i).getLat(), detailObjects.get(i).getLng()))
                    .title(detailObjects.get(i).getBridgeName())
                    .anchor((float) 0.5, (float) 0.5);

            int openState = DivorceState(detailObjects.get(i).getDivorces());
            if (openState > 0) {
                mo.icon(getBitmapDescriptor(R.drawable.ic_brige_normal));
            } else if (openState == 0) {
                mo.icon(getBitmapDescriptor(R.drawable.ic_brige_soon));
            } else {
                mo.icon(getBitmapDescriptor(R.drawable.ic_brige_late));
            }

            markerArrayList.add(mMap.addMarker(mo));

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


                markerId = markerToIntConverter(marker.getId());
                if (marker.getId().equals(locationMarker.getId())) {
                    return true;
                } else {
                    infoCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (markerId == markerToIntConverter(locationMarker.getId())) {

                            } else {
                                Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                                intent.putExtra("LOL", detailObjects.get(markerId));
                                startActivityForResult(intent, 2);
                            }
                        }
                    });
                    infoCardView.setVisibility(View.VISIBLE);
                    boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), markerId,
                            new Intent(getApplicationContext(), AlertReceiver.class),
                            PendingIntent.FLAG_NO_CREATE) != null);

                    if (alarmUp) {
                        Log.d("myTag", "Alarm is already active");
                        rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_on);
                    } else {
                        rowStatusButton.setBackgroundResource(R.drawable.ic_kolocol_off);
                    }

                    rowTitleTextView.setText(detailObjects.get(markerId).getBridgeName());
                    rowTimeTextView.setText(DivorceConverter(detailObjects.get(markerId).getDivorces()));

                    int openState = DivorceState(detailObjects.get(markerId).getDivorces());
                    if (openState > 0) {
                        rowImageView.setBackgroundResource(R.drawable.ic_brige_normal);
                    } else if (openState == 0) {
                        rowImageView.setBackgroundResource(R.drawable.ic_brige_soon);
                    } else {
                        rowImageView.setBackgroundResource(R.drawable.ic_brige_late);
                    }
                    return true;
                }


            }
        });

    }

    private void startLocationUpdates() {
        if(settingsSet){
            locationUpdateStart = true;
        }
        if (locationUpdateStart) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            Log.d("myTag", "Updating started!");
        }

    }

    private void stopLocationUpdates() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, (task -> {
                    locationUpdateStart = false;
                    Log.d("myTag", "Updating stopped!");
                }));

    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (locationMarker == null) {
                        locationMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .anchor((float) 0.5, (float) 0.5)
                                .icon(getBitmapDescriptor(R.drawable.ic_my_location_blue_24dp)));
                    } else {
                        refreshLocation(location);
                    }

                }


            }
        };

    }

    private void refreshLocation(Location location) {
        locationMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(3000)
                .setSmallestDisplacement(10);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshRow();
        refreshMarkers();
    }


    private int markerToIntConverter(String markerId) {
        return Integer.parseInt(markerId.substring(1));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */


    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        startLocationUpdates();
        //updateMarker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
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

