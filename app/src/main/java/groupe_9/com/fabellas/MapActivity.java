package groupe_9.com.fabellas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import groupe_9.com.fabellas.bo.PlaceTag;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapActivity
        extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener
{
    public static final int ZOOM = 10;
    private static final int REQUEST_APPLICATION_SETTINGS_CODE = 1000;
    public static final String PLACE_ID = "placeID";

    private GoogleMap googleMap;
    private String currentPlaceID;
    private FusedLocationProviderClient locationProviderClient;
    private Location lastLocation;
    private View mapContainer;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapContainer = findViewById(R.id.map_container);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .build();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (googleApiClient != null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (googleApiClient != null && googleApiClient.isConnected())
        {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        this.googleMap.setOnInfoWindowClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void goToMyLocation(GoogleMap googleMap)
    {
        googleMap.setMyLocationEnabled(true);

        locationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Location> task)
                    {
                        if (task.isSuccessful() && task.getResult() != null)
                        {
                            lastLocation = task.getResult();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(),
                                    lastLocation.getLongitude()), MapActivity.ZOOM));
                        }
                        else
                        {
                            Toast.makeText(MapActivity.this, "Something went wrong looking for position", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        lookForPlaces();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MapActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(final PermissionRequest request)
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForLocation()
    {
        displaySnackBar();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onNeverAskAgain()
    {
        displaySnackBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_APPLICATION_SETTINGS_CODE:
                MapActivityPermissionsDispatcher.goToMyLocationWithPermissionCheck(this, this.googleMap);
                break;
        }
    }

    private void displaySnackBar()
    {
        final Snackbar snackbar = Snackbar.make(mapContainer, R.string.error_permission_geolocation, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.open_settings, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
                startActivityForResult(intent, MapActivity.REQUEST_APPLICATION_SETTINGS_CODE);

                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(this, R.string.places_api_connection_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        MapActivityPermissionsDispatcher.goToMyLocationWithPermissionCheck(this, googleMap);
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @SuppressLint("MissingPermission")
    public void lookForPlaces()
    {
        final PendingResult<PlaceLikelihoodBuffer> currentPlaces = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);


        currentPlaces.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>()
        {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces)
            {
                boolean centerToLocation = false;
                for (PlaceLikelihood placeLikelihood : likelyPlaces)
                {
                    final Place place = placeLikelihood.getPlace();


                    if (!centerToLocation)
                    {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                        centerToLocation = true;
                    }


                    Log.i("thomasecalle", String.format("Place '%s' found with id: '%s'", place.getName(), place.getId()));

                    final Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(place.getLatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title(place.getName().toString()));

                    final PlaceTag placeTag = new PlaceTag(place.getName().toString(), place.getId());
                    marker.setTag(placeTag);

                }
                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        //Toast.makeText(this, "Marker id : " + marker.getTag(), Toast.LENGTH_LONG).show();
        final Intent intent = new Intent(this, PlaceStoriesActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.PLACE_ID, (PlaceTag) marker.getTag());
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
