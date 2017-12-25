package groupe_9.com.fabellas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    public static final int ZOOM = 10;
    private static final int REQUEST_APPLICATION_SETTINGS_CODE = 1000;

    private GoogleMap googleMap;
    private FusedLocationProviderClient locationProviderClient;
    private Location lastLocation;
    private View mapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapContainer = findViewById(R.id.map_container);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        MapActivityPermissionsDispatcher.goToMyLocationWithPermissionCheck(this, googleMap);
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
}
