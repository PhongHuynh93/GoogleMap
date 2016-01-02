package com.phong.googlemap_1.Activity_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.phong.googlemap_1.R;

public class Camera extends AppCompatActivity implements OnMapReadyCallback{

    private static final double LATITUDE = -33.87365;
    private static final double LONGTITUDE = 151.20689;
    private static final int SCROLL_BY_BX = 100;
    private static final CameraPosition SYDNEY = new CameraPosition.Builder().target(new LatLng(-33.87365, 151.20689))
            .zoom(15.5f)
            .bearing(0)
            .tilt(25)
            .build();

    private static final CameraPosition BONDI = new CameraPosition.Builder().target(new LatLng(-33.891614, 151.276417))
            .zoom(15.5f)
            .bearing(300)
            .tilt(50)
            .build();
    private CompoundButton mCustomDurationToggle;
    private CompoundButton mAnimateToggle;
    private SeekBar mCustomDurationBar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // animate
        mAnimateToggle = (CompoundButton)findViewById(R.id.animate);
        mCustomDurationToggle = (CompoundButton)findViewById(R.id.duration_toggle);
        mCustomDurationBar = (SeekBar)findViewById(R.id.duration_bar);

        updateEnableState();

        // map
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEnableState();
    }

    private void updateEnableState() {
        mCustomDurationToggle.setEnabled(mAnimateToggle.isChecked());
        mCustomDurationBar.setEnabled(mAnimateToggle.isChecked() && mCustomDurationToggle.isChecked());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void opStopAnimation(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.stopAnimation();
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onToggleAnimate(View view) {
        updateEnableState();
    }

    public void onScrollLeft(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_BX, 0));
    }

    private void changeCamera(CameraUpdate cameraUpdate) {
        changeCamera(cameraUpdate, null);
    }

    private void changeCamera(CameraUpdate cameraUpdate, GoogleMap.CancelableCallback callback) {
        if (mAnimateToggle.isChecked()) {
            if (mCustomDurationToggle.isChecked()) {
                int duration = mCustomDurationBar.getProgress();
                mMap.animateCamera(cameraUpdate, Math.max(duration, 1), callback);
            }
            else {
                mMap.animateCamera(cameraUpdate, callback);
            }
        }
        else {
            mMap.moveCamera(cameraUpdate);
        }
    }

    public void onScrollUp(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_BX));
    }

    public void onScrollRight(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_BX, 0));
    }

    public void onScrollDown(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_BX));
    }

    public void onZoomIn(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.zoomIn());
    }

    public void onZoomOut(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.zoomOut());
    }

    public void onToggleCustomDuration(View view) {
        updateEnableState();
    }

    public void onGoToSydney(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.newCameraPosition(SYDNEY), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(getBaseContext(), "Animation to Sydney complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Animation to Sydnet cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onGoToBondi(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.newCameraPosition(BONDI));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE, LONGTITUDE), 10));
    }

    public void onTiltMore(View view) {
        if (!checkReady()) {
            return;
        }
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        float currentTilt = currentCameraPosition.tilt;
        float newTilt = currentTilt + 10;

        newTilt = (newTilt > 90) ? 90 : newTilt;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition).tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void onTiltLess(View view) {
        if (!checkReady()) {
            return;
        }
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        float currentTilt = currentCameraPosition.tilt;
        float newTilt = currentTilt - 10;

        newTilt = (newTilt > 0) ? newTilt : 0;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition).tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
