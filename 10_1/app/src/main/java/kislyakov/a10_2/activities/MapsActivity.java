package kislyakov.a10_2.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


import kislyakov.a10_2.R;
import kislyakov.a10_2.classes.AlertReceiver;
import kislyakov.a10_2.models.DetailObject;
import kislyakov.a10_2.models.Divorce;

import static kislyakov.a10_2.models.Divorce.DivorceConverter;
import static kislyakov.a10_2.models.Divorce.DivorceState;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private FusedLocationProviderClient mFusedLocationClient;

    private Boolean mRequestingLocationUpdates;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    private GoogleMap mMap;
    ArrayList<DetailObject> detailObjects;

    CardView infoCardView;
    ImageView rowImageView;
    TextView rowTitleTextView;
    TextView rowTimeTextView;
    Button rowStatusButton;

    int markerId;

    public static final int REQUEST_CODE_PERMISSION = 101;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    ArrayList<MarkerOptions> markerOptionsArrayList;
    MarkerOptions locationMarkerOptions;

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
        updateValuesFromBundle(savedInstanceState);

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


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
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
            locationMarkerOptions = new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .anchor((float) 0.5,(float)0.5)
                    .icon(getBitmapDescriptor(R.drawable.ic_launcher_background));
            mMap.addMarker(mo);
            mMap.addMarker(locationMarkerOptions);
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

    private boolean checkPermissions() {
        int permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
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

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                //mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateMarker();
        }
    }

    private void updateMarker(){
        locationMarkerOptions.position(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()));
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
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("myTag", "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateMarker();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("myTag", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("myTag", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("myTag", errorMessage);
                                Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateMarker();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }

        updateMarker();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d("myTag", "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        mRequestingLocationUpdates = false;
                        //setButtonsEnabledState();
                    }
                });
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateMarker();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
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
